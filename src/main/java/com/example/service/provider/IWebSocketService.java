package com.example.service.provider;

import com.example.dto.response.MessageDto;

import java.util.UUID;

/**
 * An abstraction for a WebSocket service to push real-time updates to clients.
 */
public interface IWebSocketService {
    /**
     * Sends a message DTO to a specific user via WebSocket.
     * @param userId The ID of the target user.
     * @param message The message DTO to send.
     */
    void sendMessageToUser(UUID userId, MessageDto message);
}