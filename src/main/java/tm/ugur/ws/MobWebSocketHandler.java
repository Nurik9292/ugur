package tm.ugur.ws;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MobWebSocketHandler extends TextWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MobWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.setTextMessageSizeLimit(1048576); // Здесь установите такой же размер буфера, как в WebSocketConfig
        super.afterConnectionEstablished(session);
    }

    public void sendToMobileApp(String message) {
        messagingTemplate.convertAndSend("/topic/mobile", message);
    }
}

