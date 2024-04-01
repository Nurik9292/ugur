package tm.ugur.services.api;

import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.StopService;
import tm.ugur.services.redis.RedisBusService;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BusTimeService {

    private final StopService stopService;
    private final RedisBusService redisBusService;


    public BusTimeService(StopService stopService, RedisBusService redisBusService) {
        this.stopService = stopService;
        this.redisBusService = redisBusService;
    }

    public Map<Integer, String> getBusTime(Long id){
        Stop stop = stopService.findOneInit(id);

        if (Objects.isNull(stop)) {
            return Collections.emptyMap();
        }

        Set<Route> routes = stop.getStartRouteStops().stream().map(StartRouteStop::getRoute).collect(Collectors.toSet());
        stop.getEndRouteStops().forEach(endRouteStop -> routes.add(endRouteStop.getRoute()));

        Map<Integer, Integer> indexes = new HashMap<>();

        stop.getStartRouteStops().forEach(stopRoute -> indexes.put(stopRoute.getRoute().getNumber(), stopRoute.getIndex()));
        stop.getEndRouteStops().forEach(stopRoute -> indexes.put(stopRoute.getRoute().getNumber(), stopRoute.getIndex()));


        Map<Integer, BusDTO> nearestBuses = filterBus(indexes);

        Map<Integer, String> times = new HashMap<>();

           nearestBuses.forEach((number, bus) -> {
                times.put(number,  new DecimalFormat("#.##")
                        .format(calculateArrivalTime(calculateDistance(
                                        stop.getLocation().getX(), stop.getLocation().getY(),
                                        bus.getLocation().getLat(), bus.getLocation().getLng()),
                                Double.parseDouble(bus.getSpeed()))));
           });

           return times;
    }

    private Map<Integer, BusDTO> filterBus(Map<Integer, Integer> indexes){
        Map<Integer, BusDTO> nearestBuses = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : indexes.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();

            List<BusDTO> busDTOList = redisBusService.getBuses(String.valueOf(key));
            List<BusDTO> filterBus = new ArrayList<>();

            for (BusDTO busDTO : busDTOList) {
                if (value % 2 == busDTO.getIndex() % 2) {
                    filterBus.add(busDTO);
                }
            }

            filterBus.sort(Comparator.comparing(BusDTO::getIndex).reversed());

            for (BusDTO busDTO : filterBus) {
                if (value > busDTO.getIndex()) {
                    nearestBuses.put(key, busDTO);
                    break;
                }
            }
        }
        return nearestBuses;
    }

    public Double getBusOneTime(Long id, String number, String carNumber){
        Stop stop = stopService.findOneInit(id);

        if (Objects.isNull(stop)) {
            return 0.0;
        }

        BusDTO bus =  redisBusService.getBuses(number)
                .stream().filter(b -> b.getCarNumber().equals(carNumber)).findAny().orElseThrow();

        double distance = calculateDistance(bus.getLocation().getLat(),
                bus.getLocation().getLng(),
                stop.getLocation().getX(),
                stop.getLocation().getY());

        return calculateArrivalTime(distance, Double.parseDouble(bus.getSpeed()));

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

    private static double calculateArrivalTime(double distance, double busSpeed) {
        double timeInHours = distance / busSpeed;
        return timeInHours * 60;
    }


}

