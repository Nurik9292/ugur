package tm.ugur.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tm.ugur.models.Client;
import tm.ugur.services.api.BusTimeService;

import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class SendBusTime {

    private ScheduledExecutorService scheduledExecutorService;
    private final SimpMessagingTemplate messagingTemplate;
    private final BusTimeService busTimeService;
    private Timer timer;
    private Client client;

    private Long stopId;
    private final static Logger logger = LoggerFactory.getLogger(SendBusTime.class);

    @PostConstruct
    public void init() {
        timer = new Timer();
    }

    @Autowired
    public SendBusTime(SimpMessagingTemplate messagingTemplate, BusTimeService busTimeService) {
        this.messagingTemplate = messagingTemplate;
        this.busTimeService = busTimeService;

    }

    @EventListener
    public void handleWebSocketConnectListenerTime(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendBusTime(sessionId);
            }
        }, 3000, 3000);

//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        scheduledExecutorService.scheduleAtFixedRate(this::sendBusTime, 0, 3, TimeUnit.SECONDS);
    }

    @EventListener
    public void handleWebSocketDisconnectListenerTime(SessionDisconnectEvent event){
//        scheduledExecutorService.shutdown();
        timer.cancel();
    }

    private void sendBusTime(String sessionId){
        try {
            if(Objects.nonNull(stopId)){
                Map<Integer, Double> times = busTimeService.getBusTime(stopId);
                ObjectMapper mapper = new ObjectMapper();
                messagingTemplate.convertAndSendToUser(sessionId, "/topic/time." + client.getPhone(),
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
