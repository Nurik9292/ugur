package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.services.RouteService;
import tm.ugur.services.StartRouteStopService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(7)
public class IndexingStartStops implements CommandLineRunner {

    private final RouteService routeService;
    private final StartRouteStopService startRouteStopService;

    @Autowired
    public IndexingStartStops(RouteService routeService, StartRouteStopService startRouteStopService) {
        this.routeService = routeService;
        this.startRouteStopService = startRouteStopService;
    }

    @Override
    public void run(String... args) throws Exception {

        Set<Route> routes = new HashSet<>(routeService.findAll());

        routes.forEach(route -> {
            AtomicInteger count = new AtomicInteger(2);
            List<StartRouteStop> startRouteStops = startRouteStopService.findByRoute(route);
            startRouteStops.forEach(startRouteStop -> {
                startRouteStop.setIndex(count.get());
                startRouteStopService.store(startRouteStop);
                count.addAndGet(2);
            });
        });
    }
}
