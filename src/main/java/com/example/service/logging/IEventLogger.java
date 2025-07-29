package com.example.service.logging;

/**
 * An abstraction for logging key business events.
 */
public interface IEventLogger {
    /**
     * Logs a specific business event.
     * @param eventType A category for the event (e.g., "LoginSuccess", "MessageSent").
     * @param message A detailed message describing the event.
     */
    void log(String eventType, String message);
}