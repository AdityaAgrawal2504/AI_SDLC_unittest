package com.example.auth.api;

import com.example.auth.dto.LoginRequest_LCVAPI_104;
import com.example.auth.dto.LoginSuccessResponse_LCVAPI_105;
import com.example.auth.service.IAuthenticationService_LCVAPI_117;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related API endpoints.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthenticationController_LCVAPI_119 {

    private final IAuthenticationService_LCVAPI_117 authenticationService;

    /**
     * Verifies user credentials and triggers an OTP on success.
     * @param loginRequest The request body containing the user's phone number and password.
     * @return A response entity with a success message.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponse_LCVAPI_105> login(@Valid @RequestBody LoginRequest_LCVAPI_104 loginRequest) {
        long startTime = System.currentTimeMillis();
        log.info("Received login request for phone number: {}", loginRequest.getPhoneNumber());

        LoginSuccessResponse_LCVAPI_105 response = authenticationService.loginWithCredentials(loginRequest);

        log.info("Login request for {} processed successfully. Total time: {}ms", loginRequest.getPhoneNumber(), System.currentTimeMillis() - startTime);
        return ResponseEntity.ok(response);
    }
}
src/main/java/com/example/auth/config/DataInitializer_LCVAPI_120.java