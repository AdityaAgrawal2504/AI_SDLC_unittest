package com.auth.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for 409 Conflict errors, such as a user already existing.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserRegAPI_ConflictException_33A1 extends RuntimeException {
    public UserRegAPI_ConflictException_33A1(String message) {
        super(message);
    }
}
```
```java
// src/main/java/com/auth/api/service/hasher/UserRegAPI_PasswordHasher_33A1.java