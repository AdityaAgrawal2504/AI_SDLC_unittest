package com.example.chat.v1.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Log4j2 implementation of the structured logger.
 */
@Component
public class Log4j2LoggerC1V1 implements AbstractLoggerC1V1 {

    private static final Logger logger = LogManager.getLogger(Log4j2LoggerC1V1.class);

    @Override
    public void logInfo(String message, Map<String, Object> context) {
        try {
            if (context != null && !context.isEmpty()) {
                ThreadContext.putAll(context);
            }
            logger.info(message);
        } finally {
            ThreadContext.clearAll();
        }
    }

    @Override
    public void logWarn(String message, Map<String, Object> context) {
        try {
            if (context != null && !context.isEmpty()) {
                ThreadContext.putAll(context);
            }
            logger.warn(message);
        } finally {
            ThreadContext.clearAll();
        }
    }

    @Override
    public void logError(String message, Map<String, Object> context, Throwable throwable) {
        try {
            if (context != null && !context.isEmpty()) {
                ThreadContext.putAll(context);
            }
            logger.error(message, throwable);
        } finally {
            ThreadContext.clearAll();
        }
    }
}
```
```java
// src/main/java/com/example/chat/v1/logging/LoggingAspectC1V1.java