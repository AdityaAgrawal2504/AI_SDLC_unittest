src/main/java/com/example/logging/IEventLogger.java
package com.example.logging;

/**
 * Interface for the event logging service.
 */
public interface IEventLogger {

    /**
     * Logs a specific business event.
     * @param eventName The name of the event.
     * @param eventDetails Details or payload of the event.
     */
    void logEvent(String eventName, String eventDetails);
}