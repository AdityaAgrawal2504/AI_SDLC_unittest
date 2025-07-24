src/main/java/com/example/exception/ForbiddenException_W3V4U.java
package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException_W3V4U extends RuntimeException {
    public ForbiddenException_W3V4U(String message) {
        super(message);
    }
}