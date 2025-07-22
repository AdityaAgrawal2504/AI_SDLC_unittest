package com.example.exception;

/**
 * Exception thrown when a user tries to access a resource they are not authorized for.
 */
public class ForbiddenAccessException_CHAT_2011 extends RuntimeException {
    public ForbiddenAccessException_CHAT_2011(String message) {
        super(message);
    }
}
```
```java
//
// Filename: src/main/java/com/example/grpc/AuthInterceptor_GRPC_4001.java
//