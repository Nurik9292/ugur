package tm.ugur.services.api;

import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.StopService;
import tm.ugur.services.redis.RedisBusService;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BusTimeService {

    private final StopService stopService;
    private final RedisBusService redisBusService;

    private static final double EARTH_RADIUS = 6371.0;

    public BusTimeService(StopService stopService, RedisBusService redisBusService) {
        this.stopService = stopService;
        this.redisBusService = redisBusService;
    }

    public Map<Integer, Double> getBusTime(Long id){
        Stop stop = stopService.findOneInit(id);

        if (Objects.isNull(stop)) {
            return Collections.emptyMap();
        }
            int index = stop.getEndRouteStops().isEmpty() ? stop.getStartRouteStops().getFirst().getIndex() :
                    stop.getEndRouteStops().getFirst().getIndex();
            List<Route> routes = stop.getEndRouteStops().isEmpty() ? stop.getStartRoutes() : stop.getEndRoutes();

            Map<Integer, BusDTO> nearestBuses = routes.stream()
                    .map(Route::getNumber)
                    .parallel()
                    .flatMap(number -> redisBusService.getBuses(String.valueOf(number)).stream())
                    .filter(busDTO -> busDTO.getIndex() != null && Math.floorMod(busDTO.getIndex(), 2) == Math.floorMod(index, 2))
                    .filter(busDTO -> busDTO.getIndex() > index)
                    .sorted(Comparator.comparing(BusDTO::getIndex))
                    .collect(Collectors.toMap(BusDTO::getNumber, busDTO -> busDTO, (a, b) -> a));


            Map<Integer, Double> times = new HashMap<>();

           nearestBuses.forEach((number, bus) -> {
                times.put(number, calculateArrivalTime(
                        bus.getLocation().getLat(), bus.getLocation().getLng(),
                        stop.getLocation().getX(), stop.getLocation().getY(), Double.parseDouble(bus.getSpeed())));
           });

           return times;
    }


    public  double calculateArrivalTime(double busLat, double busLng, double stopLat, double stopLng, double speed) {
        double distance = calculateDistance(busLat, busLng, stopLat, stopLng);
        double timeInHours = distance / speed == 0 ? 20 : speed;
        return TimeUnit.HOURS.toMinutes((long) timeInHours);
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }


}

