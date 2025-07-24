src/main/java/com/example/exception/ConflictException_Z1Y2X.java
package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException_Z1Y2X extends RuntimeException {
    public ConflictException_Z1Y2X(String message) {
        super(message);
    }
}