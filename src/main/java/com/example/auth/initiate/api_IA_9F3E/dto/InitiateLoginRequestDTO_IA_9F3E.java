package com.example.auth.initiate.api_IA_9F3E.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the initiate login request body.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitiateLoginRequestDTO_IA_9F3E {

    @NotBlank(message = "Phone number must not be blank.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits.")
    private String phoneNumber;

    @NotBlank(message = "Password must not be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
```
```java