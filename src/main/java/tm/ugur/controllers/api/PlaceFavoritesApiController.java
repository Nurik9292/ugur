package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.PlaceDTO;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Client;
import tm.ugur.models.Place;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.api.PlaceApiService;
import tm.ugur.util.errors.places.PlaceErrorResponse;
import tm.ugur.util.errors.places.PlaceNotFoundException;
import tm.ugur.util.errors.stop.StopNotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites/places")
public class PlaceFavoritesApiController {

    private final PlaceApiService placeService;

    @Autowired
    public PlaceFavoritesApiController(PlaceApiService placeService) {
        this.placeService = placeService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, String>> addOrRemoveStop(@PathVariable("id") Long id) {
        Client client = getAuthClient();
        String message = "Successfully removed from favorites";

        Place place = placeService.getPlace(id).orElseThrow(StopNotFoundException::new);
        List<Client> clients = place.getClients();

        if (!clients.contains(client)) {
            clients.add(client);
            message = "Successfully added from favorites";
        } else {
            clients.removeIf(c -> c.getId() == client.getId());
        }

        this.placeService.store(place);

        return ResponseEntity.ok(Map.of("message", message));
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }

    @ExceptionHandler
    private ResponseEntity<PlaceErrorResponse> handleException(PlaceNotFoundException e){
        PlaceErrorResponse errorResponse = new PlaceErrorResponse(
                "Place with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
