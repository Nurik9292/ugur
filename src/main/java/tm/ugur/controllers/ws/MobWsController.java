package tm.ugur.controllers.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tm.ugur.dto.BusDTO;
import tm.ugur.services.data_bus.AtLogisticService;
import tm.ugur.services.data_bus.ImdataService;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@RestController
public class MobWsController {

    private final SimpMessageSendingOperations sendToMobileApp;
    private final ImdataService imdataService;
    private final AtLogisticService atLogisticService;

    private ScheduledExecutorService scheduledExecutorService;
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


//    @GetMapping("/api/buses/number/{number}")
//    public ResponseEntity<HttpStatus> getBusesForNumber(@PathVariable("number") String number){
//        this.numberRoute = number;
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//
//


}
