package com.example.userregistration.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MapMessage; // Added missing import
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class LogPublisher_URAPI_1 {

    private static final Logger log = LogManager.getLogger("ExternalLogPublisher");

    /**
     * Publishes a structured log event.
     * In a real system, this would send the event to Kafka, Event Hub, etc.
     * Here, it logs to a dedicated JSON file via log4j2 configuration.
     * @param event A map representing the structured log event.
     */
    public void publish(Map<String, Object> event) {
        try {
            // Log4j's JsonLayout will automatically format the MapMessage into a JSON string.
            log.info(new MapMessage<>(event));
        } catch (Exception e) {
            // Fallback to standard logger if publishing fails
            System.err.println("Failed to publish structured log: " + e.getMessage());
        }
    }
}
```
src/main/java/com/example/userregistration/logging/StructuredLogger_URAPI_1.java
```java