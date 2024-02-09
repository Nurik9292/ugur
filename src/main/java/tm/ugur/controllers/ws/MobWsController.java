package tm.ugur.controllers.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import tm.ugur.services.data_bus.AtLogisticImport;
import tm.ugur.services.data_bus.ImdataImport;

import java.util.concurrent.ScheduledExecutorService;

@RestController
public class MobWsController {

    private final SimpMessageSendingOperations sendToMobileApp;
    private final ImdataImport imdataService;
    private final AtLogisticImport atLogisticService;

    private ScheduledExecutorService scheduledExecutorService;
    private ObjectMapper mapper;
    private StringBuilder carNumber;

    private String numberRoute;

    private final static Logger logger = LoggerFactory.getLogger(MobWsController.class);

    @Autowired
    public MobWsController(SimpMessageSendingOperations messagingTemplate, ImdataImport imdataService, AtLogisticImport atLogisticService) {
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
