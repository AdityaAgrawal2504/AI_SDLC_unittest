package com.verifyotpapi.logging;

import java.util.Map;

/**
 * Interface for an abstracted structured logging layer.
 */
public interface StructuredLogger_VOTP1 {

    void info(String message, Map<String, Object> context);

    void warn(String message, Map<String, Object> context);

    void error(String message, Map<String, Object> context, Throwable t);

    void logFunctionStart(String functionName);

    void logFunctionEnd(String functionName, long startTime);

}
```
```java
// src/main/java/com/verifyotpapi/logging/Log4j2StructuredLogger_VOTP1.java