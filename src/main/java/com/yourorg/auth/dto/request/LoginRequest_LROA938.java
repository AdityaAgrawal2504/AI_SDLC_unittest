package com.yourorg.auth.dto.request;

import com.yourorg.auth.constants.ApiConstants_LROA938;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO for the login request body, including validation constraints.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest_LROA938 {

    @NotBlank(message = "Phone number is mandatory.")
    @Pattern(regexp = ApiConstants_LROA938.PHONE_PATTERN, message = "Must be a valid phone number in E.164 format.")
    @Size(min = 10, max = 15, message = "Phone number length must be between 10 and 15 characters.")
    private String phone;

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
    private String password;
}
```
```java
// src/main/java/com/yourorg/auth/dto/response/LoginSuccessResponse_LROA938.java