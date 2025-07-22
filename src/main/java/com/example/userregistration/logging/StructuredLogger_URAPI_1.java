package com.example.userregistration.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstracted logging service to create structured logs for external systems.
 * This decouples the application logic from the specific logging destination (e.g., Kafka).
 */
@Service
@RequiredArgsConstructor
public class StructuredLogger_URAPI_1 {

    private final LogPublisher_URAPI_1 logPublisher;

    /**
     * Logs a generic event with a custom message and context.
     * @param level The log level (e.g., "INFO", "ERROR").
     * @param message The primary log message.
     * @param context A map of additional key-value pairs for context.
     */
    public void log(String level, String message, Map<String, Object> context) {
        Map<String, Object> event = new HashMap<>();
        event.put("level", level);
        event.put("message", message);
        event.put("context", context);
        logPublisher.publish(event);
    }

    /**
     * Logs an error event.
     * @param message The error message.
     * @param error The exception that occurred.
     * @param context Additional context about the error.
     */
    public void error(String message, Throwable error, Map<String, Object> context) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorMessage", error.getMessage());
        // For stack trace, it's generally better to get more of it,
        // but for a concise structured log, the first element might suffice,
        // or convert to string for full trace.
        errorDetails.put("stackTrace", error.getStackTrace()[0].toString()); 

        Map<String, Object> fullContext = new HashMap<>(context);
        fullContext.put("error", errorDetails);

        log("ERROR", message, fullContext);
    }
}
```
src/main/java/com/example/userregistration/logging/LoggingAspect_URAPI_1.java
```java