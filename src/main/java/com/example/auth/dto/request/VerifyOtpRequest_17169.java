package com.example.auth.dto.request;

import com.example.auth.util.ValidationConstants_AuthVerifyOtp_17169;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO representing the request body for the OTP verification endpoint.
 */
@Data
public class VerifyOtpRequest_17169 {

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = ValidationConstants_AuthVerifyOtp_17169.PHONE_NUMBER_REGEX, message = ValidationConstants_AuthVerifyOtp_17169.PHONE_NUMBER_MESSAGE)
    private String phoneNumber;

    @NotBlank(message = "OTP is required.")
    @Pattern(regexp = ValidationConstants_AuthVerifyOtp_17169.OTP_REGEX, message = ValidationConstants_AuthVerifyOtp_17169.OTP_MESSAGE)
    private String otp;
}
src/main/java/com/example/auth/dto/response/VerifyOtpSuccessResponse_17169.java