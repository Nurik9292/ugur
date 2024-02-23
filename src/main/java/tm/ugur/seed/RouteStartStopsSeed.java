package tm.ugur.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.models.Stop;
import tm.ugur.services.admin.RouteService;
import tm.ugur.services.admin.StartRouteStopService;
import tm.ugur.services.admin.StopService;

import java.util.*;

@Component
@Order(4)
@ConditionalOnProperty(name = "db.init.enabled", havingValue = "true", matchIfMissing = false)
public class RouteStartStopsSeed implements CommandLineRunner {

    private final RouteService routeService;
    private final StopService stopService;
    private final StartRouteStopService startRouteStopService;

    @Autowired
    public RouteStartStopsSeed(RouteService routeService,
                               StopService stopService,
                               StartRouteStopService startRouteStopService) {
        this.routeService = routeService;
        this.stopService = stopService;
        this.startRouteStopService = startRouteStopService;
    }

    @Override
    @DependsOn("StopSeed")
    public void run(String... args) throws Exception {
        ClassPathResource resource = new ClassPathResource("route_stops.json");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<Map<String, Map<String, Integer>>>> importData = mapper.readValue(resource.getInputStream(), LinkedHashMap.class);
        List<Stop> stops = stopService.findAll();
        List<Route> routes = routeService.findAll();

        if (!this.startRouteStopService.hasRoute(routes.getFirst())) {
            for (Map.Entry<String, List<Map<String, Map<String, Integer>>>> entry : importData.entrySet()) {
                String stopName = entry.getKey();
                List<Map<String, Map<String, Integer>>> routesData = entry.getValue();
                Stop stop = stops.stream()
                        .filter(s -> s.getName().equals(stopName))
                        .findFirst()
                        .orElse(null);
                if (stop != null) {
                    for (Map<String, Map<String, Integer>> routeData : routesData) {
                        for (Map.Entry<String, Map<String, Integer>> routeEntry : routeData.entrySet()) {
                            String routeName = routeEntry.getKey();
                            Route route = routes.stream()
                                    .filter(r -> r.getName().equals(routeName) && (int)r.getNumber() == routeEntry.getValue().get("number"))
                                    .findFirst()
                                    .orElse(null);
                            if (route != null) {
                                startRouteStopService.store(new StartRouteStop(
                                        route, stop, routeEntry.getValue().get("index")
                                ));
                            }
                        }
                    }
                }
            }
        }
    }
}
