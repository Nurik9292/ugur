package tm.ugur.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tm.ugur.models.Client;
import tm.ugur.security.ClientDetails;
import tm.ugur.ws.SendBusTime;


@Controller
@RequestMapping("/api")
public class BusTimeController {

    private final SendBusTime sendBusTime;

    @Autowired
    public BusTimeController(SendBusTime sendBusTime) {
        this.sendBusTime = sendBusTime;
    }

    @GetMapping("/get_bus_time/{id}")
    public ResponseEntity<HttpStatus> getBusTime(@PathVariable("id") Long id){
        sendBusTime.setStopId(id);
        sendBusTime.setClient(getAuthClient());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }
}