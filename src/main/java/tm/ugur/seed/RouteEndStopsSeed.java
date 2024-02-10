package tm.ugur.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tm.ugur.models.EndRouteStop;
import tm.ugur.models.Route;
import tm.ugur.services.EndRouteStopService;
import tm.ugur.services.RouteService;
import tm.ugur.services.StopService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Order(5)
public class RouteEndStopsSeed implements CommandLineRunner {

    private final RouteService routeService;
    private final StopService stopService;
    private final EndRouteStopService endRouteStopService;

    @Autowired
    public RouteEndStopsSeed(RouteService routeService, StopService stopService, EndRouteStopService endRouteStopService) {
        this.routeService = routeService;
        this.stopService = stopService;
        this.endRouteStopService = endRouteStopService;
    }

    @Override
    @DependsOn("StopSeed")
    public void run(String... args) throws Exception {
        ClassPathResource resource = new ClassPathResource("end_route_stops.json");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<Map<String, Integer>>> parsedObject = mapper.readValue(resource.getFile(), LinkedHashMap.class);

        List<Route> routes = routeService.findAll();

        if (!this.endRouteStopService.hasRoute(routes.getFirst())) {
            for (Route route : routes) {
                List<Map<String, Integer>> list = parsedObject.get(route.getName());
                if (list != null) {
                    for (Map<String, Integer> map : list) {
                        for (Map.Entry<String, Integer> entry : map.entrySet()) {
                            String key = entry.getKey();
                            Integer value = entry.getValue();

                            if(!key.startsWith("Зарядная станция")){
                                this.endRouteStopService.store(new EndRouteStop(
                                        route, this.stopService.findStopByName(key).get(), value)
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}
