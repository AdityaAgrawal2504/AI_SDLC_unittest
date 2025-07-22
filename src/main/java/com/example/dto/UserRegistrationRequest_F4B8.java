package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Data Transfer Object for user registration request.
 * Contains user's phone number and password for registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest_F4B8 {

    @NotBlank(message = "Phone number must be provided.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phoneNumber;

    @NotBlank(message = "Password must be provided.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
```
```java
src/main/java/com/example/dto/UserRegistrationResponse_F4B8.java