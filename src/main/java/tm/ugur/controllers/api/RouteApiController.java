package tm.ugur.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tm.ugur.controllers.ws.MobWsController;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Client;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.admin.ClientService;
import tm.ugur.services.api.RouteApiService;
import tm.ugur.services.data_bus.import_data.AtLogisticImport;
import tm.ugur.services.data_bus.import_data.ImdataImport;
import tm.ugur.services.redis.RedisClientRouteNumberService;
import tm.ugur.util.StaticParams;
import tm.ugur.util.errors.route.RouteErrorResponse;
import tm.ugur.util.errors.route.RouteNotFoundException;
import tm.ugur.ws.SendClientBuses;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
        return route;
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
