package com.verifyotpapi.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Log4j2 implementation of the structured logger.
 */
@Component
public class Log4j2StructuredLogger_VOTP1 implements StructuredLogger_VOTP1 {

    private static final Logger logger = LogManager.getLogger(Log4j2StructuredLogger_VOTP1.class);

    /**
     * Logs an informational message with structured context.
     */
    @Override
    public void info(String message, Map<String, Object> context) {
        ThreadContext.put("context", new JSONObject(context).toString());
        logger.info(message);
        ThreadContext.clearMap();
    }

    /**
     * Logs a warning message with structured context.
     */
    @Override
    public void warn(String message, Map<String, Object> context) {
        ThreadContext.put("context", new JSONObject(context).toString());
        logger.warn(message);
        ThreadContext.clearMap();
    }

    /**
     * Logs an error message with structured context and a throwable.
     */
    @Override
    public void error(String message, Map<String, Object> context, Throwable t) {
        ThreadContext.put("context", new JSONObject(context).toString());
        logger.error(message, t);
        ThreadContext.clearMap();
    }

    /**
     * Logs the start of a function execution.
     */
    @Override
    public void logFunctionStart(String functionName) {
        long startTime = System.currentTimeMillis();
        ThreadContext.put("function", functionName);
        ThreadContext.put("event", "start");
        ThreadContext.put("startTime", String.valueOf(startTime));
        logger.info("Starting execution");
        ThreadContext.remove("event");
        ThreadContext.remove("startTime");
    }

    /**
     * Logs the end of a function execution, including duration.
     */
    @Override
    public void logFunctionEnd(String functionName, long startTime) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        ThreadContext.put("function", functionName);
        ThreadContext.put("event", "end");
        ThreadContext.put("durationMs", String.valueOf(duration));
        logger.info("Finished execution");
        ThreadContext.clearMap();
    }
}
```
```java
// src/main/java/com/verifyotpapi/service/ITokenService_VOTP1.java