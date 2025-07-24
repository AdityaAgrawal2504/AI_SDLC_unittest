src/main/java/com/example/controller/UserController_A1B2C.java
package com.example.controller;

import com.example.dto.CreateUserDto_A1B2C;
import com.example.dto.UserDto_D3E4F;
import com.example.service.interfaces.IUserService_D7E8F;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user registration.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController_A1B2C {

    private final IUserService_D7E8F userService;

    /**
     * Handles POST /users to create a new user account.
     * @param createUserDto The request body containing user details.
     * @return The created user's information.
     */
    @PostMapping
    public ResponseEntity<UserDto_D3E4F> createUser(@Valid @RequestBody CreateUserDto_A1B2C createUserDto) {
        UserDto_D3E4F createdUser = userService.createUser(createUserDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}