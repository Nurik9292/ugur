package tm.ugur.services.data_bus;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.EndRouteStopService;
import tm.ugur.services.admin.RouteService;
import tm.ugur.services.admin.StartRouteStopService;
import tm.ugur.services.admin.StopService;
import tm.ugur.services.redis.RedisRouteService;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class BusDestinationDirectionSide {

    private final RouteService routeService;
    private final RedisRouteService redisRouteService;
    private final StartRouteStopService startRouteStopService;
    private final GeometryFactory factory;

    private static final Logger logger = LoggerFactory.getLogger(BusDestinationDirectionSide.class);

    @Autowired
    public BusDestinationDirectionSide(RouteService routeService,
                                       RedisRouteService redisRouteService,
                                       StartRouteStopService startRouteStopService,
                                       GeometryFactory factory) {
        this.routeService = routeService;
        this.redisRouteService = redisRouteService;
        this.startRouteStopService = startRouteStopService;
        this.factory = factory;
    }

    @Transactional
    public List<BusDTO> define(List<BusDTO> buses){

        buses.parallelStream().forEach(bus -> {

            Optional<Route> route = routeService.findByNumber(bus.getNumber());

            if (route.isPresent()) {
                List<StartRouteStop> startRouteStops = startRouteStopService.findByRoute(route.get());

                    if(!startRouteStops.isEmpty()) {

                        Point pointA = startRouteStops.getFirst().getStop().getLocation();
                        Point pointB = startRouteStops.getLast().getStop().getLocation();
                        PointDTO pointBus = bus.getLocation();

                        if(isAtPointA(pointA , pointBus))
                            bus.setSide("front");

                        if(isAtPointB(pointB, pointBus))
                            bus.setSide("back");


                        if(Objects.isNull(bus.getSide()) || bus.getSide().isBlank()){
                            bus.setSide(getBusSide(pointA.getX(), pointA.getY(),
                                    pointB.getX(),pointB.getY(),
                                    bus.getLocation().getLat(), bus.getLocation().getLng()));
                        }

                    }
            }
        });

        return buses;
    }

    private boolean isAtPointA(Point pointA, PointDTO pointB){
        Point pointTarget = factory.createPoint(new Coordinate(pointB.getLat(), pointB.getLng()));
        double distance = pointA.distance(pointTarget);
        return distance <= 100;
    }

    private boolean isAtPointB(Point pointA, PointDTO pointB){
        Point pointTarget = factory.createPoint(new Coordinate(pointB.getLat(), pointB.getLng()));
        double distance = pointA.distance(pointTarget);
        return distance <= 100;
    }



    public String getBusSide(double ax, double ay,
                                  double bx, double by,
                                  double busX, double busY) {

        double vX = bx - ax;
        double vY = by - ay;

        double busVX = busX - ax;
        double busVY = busY - ay;

        double scalarProduct = vX * busVX + vY * busVY;

        if (scalarProduct > 0) {
            return "Bus is moving towards B";
        } else if (scalarProduct < 0) {
            return "back";
        } else {
            return "";
        }
    }

}
