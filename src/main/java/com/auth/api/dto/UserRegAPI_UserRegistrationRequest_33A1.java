package com.auth.api.dto;

import com.auth.api.constants.UserRegAPI_ValidationConstants_33A1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for the user registration request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegAPI_UserRegistrationRequest_33A1 {

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = UserRegAPI_ValidationConstants_33A1.PHONE_NUMBER_REGEX, message = UserRegAPI_ValidationConstants_33A1.PHONE_NUMBER_MESSAGE)
    private String phoneNumber;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
    @Pattern(regexp = UserRegAPI_ValidationConstants_33A1.PASSWORD_REGEX, message = UserRegAPI_ValidationConstants_33A1.PASSWORD_MESSAGE)
    private String password;
}
```
```java
// src/main/java/com/auth/api/dto/UserRegAPI_UserRegistrationResponse_33A1.java