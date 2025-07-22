package com.example.api.auth.exception;

/**
 * Exception thrown when the external OTP service fails or is unavailable.
 */
public class OtpServiceFailureExceptionLCVAPI_1 extends ApiExceptionLCVAPI_1 {
    public OtpServiceFailureExceptionLCVAPI_1(String message) {
        super(message, ErrorCodeLCVAPI_1.OTP_SERVICE_UNAVAILABLE);
    }
}
```

src/main/java/com/example/api/auth/exception/GlobalExceptionHandlerLCVAPI_1.java
```java