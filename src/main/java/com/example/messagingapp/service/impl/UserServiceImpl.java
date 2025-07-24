package com.example.messagingapp.service.impl;

import com.example.messagingapp.dto.UserRegistrationRequest;
import com.example.messagingapp.dto.UserResponse;
import com.example.messagingapp.exception.ApiException;
import com.example.messagingapp.model.User;
import com.example.messagingapp.repository.UserRepository;
import com.example.messagingapp.service.UserService;
import com.example.messagingapp.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user, checking for existing phone numbers and hashing the password.
     * @param request DTO containing registration details.
     * @return The newly created user's response DTO.
     */
    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        userRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(u -> {
            throw new ApiException(HttpStatus.CONFLICT, "PHONE_NUMBER_ALREADY_EXISTS",
                    "A user account with the provided phone number already exists.");
        });

        User user = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        return DtoMapper.toUserResponse(savedUser);
    }

    /**
     * Finds a user by their unique ID.
     * @param id The UUID of the user.
     * @return An Optional containing the user if found.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The phone number of the user.
     * @return An Optional containing the user if found.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    /**
     * Searches for users whose phone number contains the query string.
     * @param query The search string for the phone number.
     * @return A list of matching user response DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String query) {
        List<User> users = userRepository.findByPhoneNumberContaining(query);
        return users.stream()
                .map(DtoMapper::toUserResponse)
                .collect(Collectors.toList());
    }
}