src/main/java/com/example/exception/ForbiddenAccessException.java
package com.example.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for when a user is forbidden from accessing a resource.
 */
public class ForbiddenAccessException extends ApiException {
    public ForbiddenAccessException(String message) {
        super(HttpStatus.FORBIDDEN, "ACCESS_FORBIDDEN", message);
    }
}