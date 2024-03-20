package tm.ugur.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SendBusTime {

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
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        logger.info("Клиент подключен для отправки и времени, sessionId: " + sessionId);
    }

    @EventListener
    public void handleWebSocketDisconnectListenerTime(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        logger.info("Клиент отключен для отправки и времени, sessionId: " + sessionId);
    }

    @Scheduled(fixedDelay = 3000)
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
        this.stopId = stopId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
