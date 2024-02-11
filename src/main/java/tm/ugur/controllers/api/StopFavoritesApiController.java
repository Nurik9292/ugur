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
import tm.ugur.models.Stop;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.StopService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites/stops")
public class StopFavoritesApiController {

    private final StopService stopService;

    @Autowired
    public StopFavoritesApiController(StopService stopService) {
        this.stopService = stopService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, String>> addOrRemoveStop(@PathVariable("id") Long id) {
        Client client = getAuthClient();
        String message = "Successfully removed from favorites";

        Optional<Stop> stopFavorites = stopService.findByClientsAndId(client, id);
        List<Stop> stops = client.getStops();
        if (stopFavorites.isEmpty()) {
            stops.add(stopService.findOne(id));
            message = "Successfully added from favorites";
        } else {
            stops.remove(stopService.findOne(id));
        }

        client.setStops(stops);

        return ResponseEntity.ok(Map.of("message", message));
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}
