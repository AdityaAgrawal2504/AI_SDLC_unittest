package com.example.api.auth.exception;

/**
 * Exception thrown when authentication fails due to incorrect credentials.
 */
public class InvalidCredentialsExceptionLCVAPI_1 extends ApiExceptionLCVAPI_1 {
    public InvalidCredentialsExceptionLCVAPI_1() {
        super(ErrorCodeLCVAPI_1.INVALID_CREDENTIALS);
    }
}
```

src/main/java/com/example/api/auth/exception/AccountLockedExceptionLCVAPI_1.java
```java