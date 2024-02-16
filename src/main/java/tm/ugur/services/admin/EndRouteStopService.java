package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;
import tm.ugur.repo.EndRouteStopRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EndRouteStopService {


    private final EndRouteStopRepository endRouteStopRepository;

    @Autowired
    public EndRouteStopService(EndRouteStopRepository endRouteStopRepository) {
        this.endRouteStopRepository = endRouteStopRepository;
    }

    public List<EndRouteStop> findByRoute(Route route){
        return endRouteStopRepository.findByRouteOrderByIndex(route);
    }

    public Optional<EndRouteStop> findByStopAdnRoute(Stop stop, Route route){
        return endRouteStopRepository.findByStopAndRoute(stop, route);
    }

    public boolean hasRoute(Route route){
        return this.endRouteStopRepository.existsByRoute(route);
    }

    @Transactional
    public void store(EndRouteStop endRouteStop){
        this.endRouteStopRepository.save(endRouteStop);
    }


    @Transactional
    public void updateIndexes(String ids, Route route) {
        Map<Long, Stop> stopMap = route.getStartStops().stream()
                .collect(Collectors.toMap(Stop::getId, Function.identity()));

        AtomicInteger count = new AtomicInteger(1);
        Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .map(stopMap::get)
                .filter(Objects::nonNull)
                .flatMap(stop -> this.endRouteStopRepository.findByStopAndRoute(stop, route).stream())
                .forEach(startRouteStop -> {
                    startRouteStop.setIndex(count.getAndAdd(2));
                    this.endRouteStopRepository.save(startRouteStop);
                });
    }
}
