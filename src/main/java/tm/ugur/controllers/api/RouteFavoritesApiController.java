package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.Client;
import tm.ugur.models.Route;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.ClientService;
import tm.ugur.services.RouteService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites/routes")
public class RouteFavoritesApiController {

    private final RouteService routeService;
    private final ClientService clientService;


    @Autowired
    public RouteFavoritesApiController(RouteService routeService, ClientService clientService) {
        this.routeService = routeService;
        this.clientService = clientService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, String>> addOrRemoveRoute(@PathVariable("id") Long id) {
        Client client = getAuthClient();
        String message = "Successfully removed from favorites";

        Optional<Route> routeFavorite = routeService.findRoutesByClient(client, id);

        List<Route> routes = client.getRoutes();
        Route route = this.routeService.findOne(id).get();
        List<Client> clients = route.getClients();

        if (routeFavorite.isEmpty()) {
            message = "Successfully added from favorites";
            clients.add(client);
            routes.add(route);
        } else {
            routes.remove(routeFavorite.get());
            for (int i = 0; i < clients.size(); i++){
                if (clients.get(i).getId() == client.getId()){
                    clients.remove(i);
                }
            }
        }

        route.setClients(clients);
        client.setRoutes(routes);
        this.routeService.store(route);
        this.clientService.store(client);

        return ResponseEntity.ok(Map.of("message", message));
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }


}