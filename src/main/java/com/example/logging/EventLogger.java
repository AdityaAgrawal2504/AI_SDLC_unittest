src/main/java/com/example/logging/EventLogger.java
package com.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Abstracted layer for logging events to Kafka/EventHub.
 * For this implementation, it logs to a dedicated logger.
 */
@Service
public class EventLogger implements IEventLogger {

    private static final Logger eventLogger = LogManager.getLogger("EventLogger");

    /**
     * Logs a specific business event.
     * @param eventName The name of the event.
     * @param eventDetails Details or payload of the event.
     */
    @Override
    public void logEvent(String eventName, String eventDetails) {
        // In a real implementation, this would send a message to a Kafka topic.
        // For example: kafkaTemplate.send("chat-events", eventName, eventDetails);
        eventLogger.info("Event: [{}], Details: [{}]", eventName, eventDetails);
    }
}