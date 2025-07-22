package com.example.userregistration.controller;

import com.example.userregistration.dto.UserRegistrationRequest_URAPI_1;
import com.example.userregistration.dto.UserRegistrationResponse_URAPI_1;
import com.example.userregistration.service.IUserRegistrationService_URAPI_1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user registration requests.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRegistrationController_URAPI_1 {

    private final IUserRegistrationService_URAPI_1 userRegistrationService;

    /**
     * Handles the POST request to register a new user.
     *
     * @param request The request body containing user registration details.
     * @return A ResponseEntity with the registration response and a 201 Created status on success.
     */
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse_URAPI_1> registerUser(
            @Valid @RequestBody UserRegistrationRequest_URAPI_1 request) {
        
        UserRegistrationResponse_URAPI_1 response = userRegistrationService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
```
src/main/java/com/example/userregistration/exception/GlobalExceptionHandler_URAPI_1.java
```java