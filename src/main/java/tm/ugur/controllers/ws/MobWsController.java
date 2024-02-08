package tm.ugur.controllers.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Bus;
import tm.ugur.services.data_bus.AtLogisticService;
import tm.ugur.services.data_bus.ImdataService;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class MobWsController {

    private final SimpMessageSendingOperations sendToMobileApp;
    private final ImdataService imdataService;
    private final AtLogisticService atLogisticService;

    private ScheduledExecutorService scheduledExecutorService;
    private Map<String, String> map;
    private ObjectMapper mapper;
    private StringBuilder carNumber;

    private String numberRoute;

    private final static Logger logger = LoggerFactory.getLogger(MobWsController.class);

    @Autowired
    public MobWsController(SimpMessageSendingOperations messagingTemplate, ImdataService imdataService, AtLogisticService atLogisticService) {
        this.sendToMobileApp = messagingTemplate;
        this.imdataService = imdataService;
        this.atLogisticService = atLogisticService;
        this.carNumber = new StringBuilder();
        this.mapper = new ObjectMapper();
    }

//    @MessageMapping("/number-route")
//    public void acceptNumberRoute(@Payload String numberRoute){
//        this.numberRoute = numberRoute;
//    }

    @GetMapping("/api/buses/number/{number}")
    public ResponseEntity<HttpStatus> getBusesForNumber(@PathVariable("number") String number){
        this.numberRoute = number;
        return ResponseEntity.ok(HttpStatus.OK);
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
            map = this.imdataService.getDataBus();
            for (JsonNode node : this.atLogisticService.getDataBus().get("list")) {
                if (!node.get("vehiclenumber").asText().isEmpty()) {
                    this.carNumber.setLength(0);
                    this.carNumber.append(node.get("vehiclenumber").asText().trim());
                }
                String number = this.map.get(this.carNumber.toString());

                if (this.map.containsKey(this.carNumber.toString())) {
                    if(this.map.containsValue(numberRoute)){
                        BusDTO bus = new BusDTO(
                                this.carNumber.toString(),
                                Integer.parseInt(number),
                                node.get("status").get("speed").asText(),
                                node.get("imei").asText(),
                                node.get("status").get("dir").asText(),
                                node.get("status").get("lat").asText(),
                                node.get("status").get("lon").asText()
                        );
                        this.sendToMobileApp.convertAndSend("/topic/mobile", this.mapper.writeValueAsString(bus));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("API unavailable: " + e.getMessage());
        }
    }
}
