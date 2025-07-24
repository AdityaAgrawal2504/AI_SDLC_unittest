package com.example.exception;

import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends AppException {
    public PermissionDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN, "PERMISSION_DENIED");
    }
}