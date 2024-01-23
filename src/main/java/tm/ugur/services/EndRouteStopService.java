package tm.ugur.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.Stop;
import tm.ugur.repo.EndRouteStopRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
public class EndRouteStopService {

    private static int count = 1;

    private final EndRouteStopRepository endRouteStopRepository;

    @Autowired
    public EndRouteStopService(EndRouteStopRepository endRouteStopRepository) {
        this.endRouteStopRepository = endRouteStopRepository;
    }


    @Transactional
    public void updateIndexs(String ids, Route route) {
        Map<Long, Stop> stopMap = new HashMap<>();  // Map for efficient stop lookup
        route.getStartStops().forEach(stop -> stopMap.put(stop.getId(), stop));

        AtomicInteger count = new AtomicInteger(1);
        for (String idString : ids.split(",")) {
            int id = Integer.parseInt(idString);
            Stop stop = stopMap.get(id);
            if (stop != null) {
                List<EndRouteStop> endRouteStops = this.endRouteStopRepository.findByStopAndRoute(stop, route);
                endRouteStops.forEach(endRouteStop -> {
                    endRouteStop.setIndex(count.getAndIncrement());
                    this.endRouteStopRepository.save(endRouteStop);
                });
            }
        }
    }
}
