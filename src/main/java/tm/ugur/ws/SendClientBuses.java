package tm.ugur.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tm.ugur.controllers.ws.MobWsController;
import tm.ugur.dto.BusDTO;
import tm.ugur.models.Client;
import tm.ugur.security.ClientDetails;
import tm.ugur.services.redis.RedisBusService;
import tm.ugur.util.Constant;

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

    private final static Logger logger = LoggerFactory.getLogger(MobWsController.class);

    @Autowired
    public SendClientBuses(SimpMessagingTemplate messagingTemplate,
                           RedisBusService redisBusService) {
        this.messagingTemplate = messagingTemplate;
        this.redisBusService = redisBusService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::sendBusData, 0, 3, TimeUnit.SECONDS);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        scheduledExecutorService.shutdown();
    }

    private void sendBusData(){
        try {
            if(Objects.nonNull(number)){
                List<BusDTO> buses = redisBusService.getBuses(Constant.BUSES_DIVIDED_INTO_ROUTES + number);

                ObjectMapper mapper = new ObjectMapper();
                messagingTemplate.convertAndSend("/topic/mobile." + getAuthClient().getPhone(),
                        mapper.writeValueAsString(buses));
            }
        } catch (Exception e) {
            logger.error("Send bus data: " + e.getMessage());
        }
    }

    private Client getAuthClient(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        return clientDetails.getClient();
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
