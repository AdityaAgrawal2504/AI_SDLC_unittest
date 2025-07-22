package com.example.chat.v1.enums;

/**
 * Enumeration for non-terminal stream error codes.
 */
public enum ErrorEventCodeC1V1 {
    VALIDATION_FAILED,
    FORBIDDEN_ACTION,
    IDEMPOTENCY_KEY_REPLAY,
    RESOURCE_NOT_FOUND,
    UNKNOWN_ERROR
}
```
```java
// src/main/java/com/example/chat/v1/domain/UserC1V1.java