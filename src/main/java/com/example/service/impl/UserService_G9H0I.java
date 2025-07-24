src/main/java/com/example/service/impl/UserService_G9H0I.java

<ctrl60>
package com.example.service.impl;

import com.example.dto.CreateUserDto_A1B2C;
import com.example.dto.UserDto_D3E4F;
import com.example.exception.ConflictException_Z1Y2X;
import com.example.exception.ResourceNotFoundException_T5S6R;
import com.example.mapper.UserMapper_U1V2W;
import com.example.model.User_M1N2O;
import com.example.provider.interfaces.IPasswordHasher_A1B2C;
import com.example.repository.IUserRepository_M9N8O;
import com.example.service.interfaces.IUserService_D7E8F;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service implementation for user management.
 */
@Service
@RequiredArgsConstructor
public class UserService_G9H0I implements IUserService_D7E8F {

    private final IUserRepository_M9N8O userRepository;
    private final IPasswordHasher_A1B2C passwordHasher;
    private final UserMapper_U1V2W userMapper;

    /**
     * Creates a new user.
     * @param createUserDto DTO containing user creation data.
     * @return DTO of the newly created user.
     * @throws ConflictException_Z1Y2X if a user with the phone number already exists.
     */
    @Override
    @Transactional
    public UserDto_D3E4F createUser(CreateUserDto_A1B2C createUserDto) {
        if (userRepository.existsByPhoneNumber(createUserDto.getPhoneNumber())) {
            throw new ConflictException_Z1Y2X("User with phone number " + createUserDto.getPhoneNumber() + " already exists.");
        }

        User_M1N2O newUser = new User_M1N2O();
        newUser.setPhoneNumber(createUserDto.getPhoneNumber());
        newUser.setPassword(passwordHasher.hash(createUserDto.getPassword()));

        User_M1N2O savedUser = userRepository.save(newUser);
        return userMapper.toDto(savedUser);
    }

    /**
     * Finds a user by their phone number.
     * @param phoneNumber The user's phone number.
     * @return The User entity.
     * @throws ResourceNotFoundException_T5S6R if no user is found.
     */
    @Override
    public User_M1N2O findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException_T5S6R("User not found with phone number: " + phoneNumber));
    }
    
    /**
     * Finds a user by their ID.
     * @param id The user's UUID.
     * @return The User entity.
     * @throws ResourceNotFoundException_T5S6R if no user is found.
     */
    @Override
    public User_M1N2O findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException_T5S6R("User not found with ID: " + id));
    }
}