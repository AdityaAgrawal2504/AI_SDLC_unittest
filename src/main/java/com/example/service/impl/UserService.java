package com.example.service.impl;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.repository.IUserRepository;
import com.example.security.UserPrincipal;
import com.example.service.IUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by their ID.
     * @param id The user's UUID.
     * @return An Optional containing the User if found.
     */
    @Override
    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The user's phone number.
     * @return An Optional containing the User if found.
     */
    @Override
    public Optional<User> findUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
    
    /**
     * Loads a user by their username (phone number for this app) for Spring Security.
     * @param username The phone number.
     * @return UserDetails object.
     * @throws UsernameNotFoundException if user not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + username));
        return UserPrincipal.create(user);
    }

    /**
     * Loads a user by their UUID for JWT authentication.
     * @param id The user's UUID.
     * @return UserDetails object.
     */
    @Override
    public UserDetails loadUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return UserPrincipal.create(user);
    }
}