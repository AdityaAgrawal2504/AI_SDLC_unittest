package com.example.exception;

import org.springframework.http.HttpStatus;

public class ResourceConflictException extends ApiException {
    public ResourceConflictException(String resource, String field, Object value) {
        super(String.format("%s with %s '%s' already exists.", resource, field, value), HttpStatus.CONFLICT);
    }
}