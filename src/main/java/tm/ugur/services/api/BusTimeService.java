package tm.ugur.services.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.StopService;
import tm.ugur.services.redis.RedisBusService;
import tm.ugur.util.Distance;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusTimeService {

    private final StopService stopService;
    private final RedisBusService redisBusService;
    private final Distance distance;

    private static final Logger logger = LoggerFactory.getLogger(BusTimeService.class);


    public BusTimeService(StopService stopService, RedisBusService redisBusService, Distance distance) {
        this.stopService = stopService;
        this.redisBusService = redisBusService;
        this.distance = distance;
    }

    public Map<Integer, String> getBusTime(Long id){
        Stop stop = stopService.findOneInit(id);

        if (Objects.isNull(stop)) {
            return Collections.emptyMap();
        }
        Map<Integer, BusDTO> nearestBuses = filterBus(getRouteIndexes(stop));

        logger.info(nearestBuses.toString());

        return nearestBuses.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> formatTime(stop, entry.getValue())));
    }

    private Map<Integer, BusDTO> filterBus(Map<Integer, Integer> indexes){
        Map<Integer, BusDTO> nearestBuses = new HashMap<>();
        indexes.forEach((number, value) -> {
            if (value != null) {
                List<BusDTO> busDTOList = Optional.ofNullable(redisBusService.getBuses(String.valueOf(number)))
                        .orElse(Collections.emptyList());

                List<BusDTO> filterBus = busDTOList.stream()
                        .filter(busDTO -> busDTO.getIndex() != null && value % 2 == busDTO.getIndex() % 2)
                        .sorted(Comparator.comparing(BusDTO::getIndex).reversed())
                        .toList();

              filterBus.forEach(bus -> {
                  if(value > bus.getIndex())
                      nearestBuses.put(number, bus);
              });
            }
        });

        return nearestBuses;
    }

    private String formatTime(Stop stop, BusDTO bus) {
        double time = calculateArrivalTime(distance.calculateRadius(
                        stop.getLocation().getX(), stop.getLocation().getY(),
                        bus.getLocation().getLat(), bus.getLocation().getLng()),
                Double.parseDouble(bus.getSpeed()));

        return Double.isInfinite(time) ? "0.0" : new DecimalFormat("#.##").format(time);
    }


    private Map<Integer, Integer> getRouteIndexes(Stop stop) {
        Map<Integer, Integer> indexes = new HashMap<>();
        stop.getStartRouteStops().forEach(stopRoute -> indexes.put(stopRoute.getRoute().getNumber(), stopRoute.getIndex()));
        stop.getEndRouteStops().forEach(stopRoute -> indexes.put(stopRoute.getRoute().getNumber(), stopRoute.getIndex()));
        return indexes;
    }



    public Double getBusOneTime(Long id, String number, String carNumber){
        Stop stop = stopService.findOneInit(id);

        if (Objects.isNull(stop)) {
            return 0.0;
        }

        BusDTO bus =  redisBusService.getBuses(number)
                .stream().filter(b -> b.getCarNumber().equals(carNumber)).findAny().orElseThrow();

        double distance = this.distance.calculateRadius(bus.getLocation().getLat(),
                bus.getLocation().getLng(),
                stop.getLocation().getX(),
                stop.getLocation().getY());

        return calculateArrivalTime(distance, Double.parseDouble(bus.getSpeed()));

    }



    private static double calculateArrivalTime(double distance, double busSpeed) {
        busSpeed = busSpeed < 10.0 ? 30.0 : busSpeed;
        double timeInHours = distance / busSpeed;
        return timeInHours * 60;
    }

}

