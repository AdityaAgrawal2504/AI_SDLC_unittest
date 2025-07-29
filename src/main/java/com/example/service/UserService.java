package com.example.service;

import com.example.model.User;
import com.example.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Implements user data retrieval operations.
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    /**
     * Finds a user by their ID.
     * @param id The UUID of the user.
     * @return An Optional containing the User if found.
     */
    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number of the user.
     * @return An Optional containing the User if found.
     */
    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}