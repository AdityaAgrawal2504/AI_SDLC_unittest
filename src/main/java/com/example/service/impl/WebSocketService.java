package com.example.service.impl;

import com.example.service.IWebSocketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class WebSocketService implements IWebSocketService {

    private static final Logger log = LogManager.getLogger(WebSocketService.class);
    // Maps userId to a list of their active WebSocket sessions
    private final Map<UUID, List<WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    /**
     * Registers a new WebSocket connection for a user.
     * @param userId The ID of the connected user.
     * @param session The WebSocket session.
     */
    @Override
    public void addConnection(UUID userId, WebSocketSession session) {
        userSessions.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(session);
        log.info("WebSocket connection added for user: {}. Session ID: {}", userId, session.getId());
    }

    /**
     * Removes a WebSocket connection upon disconnection.
     * @param session The WebSocket session that was closed.
     */
    @Override
    public void removeConnection(WebSocketSession session) {
        userSessions.forEach((userId, sessions) -> {
            if (sessions.remove(session)) {
                log.info("WebSocket connection removed for user: {}. Session ID: {}", userId, session.getId());
                if (sessions.isEmpty()) {
                    userSessions.remove(userId);
                    log.info("No more active sessions for user: {}. Removing from map.", userId);
                }
            }
        });
    }

    /**
     * Sends a message payload to all active connections of a specific user.
     * @param userId The ID of the target user.
     * @param payload The JSON string payload to send.
     */
    @Override
    public void sendToUser(UUID userId, String payload) {
        List<WebSocketSession> sessions = userSessions.get(userId);
        if (sessions != null && !sessions.isEmpty()) {
            TextMessage message = new TextMessage(payload);
            sessions.forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                        log.debug("Sent message to user {} via session {}", userId, session.getId());
                    }
                } catch (IOException e) {
                    log.error("Failed to send message to user {} via session {}", userId, session.getId(), e);
                }
            });
        } else {
            log.warn("No active WebSocket session found for user {}", userId);
        }
    }
}