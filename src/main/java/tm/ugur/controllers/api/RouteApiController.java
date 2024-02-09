package tm.ugur.controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tm.ugur.controllers.ws.MobWsController;
import tm.ugur.dto.BusDTO;
import tm.ugur.dto.RouteDTO;
import tm.ugur.models.Route;
import tm.ugur.services.api.RouteApiService;
import tm.ugur.services.data_bus.AtLogisticService;
import tm.ugur.services.data_bus.ImdataService;
import tm.ugur.util.errors.route.RouteErrorResponse;
import tm.ugur.util.errors.route.RouteNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/routes")
public class RouteApiController {

    private final RouteApiService routeService;
    private final SimpMessageSendingOperations sendToMobileApp;
    private final ImdataService imdataService;
    private final AtLogisticService atLogisticService;

    private ScheduledExecutorService scheduledExecutorService;
    private ObjectMapper mapper;
    private StringBuilder carNumber;

    private String numberRoute;

    private final static Logger logger = LoggerFactory.getLogger(MobWsController.class);

    @Autowired
    public RouteApiController(RouteApiService routeService, SimpMessageSendingOperations sendToMobileApp, ImdataService imdataService, AtLogisticService atLogisticService) {
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
    public RouteDTO getRoute(@PathVariable("id") int id){
        RouteDTO route = this.routeService.findOne(id);
        numberRoute = String.valueOf(route.getNumber());
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
            Map<String, String> map = imdataService.getDataBus();
            List<BusDTO> busDTOList = new ArrayList<>();
            for (JsonNode node : atLogisticService.getDataBus().get("list")) {
                if (!node.get("vehiclenumber").asText().isEmpty()) {
                    carNumber.setLength(0);
                    carNumber.append(node.get("vehiclenumber").asText().trim());
                }
                String number = map.get(this.carNumber.toString());
                if (this.numberRoute != null && numberRoute.equals(map.get(carNumber.toString()))
                        && numberRoute.equals(number)) {
                    BusDTO bus = new BusDTO(
                            carNumber.toString(),
                            Integer.parseInt(number),
                            node.get("status").get("speed").asText(),
                            node.get("imei").asText(),
                            node.get("status").get("dir").asText(),
                            node.get("status").get("lat").asText(),
                            node.get("status").get("lon").asText()
                    );
                    busDTOList.add(bus);
                }
            }
            sendToMobileApp.convertAndSend("/topic/mobile", mapper.writeValueAsString(busDTOList));
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
}
