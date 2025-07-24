package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public AppException(String message, HttpStatus status, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }
}