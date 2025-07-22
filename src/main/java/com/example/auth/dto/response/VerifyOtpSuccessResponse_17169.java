package com.example.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the successful response after OTP verification.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpSuccessResponse_17169 {

    private String sessionToken;

    @Builder.Default
    private String tokenType = "Bearer";

    private Long expiresIn;
}
src/main/java/com/example/auth/dto/response/ErrorResponse_17169.java