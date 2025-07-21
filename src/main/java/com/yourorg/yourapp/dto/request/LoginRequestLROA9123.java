package com.yourorg.yourapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the login and OTP request.
 * Contains user credentials for the first factor of authentication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestLROA9123 {

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Must be a valid phone number in E.164 format.")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters.")
    private String phone;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
    private String password;
}
```
src/main/java/com/yourorg/yourapp/dto/response/LoginSuccessResponseLROA9123.java
```java