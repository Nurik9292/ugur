package tm.ugur.services.data_bus;

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

@Component
public class BusDestinationDirectionSide {

    private final RouteService routeService;
    private final RedisRouteService redisRouteService;
    private final StartRouteStopService startRouteStopService;
    private final Lock lock;

    private static final int EARTH_RADIUS = 6371;
    private static final Logger logger = LoggerFactory.getLogger(BusDestinationDirectionSide.class);

    @Autowired
    public BusDestinationDirectionSide(RouteService routeService,
                                       RedisRouteService redisRouteService,
                                       StartRouteStopService startRouteStopService,
                                       Lock lock) {
        this.routeService = routeService;
        this.redisRouteService = redisRouteService;
        this.startRouteStopService = startRouteStopService;
        this.lock = lock;
    }

    @Transactional
    public List<BusDTO> define(List<BusDTO> buses){

        long start = System.currentTimeMillis();
        logger.info("Start: side");
        buses.parallelStream().forEach(bus -> {

            Optional<Route> route = routeService.findByNumber(bus.getNumber());

            if (route.isPresent()) {
                List<StartRouteStop> startRouteStops = startRouteStopService.findByRoute(route.get());

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
        long end = System.currentTimeMillis();
        logger.info("end side");
        logger.info(String.valueOf(end - start));

        return buses;
    }

    private double calculateAzimuth(double lat1, double lon1, double lat2, double lon2) {
        double dLon = lon2 - lon1;
        double y = Math.sin(Math.toRadians(dLon)) * Math.cos(Math.toRadians(lat2));
        double x = Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) -
                Math.sin(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(dLon));
        double azimuth = Math.toDegrees(Math.atan2(y, x));
        return (azimuth + 360) % 360; // Приводим значение азимута к диапазону [0, 360)
    }

    // Определение направления движения автобуса
    private  String determineDirection(double currentDirection, double azimuthToA, double azimuthToB) {
        // Проверяем, к какой точке ближе текущее направление движения
        double diffToA = Math.abs(azimuthToA - currentDirection);
        double diffToB = Math.abs(azimuthToB - currentDirection);

        if (diffToA < diffToB) {
            return "front";
        } else {
            return "back";
        }
    }



}
