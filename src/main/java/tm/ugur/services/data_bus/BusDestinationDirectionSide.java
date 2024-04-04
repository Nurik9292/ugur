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
import tm.ugur.util.Distance;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BusDestinationDirectionSide {

    private final RouteService routeService;
    private final RedisBusService redisBusService;
    private final Distance distance;


    private static final Logger logger = LoggerFactory.getLogger(BusDestinationDirectionSide.class);

    @Autowired
    public BusDestinationDirectionSide(RouteService routeService,
                                       RedisBusService redisBusService,
                                       Distance distance) {
        this.routeService = routeService;
        this.redisBusService = redisBusService;
        this.distance = distance;
    }

    @Transactional
    public List<BusDTO> defineBusSides(List<BusDTO> buses){

        buses.parallelStream().forEach(bus -> {

            Optional<Route> route = routeService.findByNumberInitRouteStops(bus.getNumber());

            route.ifPresent(r -> {
                List<Stop> sortedStops = getSortedStops(r.getStartRouteStops());

                PointDTO busLocation = bus.getLocation();

                if (isOnRoute(r, busLocation)) {
                    bus.setStatus(true);
                    bus.setSide(determineSide(sortedStops, bus));
                }
            });

        });

        return buses;
    }

    private String determineSide(List<Stop> stops, BusDTO bus) {
        Point firstStop = stops.getFirst().getLocation();
        Point lastStop = stops.getLast().getLocation();
        PointDTO busLocation = bus.getLocation();

        if (distance.calculate(firstStop.getX(), firstStop.getY(), busLocation.getLat(), busLocation.getLng())) {
            return "front";
        } else if (distance.calculate(lastStop.getX(), lastStop.getY(), busLocation.getLat(), busLocation.getLng())) {
            return "back";
        } else {
            Optional<BusDTO> oldBus = redisBusService.getBuses(String.valueOf(bus.getNumber()))
                    .stream().filter(b -> b.getCarNumber().equals(bus.getCarNumber())).findFirst();
            return oldBus.map(BusDTO::getSide).orElse(null);
        }
    }

    private List<Stop> getSortedStops(List<StartRouteStop> startRouteStops) {
        return startRouteStops.stream()
                .sorted(Comparator.comparing(StartRouteStop::getIndex))
                .map(StartRouteStop::getStop)
                .collect(Collectors.toList());
    }


    private boolean isOnRoute(Route route, PointDTO busLocation){
        return checkCoordinate(route.getFrontLine(), busLocation) || checkCoordinate(route.getBackLine(), busLocation);
    }


    private boolean checkCoordinate(LineString line, PointDTO busLocation){
        for(Coordinate coordinate : line.getCoordinates()){
            if (distance.calculate(coordinate.getX(), coordinate.getY(),
                    busLocation.getLat(), busLocation.getLng())){
                return true;
            }
        }
        return false;
    }




}
