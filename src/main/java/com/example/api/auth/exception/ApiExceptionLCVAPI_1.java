package com.example.api.auth.exception;

import lombok.Getter;

/**
 * Base custom exception for the application.
 * Allows wrapping a specific error code with each exception instance.
 */
@Getter
public abstract class ApiExceptionLCVAPI_1 extends RuntimeException {
    private final ErrorCodeLCVAPI_1 errorCode;

    /**
     * Constructs an API exception with a specific error code.
     * @param errorCode The error code enum value.
     */
    public ApiExceptionLCVAPI_1(ErrorCodeLCVAPI_1 errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * Constructs an API exception with a specific error code and a custom message.
     * @param message The custom detail message.
     * @param errorCode The error code enum value.
     */
    public ApiExceptionLCVAPI_1(String message, ErrorCodeLCVAPI_1 errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
```

src/main/java/com/example/api/auth/exception/InvalidCredentialsExceptionLCVAPI_1.java
```java