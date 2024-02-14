package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.services.admin.RouteService;

import java.util.List;

@Component
@Order(8)
public class RouteStartIndexingSeed implements CommandLineRunner {

    private final RouteService routeService;

    @Autowired
    public RouteStartIndexingSeed(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Route> routes = routeService.findAll();

        for(Route route : routes){
            List<StartRouteStop> startRouteStops = route.getStartRouteStops();

        }
    }
}
