package tm.ugur.services.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.repo.StartRouteStopRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional(readOnly = true)
public class StartRouteStopService{

    private final StartRouteStopRepository startRouteStopRepository;


    @Autowired
    public StartRouteStopService(StartRouteStopRepository startRouteStopRepository) {
        this.startRouteStopRepository = startRouteStopRepository;
    }

    public Boolean hasRoute(Route route){
        return  this.startRouteStopRepository.existsByRoute(route);
    }

    @Transactional
    public void store(StartRouteStop startRouteStop){
        this.startRouteStopRepository.save(startRouteStop);
    }

    @Transactional
    public void updateIndexs(String ids, Route route) {
        Map<Long, Stop> stopMap = new HashMap<>();
        route.getStartStops().forEach(stop -> stopMap.put(stop.getId(), stop));

        AtomicInteger count = new AtomicInteger(2);
        for (String idString : ids.split(",")) {
            int id = Integer.parseInt(idString);
            Stop stop = stopMap.get(id);
            if (stop != null) {
                List<StartRouteStop> startRouteStops = this.startRouteStopRepository.findByStopAndRoute(stop, route);
                startRouteStops.forEach(startRouteStop -> {
                    startRouteStop.setIndex(count.getAndAdd(2));
                    this.startRouteStopRepository.save(startRouteStop);
                });
            }
        }
    }
}
