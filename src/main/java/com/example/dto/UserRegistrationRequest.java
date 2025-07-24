package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format.")
    private String phoneNumber;

    @NotBlank(message = "Password is required.")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = "Password must be at least 8 characters long, contain a number, an uppercase letter, a lowercase letter, and a special character."
    )
    private String password;
}