package tm.ugur.services.data_bus.import_data;

import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.services.admin.RouteService;
import tm.ugur.services.redis.RedisRouteService;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class BusDestinationDirectionSide {

    private final RouteService routeService;
    private final RedisRouteService redisRouteService;


    @Autowired
    public BusDestinationDirectionSide(RouteService routeService, RedisRouteService redisRouteService) {
        this.routeService = routeService;
        this.redisRouteService = redisRouteService;
    }

    @Transactional
    public List<BusDTO> define(List<BusDTO> buses){
        List<Route> routes = getRouteCache();

        Map<Integer, Route> routeMap = routes.stream()
                .collect(Collectors.toMap(Route::getNumber, route -> route));

        routes.sort(Comparator.comparing(Route::getNumber));

        buses.forEach(bus -> {
            Route route = routeMap.get(bus.getNumber());

            if (route != null) {
                List<StartRouteStop> startRouteStops = route.getStartRouteStops();

                    startRouteStops.sort(Comparator.comparing(StartRouteStop::getIndex));
                    System.out.println(route.getName() + " " + route.getId());

                    if(!startRouteStops.isEmpty()) {
                        Point pointA = startRouteStops.getFirst().getStop().getLocation();
                        Point pointB = startRouteStops.getLast().getStop().getLocation();

                        double azimuthAB = Math.atan2(pointA.getY() - pointB.getY(), pointA.getX() - pointB.getX());
                        azimuthAB = Math.toDegrees(azimuthAB);

                        double directionDiff = Math.abs(azimuthAB - Integer.parseInt(bus.getDir()));

                        bus.setSide(directionDiff <= 90 ? "back" : "front");
                    }
                }
        });
        System.out.println(buses);
        return buses;
    }

    private List<Route> getRouteCache() {
//        List<Route> routes = redisRouteService.getRoutes();
//        return  Objects.nonNull(routes) ? routes : routeService.findAll();
        return routeService.findAll();
    }
}
