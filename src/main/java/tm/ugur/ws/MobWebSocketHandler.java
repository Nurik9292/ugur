package tm.ugur.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tm.ugur.models.Bus;
import tm.ugur.services.data_bus.AtLogisticImport;
import tm.ugur.services.data_bus.ImdataImport;

import java.util.Map;

@Component
public class MobWebSocketHandler extends TextWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final ImdataImport imdataService;
    private final AtLogisticImport atLogisticService;
    private Map<String, String> map;
    private StringBuilder carNumbmer;
    private  ObjectMapper mapper;
    private String routeNumber;

    private final static Logger logger = LoggerFactory.getLogger(MobWebSocketHandler.class);

    @Autowired
    public MobWebSocketHandler(SimpMessagingTemplate messagingTemplate, ImdataImport imdataService, AtLogisticImport atLogisticService) {
        this.messagingTemplate = messagingTemplate;
        this.imdataService = imdataService;
        this.atLogisticService = atLogisticService;
        this.carNumbmer = new StringBuilder();
        this.mapper = new ObjectMapper();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        logger.info("Client connection: " + session.getId());
        this.dataProcessing(true);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        logger.info("Client disconnected: " + session.getId());
        this.dataProcessing(false);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        this.routeNumber = message.getPayload();
    }

    public void sendToMobileApp(String message) {
        messagingTemplate.convertAndSend("/topic/mobile", message);
    }

    private void dataProcessing(boolean work){
        while (work){
            try {
                map = this.imdataService.getDataBus();
                for (JsonNode node : this.atLogisticService.getDataBus().get("list")) {
                    if (!node.get("vehiclenumber").asText().isEmpty()) {
                        this.carNumbmer.setLength(0);
                        this.carNumbmer.append(node.get("vehiclenumber").asText().trim());
                    }
                    String number = this.map.get(this.carNumbmer.toString());

                    if (this.map.containsKey(this.carNumbmer.toString())) {
                        if(this.map.containsValue(routeNumber)){
                            Bus bus = new Bus(
                                    this.carNumbmer.toString(),
                                    Integer.parseInt(number),
                                    node.get("status").get("speed").asText(),
                                    node.get("imei").asText(),
                                    node.get("status").get("dir").asText(),
                                    node.get("status").get("lat").asText(),
                                    node.get("status").get("lon").asText()
                            );
                            this.sendToMobileApp(this.mapper.writeValueAsString(bus));
                        }
                    }
                }
            } catch (Exception e) {
                this.logger.error("API unavailable: " + e.getMessage());
            }
        }
    }
}

