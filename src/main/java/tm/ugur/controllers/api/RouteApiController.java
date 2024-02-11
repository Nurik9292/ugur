package tm.ugur.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Client;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.ClientService;
import tm.ugur.services.api.RouteApiService;
import tm.ugur.services.data_bus.AtLogisticImport;
import tm.ugur.services.data_bus.ImdataImport;
import tm.ugur.util.errors.route.RouteErrorResponse;
import tm.ugur.util.errors.route.RouteNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/routes")
public class RouteApiController {

    private final RouteApiService routeService;
    private final SimpMessageSendingOperations sendToMobileApp;
    private final ImdataImport imdataService;
    private final AtLogisticImport atLogisticService;
    private ScheduledExecutorService scheduledExecutorService;

    private Integer numberRoute;

    private final static Logger logger = LoggerFactory.getLogger(MobWsController.class);

    @Autowired
    public RouteApiController(RouteApiService routeService,
                              SimpMessageSendingOperations sendToMobileApp,
                              ImdataImport imdataService,
                              AtLogisticImport atLogisticService, ClientService clientService) {
        this.routeService = routeService;
        this.sendToMobileApp = sendToMobileApp;
        this.imdataService = imdataService;
        this.atLogisticService = atLogisticService;
    }



    @GetMapping
    public List<RouteDTO> getRoutes(){
        return this.routeService.findAll();
    }

    @GetMapping("/{id}")
    public RouteDTO getRoute(@PathVariable("id")  Long id){
        RouteDTO route = this.routeService.findOne(id);
        numberRoute = route.getNumber();
        return route;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::sendBusData, 0, 3, TimeUnit.SECONDS);
    }

    @EventListener
    public void handleWebSocketDesconectListener(SessionDisconnectEvent event){
        scheduledExecutorService.shutdown();
    }

    private void sendBusData(){
        try {
            Map<String, BusDTO> imdateBuses = imdataService.getBusData();
            Map<String, BusDTO> atLogistikaBuses = atLogisticService.getBusData();
            List<BusDTO> buses = new ArrayList<>();

            for (Map.Entry<String, BusDTO> entry : imdateBuses.entrySet()) {
                BusDTO imdataBus = entry.getValue();
                if (imdataBus.getNumber() == numberRoute && atLogistikaBuses.containsKey(entry.getKey())) {
                    BusDTO atLogistikaBus = atLogistikaBuses.get(entry.getKey());
                    buses.add(new BusDTO(
                            1L,
                            imdataBus.getCarNumber(),
                            imdataBus.getNumber(),
                            atLogistikaBus.getSpeed(),
                            atLogistikaBus.getDir(),
                            atLogistikaBus.getLocation()
                    ));
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            sendToMobileApp.convertAndSend("/topic/mobile", mapper.writeValueAsString(buses));
        } catch (Exception e) {
            logger.error("API unavailable: " + e.getMessage());
        }
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
