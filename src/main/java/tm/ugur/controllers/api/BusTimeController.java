package tm.ugur.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tm.ugur.models.Client;
import tm.ugur.security.ClientDetails;
import tm.ugur.ws.SendBusOneTime;
import tm.ugur.ws.SendBusTime;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api")
public class BusTimeController {

    private final SendBusTime sendBusTime;
    private final SendBusOneTime sendBusOneTime;
    private static final Logger logger = LoggerFactory.getLogger(BusTimeController.class);
    @Autowired
    public BusTimeController(SendBusTime sendBusTime, SendBusOneTime sendBusOneTime) {
        this.sendBusTime = sendBusTime;
        this.sendBusOneTime = sendBusOneTime;
    }

    @GetMapping("/get_bus_time/{id}")
    public ResponseEntity<HttpStatus> getBusTime(@PathVariable("id") Long id){
        sendBusTime.setStopId(id);
        sendBusTime.setClient(getAuthClient());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/get_one_bus_time/{id}")
    public ResponseEntity<HttpStatus> getOneBusTime(@PathVariable("id") Long id,
                                                    @RequestParam("number") String number,
                                                    @RequestParam("car_number") String carNumber){

        sendBusOneTime.setStopId(id);
        sendBusOneTime.setNumber(number);
        sendBusOneTime.setCarNumber(carNumber);
        sendBusOneTime.setClient(getAuthClient());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}
