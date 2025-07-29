package com.example.service;

import com.example.model.User;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for user-related operations.
 */
public interface IUserService {
    /**
     * Finds a user by their unique ID.
     * @param id The UUID of the user.
     * @return An Optional containing the User if found.
     */
    Optional<User> findById(UUID id);

    /**
     * Finds a user by their unique phone number.
     * @param phoneNumber The phone number of the user.
     * @return An Optional containing the User if found.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
}