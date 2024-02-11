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
import tm.ugur.services.ClientService;
import tm.ugur.services.StopService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites/stops")
public class StopFavoritesApiController {

    private final StopService stopService;
    private final ClientService clientService;

    @Autowired
    public StopFavoritesApiController(StopService stopService, ClientService clientService) {
        this.stopService = stopService;
        this.clientService = clientService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, String>> addOrRemoveStop(@PathVariable("id") Long id) {
        Client client = getAuthClient();
        String message = "Successfully removed from favorites";

        Optional<Stop> stopFavorites = stopService.findByClientsAndId(client, id);

        List<Stop> stops = client.getStops();
        Stop stop = stopService.findOne(id);
        List<Client> clients = stop.getClients();

        if (stopFavorites.isEmpty()) {
            stops.add(stop);
            clients.add(client);
            message = "Successfully added from favorites";
        } else {
            stops.remove(stop);
            for (int i = 0; i < clients.size(); i++){
                if(clients.get(i).getId() == client.getId()){
                    clients.remove(i);
                }
            }
        }
        System.out.println(clients);
        System.out.println(stops);
        stop.setClients(clients);
        client.setStops(stops);
        this.stopService.store(stop);
        this.clientService.store(client);

        return ResponseEntity.ok(Map.of("message", message));
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}
