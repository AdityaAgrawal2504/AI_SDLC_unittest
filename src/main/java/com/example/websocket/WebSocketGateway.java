package com.example.websocket;

import com.example.service.IWebSocketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

@Component
public class WebSocketGateway extends TextWebSocketHandler {

    private static final Logger log = LogManager.getLogger(WebSocketGateway.class);
    private final IWebSocketService webSocketService;

    public WebSocketGateway(IWebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    /**
     * Called after a WebSocket connection is established.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UUID userId = (UUID) session.getAttributes().get("userId");
        if (userId != null) {
            webSocketService.addConnection(userId, session);
        } else {
            log.warn("Closing session {} because no userId was found in attributes.", session.getId());
            session.close(CloseStatus.POLICY_VIOLATION.withReason("User not authenticated"));
        }
    }

    /**
     * Handles incoming text messages from clients.
     * Per spec, chat messages go via REST, so this is mainly for other real-time events if needed.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // The spec implies messages are sent via the REST API, not received from the client over WebSocket.
        // This handler can be used for other real-time interactions like "user is typing" events.
        log.info("Received message from {}: {}", session.getAttributes().get("userId"), message.getPayload());
    }

    /**
     * Called after a WebSocket connection is closed.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketService.removeConnection(session);
        log.info("Connection closed for session {}. Status: {}", session.getId(), status);
    }
}