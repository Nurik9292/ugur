package tm.ugur.services.data_bus;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.geo.PointDTO;
import tm.ugur.models.Bus;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.RouteService;
import tm.ugur.services.redis.RedisBusService;

import java.util.*;

@Component
public class BusDestinationDirectionSide {

    private final RouteService routeService;
    private final RedisBusService redisBusService;
    private final GeometryFactory factory;

    private static final Logger logger = LoggerFactory.getLogger(BusDestinationDirectionSide.class);

    @Autowired
    public BusDestinationDirectionSide(RouteService routeService, RedisBusService redisBusService,
                                       GeometryFactory factory) {
        this.routeService = routeService;
        this.redisBusService = redisBusService;
        this.factory = factory;
    }

    @Transactional
    public List<BusDTO> define(List<BusDTO> buses){


        buses.parallelStream().forEach(bus -> {

            Optional<Route> route = routeService.findByNumberInitRouteStops(bus.getNumber());


            if (route.isPresent()) {
                List<StartRouteStop> startRouteStops = route.get().getStartRouteStops();

                if (!startRouteStops.isEmpty()) {
                    if (busOnRoute(route.get(), bus)) {
                        bus.setStatus(true);

                        List<Stop> stops = startRouteStops.stream()
                                .sorted(Comparator.comparing(StartRouteStop::getIndex))
                                .map(StartRouteStop::getStop).toList();


                        Point pointA = stops.getFirst().getLocation();
                        Point pointB = stops.getLast().getLocation();
                        PointDTO pointBus = bus.getLocation();


                        boolean distanceABus = calculateDistance(pointA.getX(), pointA.getY(), pointBus.getLat(), pointBus.getLng());
                        boolean distanceBBus = calculateDistance(pointB.getX(), pointB.getY(), pointBus.getLat(), pointBus.getLng());

                        if (distanceABus)
                            bus.setSide("front");

                        if (distanceBBus)
                            bus.setSide("back");


                        if (Objects.isNull(bus.getSide()) || bus.getSide().isBlank()) {
                            List<BusDTO> busList = redisBusService.getBuses(String.valueOf(bus.getNumber()));
                            Optional<BusDTO> prevBus = busList.stream().filter(b -> b.getCarNumber().equals(bus.getCarNumber())).findAny();
                            if (prevBus.isPresent()) {
                                PointDTO prevPointBus = prevBus.get().getLocation();
                                bus.setSide(getSide(pointA, pointBus, prevPointBus));
                            }

                        }

                    }
                }
            }
        });

        return buses;
    }

    private boolean busOnRoute(Route route, BusDTO bus){
        if(checkCoordinate(route.getFrontLine(), bus))
            return true;
        if(checkCoordinate(route.getBackLine(), bus))
            return true;

        return false;
    }

    private boolean checkCoordinate(LineString line, BusDTO bus){
        for(Coordinate coordinate : line.getCoordinates()){
            if (calculateDistance(coordinate.getX(), coordinate.getY(),
                    bus.getLocation().getLat(), bus.getLocation().getLng())){
                return true;
            }
        }
        return false;
    }

    private boolean calculateDistance(double pointX, double pointY, double busX, double busY){
        double distance = Math.sqrt(Math.pow(busX - pointX, 2) + Math.pow(busY - pointY, 2));
        return distance < 0.002;
    }

    private String getSide(Point a, PointDTO current, PointDTO prev){

        double scalarProductA = calculateDistanceSide(a.getX(), a.getY(), current.getLat(), current.getLng());
        double scalarProductB = calculateDistanceSide(a.getX(), a.getY(), prev.getLat(), prev.getLng());


        String destination;
        if (scalarProductA > scalarProductB) {
            destination = "front";
        } else {
            destination = "back";
        }

        return destination;
    }

    private double calculateDistanceSide(double pointX, double pointY, double busX, double busY){
        return Math.sqrt(Math.pow(busX - pointX, 2) + Math.pow(busY - pointY, 2));
    }

}
