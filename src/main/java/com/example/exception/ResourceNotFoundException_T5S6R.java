src/main/java/com/example/exception/ResourceNotFoundException_T5S6R.java
package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException_T5S6R extends RuntimeException {
    public ResourceNotFoundException_T5S6R(String message) {
        super(message);
    }
}