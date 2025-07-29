src/main/java/com/example/exception/DuplicateResourceException.java
package com.example.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for when a resource already exists.
 */
public class DuplicateResourceException extends ApiException {
    public DuplicateResourceException(String message) {
        super(HttpStatus.CONFLICT, "RESOURCE_CONFLICT", message);
    }
}