package com.example.service;

import com.example.dto.UserRegistrationRequest;
import com.example.dto.UserResponse;
import com.example.exception.DuplicateResourceException;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <!--
 * mermaid
 *  sequenceDiagram
 *      UserController->>+IUserService: registerUser(dto)
 *      IUserService->>+UserRepository: existsByPhoneNumber(phone)
 *      UserRepository-->>-IUserService: boolean
 *      IUserService->>+IPasswordService: hashPassword(pass)
 *      IPasswordService-->>-IUserService: hashedPassword
 *      IUserService->>+UserRepository: save(user)
 *      UserRepository-->>-IUserService: savedUser
 *      IUserService-->>-UserController: UserResponse
 * -->
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final IPasswordService passwordService;

    /**
     * Registers a new user in the system.
     * @param request DTO containing user registration details.
     * @return DTO containing details of the created user.
     * @throws DuplicateResourceException if a user with the phone number already exists.
     */
    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("User with phone number " + request.getPhoneNumber() + " already exists.");
        }

        User newUser = User.builder()
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordService.hashPassword(request.getPassword()))
                .build();

        User savedUser = userRepository.save(newUser);

        return mapUserToResponse(savedUser);
    }

    /**
     * Searches for users by a partial phone number.
     * @param phoneNumber The phone number search query.
     * @return A list of matching users.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsersByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumberContainingIgnoreCase(phoneNumber).stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }
}