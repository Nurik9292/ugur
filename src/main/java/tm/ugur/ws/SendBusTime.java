package tm.ugur.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tm.ugur.models.Client;
import tm.ugur.services.api.BusTimeService;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SendBusTime {

    private ScheduledExecutorService scheduledExecutorService;
    private final SimpMessagingTemplate messagingTemplate;
    private final BusTimeService busTimeService;
    private Client client;

    private Long stopId;

    private final static Logger logger = LoggerFactory.getLogger(SendBusTime.class);

    @Autowired
    public SendBusTime(SimpMessagingTemplate messagingTemplate, BusTimeService busTimeService) {
        this.messagingTemplate = messagingTemplate;
        this.busTimeService = busTimeService;
    }

    @EventListener
    public void handleWebSocketConnectListenerTime(SessionConnectedEvent event){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::sendBusTime, 0, 3, TimeUnit.SECONDS);
    }

    @EventListener
    public void handleWebSocketDisconnectListenerTime(SessionDisconnectEvent event){
        scheduledExecutorService.shutdown();
    }

    private void sendBusTime(){
        try {
            if(Objects.nonNull(stopId)){
                Map<Integer, Double> times = busTimeService.getBusTime(stopId);
                ObjectMapper mapper = new ObjectMapper();
                messagingTemplate.convertAndSend("/topic/time." + client.getPhone(),
                        mapper.writeValueAsString(times));
            }
        } catch (Exception e) {
            logger.error("Send bus time: " + e.getMessage());
        }
    }


    public void setStopId(Long stopId) {
        this.   stopId = stopId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}