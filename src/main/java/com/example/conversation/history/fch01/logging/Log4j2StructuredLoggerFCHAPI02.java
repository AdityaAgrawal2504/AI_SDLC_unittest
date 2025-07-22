package com.example.conversation.history.fch01.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Log4j2 implementation of the structured logging interface.
 * Formats log data as a single JSON string.
 */
@Component
public class Log4j2StructuredLoggerFCHAPI02 implements StructuredLoggerFCHAPI01 {

    private static final Logger logger = LogManager.getLogger(Log4j2StructuredLoggerFCHAPI02.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void logInfo(String message, Map<String, Object> context) {
        logger.info(format(message, context));
    }

    @Override
    public void logError(String message, Throwable throwable, Map<String, Object> context) {
        Map<String, Object> fullContext = new HashMap<>(context);
        fullContext.put("exception", throwable.getClass().getName());
        fullContext.put("stackTrace", throwable.getStackTrace());
        logger.error(format(message, fullContext), throwable);
    }

    @Override
    public void logWarn(String message, Map<String, Object> context) {
        logger.warn(format(message, context));
    }

    @Override
    public void logDebug(String message, Map<String, Object> context) {
        logger.debug(format(message, context));
    }

    private String format(String message, Map<String, Object> context) {
        Map<String, Object> logObject = new HashMap<>();
        logObject.put("message", message);
        logObject.put("context", context);
        try {
            return objectMapper.writeValueAsString(logObject);
        } catch (JsonProcessingException e) {
            // Fallback to a simple format if JSON serialization fails
            return "{\"message\": \"Error serializing log message\", \"originalMessage\": \"" + message + "\"}";
        }
    }
}
src/main/java/com/example/conversation/history/fch01/logging/LoggingAspectFCHAPI03.java