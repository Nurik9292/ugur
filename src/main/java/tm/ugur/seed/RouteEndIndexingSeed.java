package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.services.admin.EndRouteStopService;
import tm.ugur.services.admin.RouteService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(9)
@ConditionalOnProperty(name = "db.init.enabled", havingValue = "true", matchIfMissing = false)
public class RouteEndIndexingSeed implements CommandLineRunner {

    private final EndRouteStopService endRouteStopService;
    private final RouteService routeService;

    @Autowired
    public RouteEndIndexingSeed(EndRouteStopService endRouteStopService, RouteService routeService) {
        this.endRouteStopService = endRouteStopService;
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(!routeService.findByNumber(13).isPresent()) {
            Set<Route> routes = new HashSet<>(routeService.findAll());

            routes.forEach(route -> {
                AtomicInteger count = new AtomicInteger(1);
                List<EndRouteStop> endRouteStops = endRouteStopService.findByRoute(route);
                endRouteStops.forEach(endRouteStop -> {
                    endRouteStop.setIndex(count.get());
                    endRouteStopService.store(endRouteStop);
                    count.addAndGet(2);
                });
            });
        }
    }
}
