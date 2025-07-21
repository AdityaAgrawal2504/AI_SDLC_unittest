package com.yourorg.userregistration.exception;

import com.yourorg.userregistration.enums.ErrorCodeURAPI_1201;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when attempting to register a user that already exists.
 */
public class UserAlreadyExistsExceptionURAPI_1201 extends AbstractRestExceptionURAPI_1201 {

    public UserAlreadyExistsExceptionURAPI_1201(String phoneNumber) {
        super("A user with phone number '" + phoneNumber + "' already exists.",
              HttpStatus.CONFLICT,
              ErrorCodeURAPI_1201.USER_ALREADY_EXISTS);
    }
}
```
```java
// src/main/java/com/yourorg/userregistration/security/IPasswordHasherURAPI_1201.java