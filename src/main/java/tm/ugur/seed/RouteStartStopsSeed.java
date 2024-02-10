package tm.ugur.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.services.RouteService;
import tm.ugur.services.StartRouteStopService;
import tm.ugur.services.StopService;

import java.util.*;

@Component
@Order(4)
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
        Map<String, List<Map<String, Integer>>> parsedObject = mapper.readValue(resource.getInputStream(), LinkedHashMap.class);

        List<Route> routes = routeService.findAll();

        if (!this.startRouteStopService.hasRoute(routes.getFirst())) {
            for (Route route : routes) {
                List<Map<String, Integer>> list = parsedObject.get(route.getName());
                if (list != null) {
                    for (Map<String, Integer> map : list) {
                        for (Map.Entry<String, Integer> entry : map.entrySet()) {
                            String key = entry.getKey();
                            Integer value = entry.getValue();


                            this.startRouteStopService.store(new StartRouteStop(
                                    route, this.stopService.findStopByName(key).get(), value)
                            );
                        }
                    }
                }
            }
        }
    }
}
