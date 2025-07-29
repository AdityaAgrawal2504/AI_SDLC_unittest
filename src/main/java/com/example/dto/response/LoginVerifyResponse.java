package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the response after successfully verifying a login, containing the JWT.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginVerifyResponse {
    private String accessToken;
    private String tokenType = "Bearer";
}