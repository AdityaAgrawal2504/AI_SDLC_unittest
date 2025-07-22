package com.example.userregistration.exception;

/**
 * Custom exception thrown when attempting to register a user that already exists.
 */
public class UserAlreadyExistsException_URAPI_1 extends RuntimeException {
    public UserAlreadyExistsException_URAPI_1(String message) {
        super(message);
    }
}
```
src/main/java/com/example/userregistration/exception/PasswordHashingException_URAPI_1.java
```java