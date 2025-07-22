package com.example.api.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for a successful login response.
 * Confirms that credentials were verified and OTP was sent.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessResponseLCVAPI_1 {

    /**
     * A confirmation message indicating the OTP has been dispatched.
     */
    private String message;

    /**
     * A unique identifier for the API request for tracking and logging.
     */
    private String requestId;
}
```

src/main/java/com/example/api/auth/dto/ErrorResponseLCVAPI_1.java
```java