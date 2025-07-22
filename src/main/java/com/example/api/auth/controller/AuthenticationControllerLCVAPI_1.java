package com.example.api.auth.controller;

import com.example.api.auth.dto.LoginRequestLCVAPI_1;
import com.example.api.auth.dto.LoginSuccessResponseLCVAPI_1;
import com.example.api.auth.service.IAuthenticationServiceLCVAPI_1;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationControllerLCVAPI_1 {

    private final IAuthenticationServiceLCVAPI_1 authenticationService;

    /**
     * Handles the POST request to /auth/login.
     * Validates user credentials and triggers an OTP on success.
     * @param loginRequest The request body containing phone number and password.
     * @return A success response if credentials are valid and OTP is sent.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponseLCVAPI_1> login(
        @Valid @RequestBody LoginRequestLCVAPI_1 loginRequest) {
        LoginSuccessResponseLCVAPI_1 response = authenticationService.loginWithCredentials(loginRequest);
        return ResponseEntity.ok(response);
    }
}
```

src/main/java/com/example/api/auth/service/IAuthenticationServiceLCVAPI_1.java
```java