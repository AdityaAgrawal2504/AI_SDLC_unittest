src/main/java/com/example/logging/IEventLogger_A1B2C.java
package com.example.logging;

import java.util.Map;

/**
 * Interface for an abstracted event logger.
 * This allows for swapping the underlying implementation (e.g., to Kafka, EventHub).
 */
public interface IEventLogger_A1B2C {
    void logEvent(String eventName, Map<String, Object> details);
}