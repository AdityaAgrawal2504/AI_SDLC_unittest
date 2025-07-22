package com.example.auth.logging;

import java.util.Map;

/**
 * Abstraction layer for structured logging, allowing integration with various systems.
 */
public interface StructuredLogger_AuthVerifyOtp_17169 {
    void logInfo(String message, Map<String, Object> context);
    void logWarn(String message, Map<String, Object> context);
    void logError(String message, Map<String, Object> context, Throwable throwable);
}
src/main/java/com/example/auth/config/Log4j2Config_AuthVerifyOtp_17169.java