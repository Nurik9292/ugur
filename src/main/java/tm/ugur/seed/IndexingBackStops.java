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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(8)
@ConditionalOnProperty(name = "db.init.enabled", havingValue = "true", matchIfMissing = false)
public class IndexingBackStops implements CommandLineRunner {

    private final RouteService routeService;
    private final EndRouteStopService endRouteStopService;

    @Autowired
    public IndexingBackStops(RouteService routeService, EndRouteStopService endRouteStopService) {
        this.routeService = routeService;
        this.endRouteStopService = endRouteStopService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Route> routes = routeService.findAll();

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
