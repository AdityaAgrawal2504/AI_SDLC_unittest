package com.example.messagingapp.controller;

import com.example.messagingapp.dto.UserRegistrationRequest;
import com.example.messagingapp.dto.UserResponse;
import com.example.messagingapp.logging.Loggable;
import com.example.messagingapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Registers a new user account.
     * @param request The user registration request DTO.
     * @return The created user's details.
     */
    @PostMapping("/register")
    @Loggable
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse userResponse = userService.registerUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    /**
     * Searches for users by their phone number.
     * @param query The search query (part of a phone number).
     * @return A list of matching users.
     */
    @GetMapping("/search")
    @Loggable
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam("q") String query) {
        List<UserResponse> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }
}