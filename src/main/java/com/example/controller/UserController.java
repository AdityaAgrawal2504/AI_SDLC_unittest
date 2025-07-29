src/main/java/com/example/controller/UserController.java
package com.example.controller;

import com.example.dto.UserDto;
import com.example.dto.UserRegistrationDto;
import com.example.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Handles '/users' and '/users/search' for registration and user lookup.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     * @param userRegistrationDto DTO containing user registration details.
     * @return The created user's DTO.
     */
    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        UserDto newUser = userService.registerUser(userRegistrationDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Searches for a user by their phone number.
     * @param phoneNumber The phone number to search for.
     * @return A list of users matching the phone number.
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String phoneNumber) {
        List<UserDto> users = userService.searchUsersByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(users);
    }
}