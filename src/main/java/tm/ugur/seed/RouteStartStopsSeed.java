package tm.ugur.seed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import tm.ugur.models.Route;
import tm.ugur.models.StartRouteStop;
import tm.ugur.services.RouteService;
import tm.ugur.services.StartRouteStopService;
import tm.ugur.services.StopService;

import java.util.List;
import java.util.Map;

@Component
public class RouteStartStopsSeed implements CommandLineRunner {

    private final RouteService routeService;
    private final StopService stopService;
    private final StartRouteStopService startRouteStopService;

    @Autowired
    public RouteStartStopsSeed(RouteService routeService, StopService stopService, StartRouteStopService startRouteStopService) {
        this.routeService = routeService;
        this.stopService = stopService;
        this.startRouteStopService = startRouteStopService;
    }

    @Override
    public void run(String... args) throws Exception {
//        ClassPathResource resource = new ClassPathResource("route_stops.json");
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Map<String, Integer>> startRouteStops = mapper.readValue(resource.getFile(), Map.class);
//        System.out.println(startRouteStops);
//        List<Route> routes = routeService.findAll();
//
//        for (Route route : routes){
//            Map<String, Integer> stops = startRouteStops.get(route.getName());
//            for (Map.Entry<String, Integer> entry : stops.entrySet()){
//                    this.startRouteStopService.store(
//                            new StartRouteStop(route, this.stopService.findStopByName(entry.getKey()).get(), entry.getValue()));
//            }
//        }

    }
}
