package com.yourorg.verifyotp.controller;

import com.yourorg.verifyotp.dto.VerifyOtpRequestVAPI_1;
import com.yourorg.verifyotp.dto.VerifyOtpSuccessResponseVAPI_1;
import com.yourorg.verifyotp.service.IAuthServiceVAPI_1;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthControllerVAPI_1 {

    private final IAuthServiceVAPI_1 authService;

    public AuthControllerVAPI_1(IAuthServiceVAPI_1 authService) {
        this.authService = authService;
    }

    /**
     * Endpoint to verify a user-submitted OTP.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpSuccessResponseVAPI_1> verifyOtp(@Valid @RequestBody VerifyOtpRequestVAPI_1 request) {
        VerifyOtpSuccessResponseVAPI_1 response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }
}
```
```java
// AOP Logging Aspect: LoggingAspectVAPI_1.java