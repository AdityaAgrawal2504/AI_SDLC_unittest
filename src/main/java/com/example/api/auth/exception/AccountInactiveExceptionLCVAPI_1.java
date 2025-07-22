package com.example.api.auth.exception;

/**
 * Exception thrown when a login attempt is made on an inactive account.
 */
public class AccountInactiveExceptionLCVAPI_1 extends ApiExceptionLCVAPI_1 {
    public AccountInactiveExceptionLCVAPI_1() {
        super(ErrorCodeLCVAPI_1.ACCOUNT_INACTIVE);
    }
}
```

src/main/java/com/example/api/auth/exception/OtpServiceFailureExceptionLCVAPI_1.java
```java