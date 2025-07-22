package com.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * A utility for creating structured logs that include timing and context.
 */
@Component
public class StructuredLogger_UTIL_9999 {
    
    private static final Logger logger = LogManager.getLogger(StructuredLogger_UTIL_9999.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Logs the execution time and result of a given method/supplier.
     * @param methodName The name of the method being logged.
     * @param action The supplier that executes the business logic.
     * @return The result of the action.
     * @param <T> The return type of the action.
     */
    public <T> T logMethod(String methodName, Supplier<T> action) {
        long startTime = System.currentTimeMillis();
        ObjectNode logNode = objectMapper.createObjectNode();
        logNode.put("method", methodName);
        logNode.put("status", "STARTED");
        
        try {
            log(logNode);

            T result = action.get();
            
            long endTime = System.currentTimeMillis();
            logNode.put("status", "COMPLETED");
            logNode.put("durationMs", endTime - startTime);
            log(logNode);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logNode.put("status", "FAILED");
            logNode.put("durationMs", endTime - startTime);
            logNode.put("error", e.getClass().getSimpleName());
            logNode.put("errorMessage", e.getMessage());
            log(logNode);
            throw e;
        }
    }
    
    /**
     * Logs a general info message with structured context.
     */
    public void info(String message) {
        ObjectNode logNode = objectMapper.createObjectNode();
        logNode.put("level", "INFO");
        logNode.put("message", message);
        log(logNode);
    }
    
    /**
     * Logs an error message with structured context.
     */
    public void error(String message, Throwable t) {
        ObjectNode logNode = objectMapper.createObjectNode();
        logNode.put("level", "ERROR");
        logNode.put("message", message);
        if (t != null) {
            logNode.put("exception", t.getClass().getSimpleName());
            logNode.put("exceptionMessage", t.getMessage());
        }
        log(logNode);
    }

    private void log(ObjectNode logNode) {
        try {
            logger.info(objectMapper.writeValueAsString(logNode));
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize structured log.", e);
        }
    }
}
```
```java
//
// Filename: src/main/java/com/example/validation/ValidPassword_VAL_5001.java
//