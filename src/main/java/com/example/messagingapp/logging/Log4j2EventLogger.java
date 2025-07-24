package com.example.messagingapp.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class Log4j2EventLogger implements EventLogger {

    // This logger is specifically configured in log4j2.xml to use a Kafka appender
    private static final Logger KAFKA_LOGGER = LogManager.getLogger("KAFKA_JSON_APPENDER");
    private final ObjectMapper objectMapper;

    /**
     * Logs an information-level event to the configured Kafka logger.
     * @param eventName The name of the event.
     * @param details A map of details about the event.
     */
    @Override
    public void logInfoEvent(String eventName, Map<String, Object> details) {
        logEvent(Level.INFO, eventName, details, null);
    }

    /**
     * Logs an error-level event to the configured Kafka logger.
     * @param eventName The name of the event.
     * @param details A map of details about the event.
     * @param throwable The throwable associated with the error.
     */
    @Override
    public void logErrorEvent(String eventName, Map<String, Object> details, Throwable throwable) {
        logEvent(Level.ERROR, eventName, details, throwable);
    }

    private void logEvent(Level level, String eventName, Map<String, Object> details, Throwable throwable) {
        try {
            Map<String, Object> logPayload = new HashMap<>();
            logPayload.put("eventName", eventName);
            logPayload.put("timestamp", System.currentTimeMillis());
            logPayload.put("level", level.toString());
            logPayload.put("details", details);

            if (throwable != null) {
                logPayload.put("error", throwable.getMessage());
            }

            String jsonPayload = objectMapper.writeValueAsString(logPayload);
            KAFKA_LOGGER.log(level, jsonPayload);
        } catch (Exception e) {
            log.error("Failed to serialize or send event log to Kafka", e);
        }
    }
}