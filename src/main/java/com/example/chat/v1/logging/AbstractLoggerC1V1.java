package com.example.chat.v1.logging;

import java.util.Map;

/**
 * Defines the contract for a structured logger, abstracting the underlying implementation.
 */
public interface AbstractLoggerC1V1 {

    void logInfo(String message, Map<String, Object> context);

    void logWarn(String message, Map<String, Object> context);

    void logError(String message, Map<String, Object> context, Throwable throwable);
}
```
```java
// src/main/java/com/example/chat/v1/logging/Log4j2LoggerC1V1.java