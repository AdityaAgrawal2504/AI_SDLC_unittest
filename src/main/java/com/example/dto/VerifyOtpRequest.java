package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @NotBlank(message = "OTP is required.")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must be a 6-digit number.")
    private String otp;
}