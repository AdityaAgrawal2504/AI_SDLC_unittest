package com.example.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebSocketServiceTest {

    @InjectMocks
    private WebSocketService webSocketService;

    @Mock
    private WebSocketSession session1;
    @Mock
    private WebSocketSession session2;

    private UUID userId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        // Reset the state of the service before each test
        webSocketService = new WebSocketService();
    }

    @Test
    void addConnection_shouldStoreSession() {
        webSocketService.addConnection(userId, session1);
        webSocketService.addConnection(userId, session2);
        
        // Test sending a message to see if both sessions are used
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);
        webSocketService.sendToUser(userId, "payload");

        verify(session1, times(1)).sendMessage(any(TextMessage.class));
        verify(session2, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    void removeConnection_shouldRemoveSession() throws IOException {
        webSocketService.addConnection(userId, session1);
        webSocketService.addConnection(userId, session2);
        
        webSocketService.removeConnection(session1);
        
        // Test sending a message to see that only the remaining session is used
        when(session2.isOpen()).thenReturn(true);
        webSocketService.sendToUser(userId, "payload");

        verify(session1, never()).sendMessage(any(TextMessage.class));
        verify(session2, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    void sendToUser_shouldSendMessageToAllSessions() throws IOException {
        webSocketService.addConnection(userId, session1);
        webSocketService.addConnection(userId, session2);
        
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);

        String payload = "{\"key\":\"value\"}";
        webSocketService.sendToUser(userId, payload);
        
        TextMessage expectedMessage = new TextMessage(payload);
        verify(session1, times(1)).sendMessage(expectedMessage);
        verify(session2, times(1)).sendMessage(expectedMessage);
    }
}