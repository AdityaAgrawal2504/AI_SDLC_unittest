package com.verifyotpapi.controller;

import com.verifyotpapi.dto.request.VerifyOtpRequest_VOTP1;
import com.verifyotpapi.dto.response.VerifyOtpSuccessResponse_VOTP1;
import com.verifyotpapi.service.IAuthService_VOTP1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related API endpoints.
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController_VOTP1 {

    private final IAuthService_VOTP1 authService;

    /**
     * Handles the POST request to verify a user's OTP.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpSuccessResponse_VOTP1> verifyOtp(@Valid @RequestBody VerifyOtpRequest_VOTP1 request) {
        VerifyOtpSuccessResponse_VOTP1 response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }
}
```
```java
// src/test/java/com/verifyotpapi/service/AuthService_VOTP1Test.java