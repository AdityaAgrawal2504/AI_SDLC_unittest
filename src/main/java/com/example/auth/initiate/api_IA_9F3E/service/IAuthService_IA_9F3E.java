package com.example.auth.initiate.api_IA_9F3E.service;

import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginRequestDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginResponseDTO_IA_9F3E;

/**
 * Service contract for handling core authentication logic.
 */
public interface IAuthService_IA_9F3E {

    /**
     * Validates user credentials, generates a login transaction, and triggers an OTP send.
     * @param request The DTO containing the user's phone number and password.
     * @return A DTO with the transaction ID for the next step.
     */
    InitiateLoginResponseDTO_IA_9F3E initiateLogin(InitiateLoginRequestDTO_IA_9F3E request);
}
```
```java