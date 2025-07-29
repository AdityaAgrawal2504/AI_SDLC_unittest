package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn;
}