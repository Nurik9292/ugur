package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.Client;
import tm.ugur.models.Route;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.api.RouteApiService;
import tm.ugur.util.errors.route.RouteErrorResponse;
import tm.ugur.util.errors.route.RouteNotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites/routes")
public class RouteFavoritesApiController {

    private final RouteApiService routeService;


    @Autowired
    public RouteFavoritesApiController(RouteApiService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, String>> addOrRemoveRoute(@PathVariable("id") Long id) {
        Client client = getAuthClient();
        String message = "Successfully removed from favorites";

        Route route = this.routeService.getRoute(id).orElseThrow(RouteNotFoundException::new);
        List<Client> clients = route.getClients();

        if (!clients.contains(client)) {
            message = "Successfully added from favorites";
            clients.add(client);
        } else {
            clients.removeIf(c -> c.getId() == client.getId());
        }

        this.routeService.store(route);

        return ResponseEntity.ok(Map.of("message", message));
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }

    @ExceptionHandler
    private ResponseEntity<RouteErrorResponse> handleException(RouteNotFoundException e){
        RouteErrorResponse errorResponse = new RouteErrorResponse(
                "Route with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}