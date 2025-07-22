package com.example.conversation.api.logging;

import java.util.Map;

/**
 * An abstraction layer for structured logging, allowing for different implementations (e.g., Log4j2, Logback)
 * and facilitating integration with external logging systems like Kafka or Azure Event Hub.
 */
public interface StructuredLoggerFCA1 {

    /**
     * Logs an informational message with structured context.
     *
     * @param message The main log message.
     * @param context A map of key-value pairs providing structured context.
     */
    void info(String message, Map<String, Object> context);

    /**
     * Logs an error message with structured context and an associated throwable.
     *
     * @param message   The main log message.
     * @param context   A map of key-value pairs providing structured context.
     * @param throwable The exception to log.
     */
    void error(String message, Map<String, Object> context, Throwable throwable);

    /**
     * Logs a general message at a specified level with structured context.
     *
     * @param level     The logging level (e.g., "INFO", "WARN", "ERROR").
     * @param message   The main log message.
     * @param context   A map of key-value pairs providing structured context.
     */
    void log(String level, String message, Map<String, Object> context);
}
src/main/java/com/example/conversation/api/logging/Log4j2StructuredLoggerFCA1.java