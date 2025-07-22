package com.example.chat.v1.exception;

import com.example.chat.v1.enums.ErrorEventCodeC1V1;
import lombok.Getter;

@Getter
public class ChatValidationExceptionC1V1 extends RuntimeException {
    private final ErrorEventCodeC1V1 errorCode;
    private final String clientMessageId;
    private final String originalRequestType;

    public ChatValidationExceptionC1V1(String message, ErrorEventCodeC1V1 errorCode, String clientMessageId, String originalRequestType) {
        super(message);
        this.errorCode = errorCode;
        this.clientMessageId = clientMessageId;
        this.originalRequestType = originalRequestType;
    }

    public ChatValidationExceptionC1V1(String message, ErrorEventCodeC1V1 errorCode) {
        this(message, errorCode, null, null);
    }
}
```
```java
// src/main/java/com/example/chat/v1/logging/LoggableC1V1.java