package com.example.service.provider;

import com.example.dto.response.MessageDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * A stub implementation of the WebSocket service that logs the action.
 */
@Service
public class StubWebSocketService implements IWebSocketService {

    private static final Logger logger = LogManager.getLogger(StubWebSocketService.class);

    /**
     * Logs the intent to send a message to a user.
     * @param userId The ID of the target user.
     * @param message The message DTO to send.
     */
    @Override
    public void sendMessageToUser(UUID userId, MessageDto message) {
        logger.info("--- STUB WEBSOCKET ---");
        logger.info("Pushing message {} to user {}", message.getId(), userId);
        logger.info("----------------------");
    }
}