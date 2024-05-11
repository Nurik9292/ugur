package tm.ugur.services.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;



    @Autowired
    public StartRouteStopService(StartRouteStopRepository startRouteStopRepository) {
        this.startRouteStopRepository = startRouteStopRepository;
    }

    public List<StartRouteStop> findByRoute(Route route){
        return startRouteStopRepository.findByRouteOrderByIndex(route);
    }

    public List<StartRouteStop> findByStop(Stop stop){
        return startRouteStopRepository.findByStop(stop);
    }

    public Optional<StartRouteStop> findByStopAndRoute(Stop stop, Route route){
        return startRouteStopRepository.findByStopAndRoute(stop, route);
    }

    public Boolean hasRoute(Route route){
        return startRouteStopRepository.existsByRoute(route);
    }

    @Transactional
    public void store(StartRouteStop  startRouteStop){
        startRouteStopRepository.save(startRouteStop);
    }

    @Transactional
    public void updateIndexes(Route route) {
        AtomicInteger count = new AtomicInteger(2);
        List<StartRouteStop> startRouteStops = route.getStartStops().stream()
                .map(stop -> startRouteStopRepository.findByStopAndRoute(stop, route)
                        .get())
                .peek(startRouteStop -> startRouteStop.setIndex(count.getAndAdd(2)))
                .toList();

        updateInBatch(startRouteStops);
    }

    @Transactional
    public void updateInBatch(List<StartRouteStop> startRouteStops) {
        int batchSize = 50;

        for (int i = 0; i < startRouteStops.size(); i++) {
            entityManager.persist(startRouteStops.get(i));

            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
