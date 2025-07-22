package com.example.controller;

import com.example.dto.ApiError_F4B8;
import com.example.dto.UserRegistrationRequest_F4B8;
import com.example.dto.UserRegistrationResponse_F4B8;
import com.example.service.IUserRegistrationService_F4B8;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller for handling user registration requests.
 *
 * <pre>
 * {@code
 * mermaid
 * sequenceDiagram
 *   Client->>+UserRegistrationController_F4B8: POST /users/register (JSON Request)
 *   UserRegistrationController_F4B8->>+IUserRegistrationService_F4B8: registerUser(request)
 *   IUserRegistrationService_F4B8->>+UserRepository_F4B8: findByPhoneNumber(phone)
 *   UserRepository_F4B8-->>-IUserRegistrationService_F4B8: Optional<User>
 *   alt User Exists
 *     IUserRegistrationService_F4B8-->>-UserRegistrationController_F4B8: throws UserAlreadyExistsException
 *     UserRegistrationController_F4B8->>-Client: HTTP 409 Conflict
 *   else User Does Not Exist
 *     IUserRegistrationService_F4B8->>IUserRegistrationService_F4B8: hashPassword(password)
 *     IUserRegistrationService_F4B8->>+UserRepository_F4B8: save(newUser)
 *     UserRepository_F4B8-->>-IUserRegistrationService_F4B8: Saved User
 *     IUserRegistrationService_F4B8-->>-UserRegistrationController_F4B8: UserRegistrationResponse
 *     UserRegistrationController_F4B8->>-Client: HTTP 201 Created (JSON Response)
 *   end
 * }
 * </pre>
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserRegistrationController_F4B8 {

    private final IUserRegistrationService_F4B8 userRegistrationService;

    public UserRegistrationController_F4B8(IUserRegistrationService_F4B8 userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    /**
     * Registers a new user based on the provided phone number and password.
     * @param request The valid user registration request.
     * @return A ResponseEntity containing the user's new ID upon successful creation.
     */
    @Operation(
        summary = "Register a new user",
        description = "Registers a new user with a 10-digit phone number and a secure password. A successful registration creates a user record and returns a unique user identifier."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRegistrationResponse_F4B8.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request. The request body is malformed or fails validation.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError_F4B8.class))),
        @ApiResponse(responseCode = "409", description = "Conflict. A user with the provided phone number already exists.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError_F4B8.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error. An unexpected error occurred on the server.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError_F4B8.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse_F4B8> registerUser(@Valid @RequestBody UserRegistrationRequest_F4B8 request) {
        UserRegistrationResponse_F4B8 response = userRegistrationService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
```
```java
src/test/java/com/example/ApplicationTest.java