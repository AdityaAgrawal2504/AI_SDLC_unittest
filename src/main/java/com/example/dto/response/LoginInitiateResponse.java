package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO for the response after initiating a login.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInitiateResponse {
    private UUID loginSessionId;
    private String message;
}