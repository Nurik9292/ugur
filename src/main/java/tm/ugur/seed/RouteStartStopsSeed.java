package tm.ugur.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tm.ugur.models.Route;
import tm.ugur.services.RouteService;

import java.util.List;
import java.util.Map;

@Component
public class RouteStartStopsSeed implements CommandLineRunner {

    private final RouteService routeService;

    @Autowired
    public RouteStartStopsSeed(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
