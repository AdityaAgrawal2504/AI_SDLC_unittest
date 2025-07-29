package com.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for verifying a login attempt with an OTP.
 */
@Data
public class LoginVerifyRequest {
    @NotNull(message = "Login session ID is required")
    private UUID loginSessionId;

    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must be 6 digits")
    private String otp;
}