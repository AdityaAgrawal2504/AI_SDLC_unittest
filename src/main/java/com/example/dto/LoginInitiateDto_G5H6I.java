src/main/java/com/example/dto/LoginInitiateDto_G5H6I.java
package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInitiateDto_G5H6I {

    @NotBlank(message = "Phone number cannot be blank.")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank.")
    private String password;
}