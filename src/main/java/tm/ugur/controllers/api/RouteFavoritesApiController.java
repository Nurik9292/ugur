package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.ugur.models.Client;
import tm.ugur.models.Route;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.RouteService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("favorites/routes")
public class RouteFavoritesApiController {

    private final RouteService routeService;

    @Autowired
    public RouteFavoritesApiController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, String>> addOrRemoveRoute(@PathVariable("id") Long id) {

        Client client = getAuthClient();
        Optional<Route> routeFavorite = routeService.findRoutesByClient(client, id);
        List<Route> routes = client.getRoutes();

        if (routeFavorite.isEmpty()) {
            routes.add(routeService.findOne(id).get());
        } else {
            routes.remove(routeService.findOne(id).get());
        }
        client.setRoutes(routes);

        return ResponseEntity.ok(Map.of("message", "Successfully removed from favorites"));
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }

}