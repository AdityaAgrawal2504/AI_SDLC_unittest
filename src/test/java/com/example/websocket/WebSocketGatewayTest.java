package com.example.websocket;

import com.example.service.IWebSocketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebSocketGatewayTest {

    @Mock
    private IWebSocketService webSocketService;

    @InjectMocks
    private WebSocketGateway webSocketGateway;

    @Mock
    private WebSocketSession session;

    @Test
    void afterConnectionEstablished_withUserId_shouldAddConnection() throws Exception {
        UUID userId = UUID.randomUUID();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", userId);

        when(session.getAttributes()).thenReturn(attributes);

        webSocketGateway.afterConnectionEstablished(session);

        verify(webSocketService, times(1)).addConnection(userId, session);
        verify(session, never()).close();
    }
    
    @Test
    void afterConnectionEstablished_withoutUserId_shouldCloseSession() throws Exception {
        when(session.getAttributes()).thenReturn(new HashMap<>());
        
        webSocketGateway.afterConnectionEstablished(session);
        
        verify(webSocketService, never()).addConnection(any(), any());
        verify(session, times(1)).close(any(CloseStatus.class));
    }

    @Test
    void afterConnectionClosed_shouldRemoveConnection() throws Exception {
        webSocketGateway.afterConnectionClosed(session, CloseStatus.NORMAL);
        verify(webSocketService, times(1)).removeConnection(session);
    }
}