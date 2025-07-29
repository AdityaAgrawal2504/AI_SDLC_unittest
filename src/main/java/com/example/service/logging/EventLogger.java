package com.example.service.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Concrete implementation of the event logger using Log4j2.
 */
@Service
public class EventLogger implements IEventLogger {

    private static final Logger logger = LogManager.getLogger(EventLogger.class);

    /**
     * Logs a structured event message.
     * @param eventType The type of the event (e.g., "UserCreated").
     * @param message The descriptive message for the event.
     */
    @Override
    public void log(String eventType, String message) {
        // In a real system, this would format as JSON or another structured format.
        logger.info("EVENT [{}]: {}", eventType, message);
    }
}