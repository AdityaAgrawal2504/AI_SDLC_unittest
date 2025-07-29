src/main/java/com/example/dto/OtpVerificationDto.java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * DTO for OTP verification.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationDto {
    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @NotBlank(message = "OTP is required.")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must be 6 digits.")
    private String otp;
}