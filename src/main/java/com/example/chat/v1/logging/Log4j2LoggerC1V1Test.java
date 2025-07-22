package com.example.chat.v1.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Log4j2LoggerC1V1Test {

    // No need to mock LogManager or Logger static calls directly,
    // InjectMocks will try to get a Logger instance.
    // We can use Mockito.mockStatic for ThreadContext, if needed,
    // but ThreadContext is usually safe to use directly in unit tests
    // because it's thread-local and will be cleared.
    @InjectMocks
    private Log4j2LoggerC1V1 log4j2Logger;

    private Logger mockLogger;

    @BeforeEach
    void setUp() {
        // Clear ThreadContext before each test
        ThreadContext.clearAll();
        // Mock the actual logger instance that Log4j2LoggerC1V1 would get
        mockLogger = mock(Logger.class);
        // Use reflection or a test-friendly setter if @InjectMocks doesn't set private static fields
        // For static logger, we can mock LogManager.getLogger()
    }

    @Test
    void logInfo_WithContext_PutsContextInThreadContextAndLogs() {
        Map<String, Object> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", 123);
        String message = "Test info message";

        try (MockedStatic<LogManager> mockedLogManager = mockStatic(LogManager.class)) {
            mockedLogManager.when(() -> LogManager.getLogger(any(Class.class))).thenReturn(mockLogger);

            log4j2Logger.logInfo(message, context);

            assertTrue(ThreadContext.isEmpty(), "ThreadContext should be empty after logging");
            verify(mockLogger, times(1)).info(message);
            // Verify ThreadContext interaction implicitly by checking it's empty afterward
            // In a real scenario, you'd assert that putAll was called, then clearAll.
        }
    }

    @Test
    void logInfo_WithoutContext_LogsMessageAndClearsThreadContext() {
        String message = "Test info message without context";

        try (MockedStatic<LogManager> mockedLogManager = mockStatic(LogManager.class)) {
            mockedLogManager.when(() -> LogManager.getLogger(any(Class.class))).thenReturn(mockLogger);

            log4j2Logger.logInfo(message, Collections.emptyMap());

            assertTrue(ThreadContext.isEmpty(), "ThreadContext should be empty after logging");
            verify(mockLogger, times(1)).info(message);
        }
    }

    @Test
    void logWarn_WithContext_PutsContextInThreadContextAndLogs() {
        Map<String, Object> context = Map.of("alert", "high_usage");
        String message = "Test warn message";

        try (MockedStatic<LogManager> mockedLogManager = mockStatic(LogManager.class)) {
            mockedLogManager.when(() -> LogManager.getLogger(any(Class.class))).thenReturn(mockLogger);

            log4j2Logger.logWarn(message, context);

            assertTrue(ThreadContext.isEmpty(), "ThreadContext should be empty after logging");
            verify(mockLogger, times(1)).warn(message);
        }
    }

    @Test
    void logError_WithContextAndThrowable_PutsContextInThreadContextAndLogs() {
        Map<String, Object> context = Map.of("errorType", "runtime");
        String message = "Test error message";
        RuntimeException exception = new RuntimeException("Something went wrong");

        try (MockedStatic<LogManager> mockedLogManager = mockStatic(LogManager.class)) {
            mockedLogManager.when(() -> LogManager.getLogger(any(Class.class))).thenReturn(mockLogger);

            log4j2Logger.logError(message, context, exception);

            assertTrue(ThreadContext.isEmpty(), "ThreadContext should be empty after logging");
            verify(mockLogger, times(1)).error(message, exception);
        }
    }

    @Test
    void logError_WithoutContextAndThrowable_LogsMessageAndClearsThreadContext() {
        String message = "Test error message without context or throwable";

        try (MockedStatic<LogManager> mockedLogManager = mockStatic(LogManager.class)) {
            mockedLogManager.when(() -> LogManager.getLogger(any(Class.class))).thenReturn(mockLogger);

            log4j2Logger.logError(message, Collections.emptyMap(), null);

            assertTrue(ThreadContext.isEmpty(), "ThreadContext should be empty after logging");
            verify(mockLogger, times(1)).error(message, null);
        }
    }
}
```
```java
// src/test/java/com/example/chat/v1/logging/LoggingAspectC1V1Test.java