src/main/java/com/example/logging/impl/EventLogger_Log4j2_D3E4F.java
package com.example.logging.impl;

import com.example.logging.IEventLogger_A1B2C;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Log4j2 implementation of the event logger.
 */
@Component
public class EventLogger_Log4j2_D3E4F implements IEventLogger_A1B2C {

    private static final Logger logger = LogManager.getLogger(EventLogger_Log4j2_D3E4F.class);

    /**
     * Logs an event with its details using Log4j2.
     * @param eventName The name of the event.
     * @param details A map of details about the event.
     */
    @Override
    public void logEvent(String eventName, Map<String, Object> details) {
        String detailsString = details.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(", "));
        logger.info("EVENT: {} - Details: [{}]", eventName, detailsString);
    }
}