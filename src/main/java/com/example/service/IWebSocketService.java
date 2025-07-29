package com.example.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

public interface IWebSocketService {
    void addConnection(UUID userId, WebSocketSession session);
    void removeConnection(WebSocketSession session);
    void sendToUser(UUID userId, String payload);
}