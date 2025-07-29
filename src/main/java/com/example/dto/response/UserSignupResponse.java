package com.example.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for the response after a successful user signup.
 */
@Data
@Builder
public class UserSignupResponse {
    private UUID id;
    private String phoneNumber;
    private Instant createdAt;
}