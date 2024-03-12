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
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Client;
import tm.ugur.services.redis.RedisBusService;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SendClientBuses {

    private ScheduledExecutorService scheduledExecutorService;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisBusService redisBusService;

    private Integer number;
    private Client client;

    private final static Logger logger = LoggerFactory.getLogger(SendClientBuses.class);

    @Autowired
    public SendClientBuses(SimpMessagingTemplate messagingTemplate,
                           RedisBusService redisBusService) {
        this.messagingTemplate = messagingTemplate;
        this.redisBusService = redisBusService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(this::sendBusData, 0, 3, TimeUnit.SECONDS);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        scheduledExecutorService.shutdown();
    }

    public void sendBusData(){
        try {
            if(Objects.nonNull(number)){
                List<BusDTO> buses = redisBusService.getBuses(String.valueOf(number));

                ObjectMapper mapper = new ObjectMapper();
                messagingTemplate.convertAndSend("/topic/mobile." + client.getPhone(),
                        mapper.writeValueAsString(buses));
            }
        } catch (Exception e) {
            logger.error("Send bus data: " + e.getMessage());
        }
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
