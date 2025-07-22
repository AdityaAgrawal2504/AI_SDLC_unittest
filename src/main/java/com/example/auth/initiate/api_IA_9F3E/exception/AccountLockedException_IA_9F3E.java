package com.example.auth.initiate.api_IA_9F3E.exception;

import com.example.auth.initiate.api_IA_9F3E.constants.ErrorCode_IA_9F3E;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user's account is locked.
 */
public class AccountLockedException_IA_9F3E extends BaseApiException_IA_9F3E {

    private static final String DEFAULT_MESSAGE = "Your account is locked. Please contact support.";

    /**
     * Constructs an AccountLockedException with a default message.
     */
    public AccountLockedException_IA_9F3E() {
        super(DEFAULT_MESSAGE, HttpStatus.FORBIDDEN, ErrorCode_IA_9F3E.ACCOUNT_LOCKED);
    }
}
```
```java