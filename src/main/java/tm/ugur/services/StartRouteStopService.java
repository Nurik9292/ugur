package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.repo.StartRouteStopRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class StartRouteStopService {

    private final StartRouteStopRepository startRouteStopRepository;


    @Autowired
    public StartRouteStopService(StartRouteStopRepository startRouteStopRepository) {
        this.startRouteStopRepository = startRouteStopRepository;
    }

    public List<StartRouteStop> findByRoute(Route route){
        return startRouteStopRepository.findByRouteOrderByIndex(route);
    }

    public Boolean hasRoute(Route route){
        return startRouteStopRepository.existsByRoute(route);
    }

    @Transactional
    public void store(StartRouteStop  startRouteStop){
        startRouteStopRepository.save(startRouteStop);
    }

    @Transactional
    public void updateIndexes(String ids, Route route) {
        Map<Long, Stop> stopMap = route.getStartStops().stream()
                .collect(Collectors.toMap(Stop::getId, Function.identity()));

        AtomicInteger count = new AtomicInteger(2);
        Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .map(stopMap::get)
                .filter(Objects::nonNull)
                .flatMap(stop -> this.startRouteStopRepository.findByStopAndRoute(stop, route).stream())
                .forEach(startRouteStop -> {
                    startRouteStop.setIndex(count.getAndAdd(2));
                    this.startRouteStopRepository.save(startRouteStop);
                });
    }
}
