src/main/java/com/example/dto/AuthTokensDto.java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication tokens.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokensDto {
    private String accessToken;
    private String refreshToken;
}