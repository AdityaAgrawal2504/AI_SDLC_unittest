package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for the login request body.
 * Contains user credentials for authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest_LCVAPI_104 {

    /**
     * The user's mobile phone number in E.164 international format.
     */
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format (e.g., +14155552671).")
    @Size(min = 10, max = 15, message = "Phone number length is invalid.")
    private String phoneNumber;

    /**
     * The user's secret password.
     */
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
    private String password;
}
src/main/java/com/example/auth/dto/LoginSuccessResponse_LCVAPI_105.java