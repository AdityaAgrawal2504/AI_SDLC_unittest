src/test/java/com/example/service/impl/UserService_G9H0ITest.java
package com.example.service.impl;

import com.example.dto.CreateUserDto_A1B2C;
import com.example.dto.UserDto_D3E4F;
import com.example.exception.ConflictException_Z1Y2X;
import com.example.exception.ResourceNotFoundException_T5S6R;
import com.example.mapper.UserMapper_U1V2W;
import com.example.model.User_M1N2O;
import com.example.provider.interfaces.IPasswordHasher_A1B2C;
import com.example.repository.IUserRepository_M9N8O;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserService_G9H0ITest {

    @Mock
    private IUserRepository_M9N8O userRepository;
    @Mock
    private IPasswordHasher_A1B2C passwordHasher;
    @Mock
    private UserMapper_U1V2W userMapper;

    @InjectMocks
    private UserService_G9H0I userService;

    private CreateUserDto_A1B2C createUserDto;
    private User_M1N2O user;
    private UserDto_D3E4F userDto;

    @BeforeEach
    void setUp() {
        createUserDto = new CreateUserDto_A1B2C("+15551234567", "Password123");
        user = new User_M1N2O();
        user.setId(UUID.randomUUID());
        user.setPhoneNumber(createUserDto.getPhoneNumber());
        user.setPassword("hashedPassword");
        user.setCreatedAt(LocalDateTime.now());

        userDto = new UserDto_D3E4F(user.getId(), user.getPhoneNumber(), user.getCreatedAt());
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(passwordHasher.hash(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User_M1N2O.class))).thenReturn(user);
        when(userMapper.toDto(any(User_M1N2O.class))).thenReturn(userDto);

        UserDto_D3E4F result = userService.createUser(createUserDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(createUserDto.getPhoneNumber(), result.getPhoneNumber());
        verify(userRepository, times(1)).existsByPhoneNumber(createUserDto.getPhoneNumber());
        verify(userRepository, times(1)).save(any(User_M1N2O.class));
    }

    @Test
    void createUser_Conflict_PhoneNumberExists() {
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        assertThrows(ConflictException_Z1Y2X.class, () -> userService.createUser(createUserDto));

        verify(userRepository, times(1)).existsByPhoneNumber(createUserDto.getPhoneNumber());
        verify(userRepository, never()).save(any(User_M1N2O.class));
    }

    @Test
    void findByPhoneNumber_Success() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.of(user));

        User_M1N2O foundUser = userService.findByPhoneNumber(createUserDto.getPhoneNumber());

        assertNotNull(foundUser);
        assertEquals(user.getPhoneNumber(), foundUser.getPhoneNumber());
        verify(userRepository, times(1)).findByPhoneNumber(createUserDto.getPhoneNumber());
    }

    @Test
    void findByPhoneNumber_NotFound() {
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException_T5S6R.class, () -> userService.findByPhoneNumber("nonexistent_number"));

        verify(userRepository, times(1)).findByPhoneNumber("nonexistent_number");
    }
    
    @Test
    void findById_Success() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        User_M1N2O foundUser = userService.findById(userId);

        assertNotNull(foundUser);
        verify(userRepository, times(1)).findById(userId);
    }
    
    @Test
    void findById_NotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException_T5S6R.class, () -> userService.findById(userId));

        verify(userRepository, times(1)).findById(userId);
    }
}