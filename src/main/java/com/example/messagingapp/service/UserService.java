package com.example.messagingapp.service;

import com.example.messagingapp.dto.UserRegistrationRequest;
import com.example.messagingapp.dto.UserResponse;
import com.example.messagingapp.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    /**
     * Registers a new user in the system.
     * @param request DTO containing registration details.
     * @return The newly created user's response DTO.
     */
    UserResponse registerUser(UserRegistrationRequest request);

    /**
     * Finds a user by their unique ID.
     * @param id The UUID of the user.
     * @return An Optional containing the user if found.
     */
    Optional<User> findById(UUID id);

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number of the user.
     * @return An Optional containing the user if found.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Searches for users based on a phone number query.
     * @param query The search string for the phone number.
     * @return A list of matching user response DTOs.
     */
    List<UserResponse> searchUsers(String query);
}