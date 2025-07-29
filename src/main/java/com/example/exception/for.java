src/main/java/com/example/exception/ApiException.java
package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for custom application exceptions.
 */
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }
}