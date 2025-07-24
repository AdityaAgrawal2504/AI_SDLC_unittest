src/main/java/com/example/exception/UnauthorizedException_Q7P8O.java
package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException_Q7P8O extends RuntimeException {
    public UnauthorizedException_Q7P8O(String message) {
        super(message);
    }
}