package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserSignupDto {

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format.")
    private String phoneNumber;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
             message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;

    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name must not exceed 100 characters.")
    private String name;
}