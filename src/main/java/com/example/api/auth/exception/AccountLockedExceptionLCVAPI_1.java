package com.example.api.auth.exception;

/**
 * Exception thrown when a login attempt is made on a locked account.
 */
public class AccountLockedExceptionLCVAPI_1 extends ApiExceptionLCVAPI_1 {
    public AccountLockedExceptionLCVAPI_1() {
        super(ErrorCodeLCVAPI_1.ACCOUNT_LOCKED);
    }
}
```

src/main/java/com/example/api/auth/exception/AccountInactiveExceptionLCVAPI_1.java
```java