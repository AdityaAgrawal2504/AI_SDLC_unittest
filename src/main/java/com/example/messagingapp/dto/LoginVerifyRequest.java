package com.example.messagingapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVerifyRequest {

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format.")
    private String phoneNumber;

    @NotBlank(message = "OTP is required.")
    @Size(min = 6, max = 6, message = "OTP must be a 6-digit number.")
    @Pattern(regexp = "\\d{6}", message = "OTP must be a 6-digit number.")
    private String otp;
}