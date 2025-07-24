package com.example.controller;

import com.example.dto.UserRegistrationRequest;
import com.example.dto.UserResponse;
import com.example.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;

    /**
     * Handles POST /users/register
     * @param request The user registration request body.
     * @return The created user details.
     */
    @Operation(summary = "Registers a new user with a phone number and password.")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Handles GET /users/search
     * @param phoneNumber The phone number to search for.
     * @return A list of matching users.
     */
    @Operation(summary = "Searches for registered users by phone number.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(
            @RequestParam("phone_number")
            @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format.")
            String phoneNumber) {
        List<UserResponse> users = userService.searchUsersByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(users);
    }
}