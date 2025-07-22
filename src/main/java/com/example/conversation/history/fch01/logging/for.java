package com.example.conversation.history.fch01.logging;

import java.util.Map;

/**
 * Abstracted interface for structured logging to support various backends.
 */
public interface StructuredLoggerFCHAPI01 {
    void logInfo(String message, Map<String, Object> context);
    void logError(String message, Throwable throwable, Map<String, Object> context);
    void logWarn(String message, Map<String, Object> context);
    void logDebug(String message, Map<String, Object> context);
}
src/main/java/com/example/conversation/history/fch01/logging/Log4j2StructuredLoggerFCHAPI02.java