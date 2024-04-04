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


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Component
public class BusIndexing {

    private final RouteService routeService;
    private final StartRouteStopService startRouteStopService;
    private final EndRouteStopService endRouteStopService;

    private final static Logger logger = LoggerFactory.getLogger(BusIndexing.class);

    @Autowired
    public BusIndexing(RouteService routeService,
                       StartRouteStopService startRouteStopService,
                       EndRouteStopService endRouteStopService) {
        this.routeService = routeService;
        this.startRouteStopService = startRouteStopService;
        this.endRouteStopService = endRouteStopService;
    }


    @Transactional
    public List<BusDTO> indexing(List<BusDTO> buses) {

        buses.parallelStream().forEach(bus -> {
            PointDTO point = bus.getLocation();

            Optional<Route> route = routeService.findByNumberInitStops(bus.getNumber());

            Stop stop = Objects.nonNull(bus.getSide()) && bus.getSide().equals("front") ?
                    findNearestStopIndex(route.get().getStartStops(), point) : findNearestStopIndex(route.get().getEndStops(), point) ;

            if (Objects.isNull(bus.getSide())) {
                return;
            }


            if (Objects.nonNull(bus.getSide()) && bus.getSide().equals("front")) {
                processStopsForSide(stop, route, bus, this::processStartRouteStop);
            } else if (bus.getSide().equals("back")) {
                processStopsForSide(stop, route, bus, this::processEndRouteStop);
            }
        });


        return buses;
    }


    private  Stop findNearestStopIndex(List<Stop> stops, PointDTO point) {
        Stop nearestStopIndex = null;
        double minDistance = Double.MAX_VALUE;

        for(Stop stop : stops){
            double distance = calculateDistance(stop.getLocation().getX(), stop.getLocation().getY(), point.getLat(),point.getLng());
            if (distance < minDistance) {
                minDistance = distance;
                nearestStopIndex = stop;
            }
        }
        return nearestStopIndex;
    }
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }



    private void processStopsForSide(Stop stop, Optional<Route> route, BusDTO bus,
                                     BiConsumer<Optional<?>, BusDTO> routeStopProcessor) {
        if (route.isPresent()) {

            Optional<StartRouteStop> routeStop = startRouteStopService.findByStopAndRoute(stop, route.get());

            if(routeStop.isPresent())
                routeStopProcessor.accept(routeStop, bus);
            else
                routeStopProcessor.accept(endRouteStopService.findByStopAdnRoute(stop, route.get()), bus);

        }
    }

    private void processStartRouteStop(Optional<?> routeStop, BusDTO bus) {
        processRouteStop(routeStop, bus, index -> index % 2 == 0);
    }

    private void processEndRouteStop(Optional<?> routeStop, BusDTO bus) {
        processRouteStop(routeStop, bus, index -> index % 2 != 0);
    }

    private void processRouteStop(Optional<?> routeStop, BusDTO bus, Predicate<Integer> indexCondition) {
        routeStop.ifPresent(stop -> {
            int index = stop instanceof StartRouteStop ?
                    ((StartRouteStop) stop).getIndex() : ((EndRouteStop) stop).getIndex();
            if (indexCondition.test(index)) {
                bus.setIndex(index);
            }
        });
    }
}
