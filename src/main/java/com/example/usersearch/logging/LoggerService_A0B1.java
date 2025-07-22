package com.example.usersearch.logging;

import java.util.Map;

/**
 * An abstraction layer for logging services.
 * Allows for easy integration with various logging backends like Kafka, Event Hub, etc.
 */
public interface LoggerService_A0B1 {
    /**
     * Logs an informational message.
     */
    void info(String message, Map<String, Object> details);

    /**
     * Logs an error message.
     */
    void error(String message, Throwable throwable, Map<String, Object> details);

    /**
     * Logs the start of a method execution.
     */
    void logMethodStart(String className, String methodName);

    /**
     * Logs the end of a method execution with its duration.
     */
    void logMethodEnd(String className, String methodName, long durationMillis);
}
```
src/main/java/com/example/usersearch/logging/Log4j2LoggerServiceImpl_A0B1.java
```java