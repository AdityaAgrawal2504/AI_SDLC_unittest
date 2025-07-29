src/main/java/com/example/dto/UserRegistrationDto.java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * DTO for user registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}