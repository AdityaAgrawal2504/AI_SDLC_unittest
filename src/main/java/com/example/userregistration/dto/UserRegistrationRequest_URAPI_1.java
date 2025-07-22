package com.example.userregistration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for the user registration request body.
 * Contains validation rules for incoming data.
 */
@Data
public class UserRegistrationRequest_URAPI_1 {

    @NotBlank(message = "Phone number must be provided.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phoneNumber;

    @NotBlank(message = "Password must be provided.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
```
src/main/java/com/example/userregistration/dto/UserRegistrationResponse_URAPI_1.java
```java