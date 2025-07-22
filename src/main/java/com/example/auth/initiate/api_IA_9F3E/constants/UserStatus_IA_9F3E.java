package com.example.auth.initiate.api_IA_9F3E.constants;

/**
 * Enumeration for user account statuses.
 */
public enum UserStatus_IA_9F3E {
    /**
     * The user account is active and can be used for login.
     */
    ACTIVE,

    /**
     * The user account is locked, likely due to too many failed login attempts.
     */
    LOCKED,

    /**
     * The user account has been created but not yet verified.
     */
    PENDING_VERIFICATION
}
```
```java