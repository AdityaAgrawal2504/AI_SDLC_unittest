package com.example.api.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for the login request body.
 * Contains user's phone number and password for authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestLCVAPI_1 {

    /**
     * The user's mobile phone number in E.164 international format.
     */
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 international format (e.g., +14155552671).")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters long.")
    private String phoneNumber;

    /**
     * The user's secret password.
     */
    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters long.")
    private String password;
}
```

src/main/java/com/example/api/auth/dto/LoginSuccessResponseLCVAPI_1.java
```java