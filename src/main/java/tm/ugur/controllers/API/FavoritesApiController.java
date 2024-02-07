package tm.ugur.controllers.API;

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
@RequestMapping("favorites")
public class FavoritesApiController {

    private final RouteService routeService;
    private final ClientService clientService;

    @Autowired
    public FavoritesApiController(RouteService routeService, ClientService clientService) {
        this.routeService = routeService;
        this.clientService = clientService;
    }

    @PostMapping("/{id}/routes")
    public ResponseEntity<Map<String, String>> addOrRemove(@PathVariable("id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        Client client = clientDetails.getClient();

        Optional<Route> routeFavorite = this.routeService.findRoutesByClient(
                client, id);

        if(routeFavorite.isEmpty()){
            List<Route> routes = client.getRoutes();
            routes.add(this.routeService.findOne(id).get());
            client.setRoutes(routes);
        }else{
            List<Route> routes = client.getRoutes();
            routes.remove(this.routeService.findOne(id).get());
            client.setRoutes(routes);
        }

        return ResponseEntity.ok(Map.of("message", "Successfully removed from favorites"));
    }


}
