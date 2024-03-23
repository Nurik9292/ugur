package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tm.ugur.dto.RouteDTO;
import tm.ugur.dto.StopDTO;
import tm.ugur.models.Client;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.api.RouteApiService;
import tm.ugur.util.errors.route.RouteErrorResponse;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.ws.SendClientBuses;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteApiController {

    private final RouteApiService routeService;
    private final SendClientBuses sendClientBuses;

    @Autowired
    public RouteApiController(RouteApiService routeService, SendClientBuses sendClientBuses) {
        this.routeService = routeService;
        this.sendClientBuses = sendClientBuses;
    }



    @GetMapping
    public List<RouteDTO> getRoutes(){
        return this.routeService.findAll();
    }

    @GetMapping("/{id}")
    public RouteDTO getRoute(@PathVariable("id")  Long id){
        RouteDTO route = this.routeService.findOne(id);
        sendClientBuses.setNumber(route.getNumber());
        sendClientBuses.setClient(getAuthClient());
        return route;
    }

    @GetMapping("/start-stops/{id}")
    public ResponseEntity<String> getRouteStartStop(@PathVariable("id") Long id){
        return ResponseEntity.ok(routeService.getRouteStartStop(id));
    }

    @GetMapping("/end-stops/{id}")
    public ResponseEntity<String> getRouteEndStop(@PathVariable("id") Long id){
        return ResponseEntity.ok(routeService.getRouteEndStop(id));
    }



    @ExceptionHandler
    private ResponseEntity<RouteErrorResponse> handleException(RouteNotFoundException e){
        RouteErrorResponse errorResponse = new RouteErrorResponse(
                "Route with this id wasn't found!", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}
