package com.example.handler;

import com.example.service.exception.ConflictException;
import com.example.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFoundException_shouldReturnNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        ResponseEntity<?> response = handler.handleResourceNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleConflictException_shouldReturnConflict() {
        ConflictException ex = new ConflictException("Resource conflict");
        ResponseEntity<?> response = handler.handleConflictException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}