package com.example.auth.initiate.api_IA_9F3E.service;

import com.example.auth.initiate.api_IA_9F3E.constants.UserStatus_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginRequestDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginResponseDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.entity.User_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.AccountLockedException_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.InvalidCredentialsException_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.UserNotFoundException_IA_9F3E;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of the IAuthService, orchestrating the login initiation process.
 */
@Service
public class AuthServiceImpl_IA_9F3E implements IAuthService_IA_9F3E {

    private final IUserService_IA_9F3E userService;
    private final IPasswordService_IA_9F3E passwordService;
    private final IOtpService_IA_9F3E otpService;

    public AuthServiceImpl_IA_9F3E(IUserService_IA_9F3E userService, IPasswordService_IA_9F3E passwordService, IOtpService_IA_9F3E otpService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.otpService = otpService;
    }

    /**
     * Orchestrates the login initiation flow.
     * @param request The DTO containing the user's phone number and password.
     * @return A DTO with the transaction ID for the next step.
     */
    @Override
    public InitiateLoginResponseDTO_IA_9F3E initiateLogin(InitiateLoginRequestDTO_IA_9F3E request) {
        // 1. Find the user by phone number
        User_IA_9F3E user = userService.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(UserNotFoundException_IA_9F3E::new);

        // 2. Check if the account is locked
        if (user.getStatus() == UserStatus_IA_9F3E.LOCKED) {
            throw new AccountLockedException_IA_9F3E();
        }

        // 3. Verify the password
        boolean passwordMatches = passwordService.verifyPassword(request.getPassword(), user.getPasswordHash());
        if (!passwordMatches) {
            // Note: In a real app, you might increment a failed attempt counter here
            throw new InvalidCredentialsException_IA_9F3E();
        }

        // 4. Generate a transaction ID
        String transactionId = UUID.randomUUID().toString();

        // 5. Send the OTP
        otpService.sendLoginOtp(user.getPhoneNumber(), transactionId);
        
        // 6. Return successful response
        return InitiateLoginResponseDTO_IA_9F3E.builder()
                .success(true)
                .message("OTP sent successfully to your registered mobile number.")
                .transactionId(transactionId)
                .build();
    }
}
```
```java