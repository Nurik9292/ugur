package tm.ugur.services.data_bus;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.geo.PointDTO;
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

    private static final int EARTH_RADIUS = 6371;

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

                    if(!startRouteStops.isEmpty()) {

                        Point pointA = startRouteStops.getFirst().getStop().getLocation();
                        Point pointB = startRouteStops.getLast().getStop().getLocation();
                        PointDTO pointBus = bus.getLocation();

                        double azimuthToA = calculateAzimuth(pointBus.getLat(), pointBus.getLng(), pointA.getX(), pointA.getY());
                        double azimuthToB = calculateAzimuth(pointBus.getLat(), pointBus.getLng(), pointB.getX(), pointB.getY());

                        bus.setSide(determineDirection(Double.parseDouble(bus.getDir()), azimuthToA, azimuthToB));
                    }

                }
        });
        return buses;
    }

    public static double calculateAzimuth(double lat1, double lon1, double lat2, double lon2) {
        double dLon = lon2 - lon1;
        double y = Math.sin(Math.toRadians(dLon)) * Math.cos(Math.toRadians(lat2));
        double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) -
                Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(dLon));
        double azimuth = Math.toDegrees(Math.atan2(y, x));
        return (azimuth + 360) % 360; // Приводим значение азимута к диапазону [0, 360)
    }

    // Определение направления движения автобуса
    public static String determineDirection(double currentDirection, double azimuthToA, double azimuthToB) {
        // Проверяем, к какой точке ближе текущее направление движения
        double diffToA = Math.abs(azimuthToA - currentDirection);
        double diffToB = Math.abs(azimuthToB - currentDirection);

        if (diffToA < diffToB) {
            return "front";
        } else {
            return "back";
        }
    }


    private List<Route> getRouteCache() {
//        List<Route> routes = redisRouteService.getRoutes();
//        return  Objects.nonNull(routes) ? routes : routeService.findAll();
        return routeService.findAll();
    }
}
