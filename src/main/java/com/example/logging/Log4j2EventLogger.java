package com.example.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Log4j2EventLogger implements EventLogger {
    private static final Logger eventLogger = LogManager.getLogger("KafkaAppender"); // Assuming this logger is configured
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Logs a structured event.
     * This implementation sends a JSON string to a dedicated Log4j2 logger.
     * @param eventName The name of the event.
     * @param details A map of event properties.
     */
    @Override
    public void log(String eventName, Map<String, Object> details) {
        try {
            details.put("eventName", eventName);
            details.put("timestamp", System.currentTimeMillis());
            // This will be picked up by the KafkaAppender if configured
            eventLogger.info(objectMapper.writeValueAsString(details));
        } catch (JsonProcessingException e) {
            LogManager.getLogger(this.getClass()).error("Failed to serialize event log.", e);
        }
    }
}