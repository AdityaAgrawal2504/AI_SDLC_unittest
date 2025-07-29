src/main/java/com/example/exception/ResourceNotFoundException.java
package com.example.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for when a requested resource is not found.
 */
public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", message);
    }
}