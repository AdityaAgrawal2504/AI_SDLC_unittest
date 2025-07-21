package com.fetchconversations.api.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Provides a structured logging mechanism that can be abstracted for different backends.
 */
public class StructuredLoggerFCA911 {

    private final Logger logger;

    public StructuredLoggerFCA911(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz);
    }

    /**
     * Logs the execution of a function, including its duration.
     * @param functionName The name of the function being logged.
     * @param supplier The function to execute and time.
     * @param <T> The return type of the function.
     * @return The result from the executed function.
     */
    public <T> T logAround(String functionName, Supplier<T> supplier) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("event", functionName + "_start");
        logMap.put("function", functionName);

        logger.info(logMap);

        try {
            T result = supplier.get();
            long endTime = System.currentTimeMillis();
            logMap.put("event", functionName + "_end");
            logMap.put("durationMs", endTime - startTime);
            logMap.put("status", "success");
            logger.info(logMap);
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logMap.put("event", functionName + "_end");
            logMap.put("durationMs", endTime - startTime);
            logMap.put("status", "failure");
            logMap.put("error", e.getClass().getSimpleName());
            logMap.put("errorMessage", e.getMessage());
            logger.error(logMap, e);
            throw e;
        }
    }

    /**
     * Logs an informational message with structured context.
     * @param message The main log message.
     * @param context A map of key-value pairs for additional context.
     */
    public void info(String message, Map<String, Object> context) {
        Map<String, Object> logMap = new HashMap<>(context);
        logMap.put("message", message);
        logger.info(logMap);
    }

    /**
     * Logs an error message with structured context.
     * @param message The main log message.
     * @param context A map of key-value pairs for additional context.
     * @param t The throwable to log.
     */
    public void error(String message, Map<String, Object> context, Throwable t) {
        Map<String, Object> logMap = new HashMap<>(context);
        logMap.put("message", message);
        logger.error(logMap, t);
    }
}
```
```java
// src/main/java/com/fetchconversations/api/enums/SortFieldFCA911.java