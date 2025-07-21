package com.yourorg.fetchmessagesapi.exception.fma1;

import com.yourorg.fetchmessagesapi.model.enums.fma1.ErrorCodeFMA_1;

/**
 * Exception for authentication failures (e.g., invalid token).
 */
public class AuthenticationExceptionFMA_1 extends CustomApiExceptionFMA_1 {
    public AuthenticationExceptionFMA_1(String message) {
        super(ErrorCodeFMA_1.AUTHENTICATION_FAILED, message);
    }
}
```
```java
// src/main/java/com/yourorg/fetchmessagesapi/exception/fma1/InvalidParameterExceptionFMA_1.java