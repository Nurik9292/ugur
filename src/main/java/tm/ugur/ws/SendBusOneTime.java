package tm.ugur.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tm.ugur.models.Client;
import tm.ugur.services.api.BusTimeService;

import java.util.Objects;

@Component
public class SendBusOneTime {

    private final SimpMessagingTemplate messagingTemplate;
    private final BusTimeService busTimeService;

    private Client client;
    private Long stopId;
    private String number;
    private String carNumber;

    private final static Logger logger = LoggerFactory.getLogger(SendBusOneTime.class);


    @Autowired
    public SendBusOneTime(SimpMessagingTemplate messagingTemplate, BusTimeService busTimeService) {
        this.messagingTemplate = messagingTemplate;
        this.busTimeService = busTimeService;
    }

    @Scheduled(fixedDelay = 3000)
    private void sendBusTime(){
        try {
            if(Objects.nonNull(stopId)){
                double time =  busTimeService.getBusOneTime(stopId, number, carNumber);
                ObjectMapper mapper = new ObjectMapper();
                    messagingTemplate.convertAndSend("/topic/time-one-bus." + client.getPhone(),
                        mapper.writeValueAsString(time));
            }
        } catch (Exception e) {
            logger.error("Send bus one time: " + e.getMessage());
        }
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
