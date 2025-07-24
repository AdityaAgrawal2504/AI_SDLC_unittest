package com.example.messagingapp.logging;

import java.util.Map;

/**
 * Interface for logging structured events, potentially to a different sink like Kafka/EventHub.
 */
public interface EventLogger {
    /**
     * Logs an information-level event.
     * @param eventName The name of the event.
     * @param details A map of details about the event.
     */
    void logInfoEvent(String eventName, Map<String, Object> details);

    /**
     * Logs an error-level event.
     * @param eventName The name of the event.
     * @param details A map of details about the event.
     * @param throwable The throwable associated with the error.
     */
    void logErrorEvent(String eventName, Map<String, Object> details, Throwable throwable);
}