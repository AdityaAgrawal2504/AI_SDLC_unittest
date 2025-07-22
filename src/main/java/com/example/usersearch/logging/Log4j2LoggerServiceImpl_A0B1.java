package com.example.usersearch.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Log4j2 implementation of the LoggerService abstraction.
 * This service sends structured logs to a dedicated logger.
 */
@Service
public class Log4j2LoggerServiceImpl_A0B1 implements LoggerService_A0B1 {

    private static final Logger structuredLogger = LogManager.getLogger("com.example.usersearch.logging.structured");

    @Override
    public void info(String message, Map<String, Object> details) {
        populateThreadContext(details);
        structuredLogger.info(message);
        ThreadContext.clearMap();
    }

    @Override
    public void error(String message, Throwable throwable, Map<String, Object> details) {
        populateThreadContext(details);
        structuredLogger.error(message, throwable);
        ThreadContext.clearMap();
    }

    @Override
    public void logMethodStart(String className, String methodName) {
        structuredLogger.info("Entering method: {}.{}", className, methodName);
    }

    @Override
    public void logMethodEnd(String className, String methodName, long durationMillis) {
        ThreadContext.put("duration_ms", String.valueOf(durationMillis));
        structuredLogger.info("Exiting method: {}.{}", className, methodName);
        ThreadContext.clearMap();
    }

    private void populateThreadContext(Map<String, Object> details) {
        Optional.ofNullable(details)
                .ifPresent(d -> d.forEach((key, value) -> ThreadContext.put(key, String.valueOf(value))));
    }
}
```
src/main/java/com/example/usersearch/logging/LoggingAspect_A0B1.java
```java