package tm.ugur.services.admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.repo.EndRouteStopRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EndRouteStopService {

    @PersistenceContext
    private EntityManager entityManager;

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
    public void updateIndexes(Route route) {

        AtomicInteger count = new AtomicInteger(1);

        List<EndRouteStop> endRouteStops = route.getEndStops().stream()
                .map(stop -> endRouteStopRepository.findByStopAndRoute(stop, route)
                        .get())
                .peek(endRouteStop -> endRouteStop.setIndex(count.getAndAdd(2)))
                .toList();

        updateInBatch(endRouteStops);
    }

    @Transactional
    public void updateInBatch(List<EndRouteStop> endRouteStops) {
        int batchSize = 50;

        for (int i = 0; i < endRouteStops.size(); i++) {
            entityManager.persist(endRouteStops.get(i));

            if (i % batchSize == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }
}
