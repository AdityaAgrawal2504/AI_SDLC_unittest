package com.example.userregistration.exception;

/**
 * Custom exception for errors occurring during the password hashing process.
 */
public class PasswordHashingException_URAPI_1 extends RuntimeException {
    public PasswordHashingException_URAPI_1(String message, Throwable cause) {
        super(message, cause);
    }
}
```
src/main/java/com/example/userregistration/config/SecurityConfig_URAPI_1.java
```java