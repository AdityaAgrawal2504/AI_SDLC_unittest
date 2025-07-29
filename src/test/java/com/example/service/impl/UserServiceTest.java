package com.example.service.impl;

import com.example.model.User;
import com.example.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private IUserRepository userRepository;
    @InjectMocks private UserService userService;

    private User user;
    private UUID userId = UUID.randomUUID();
    private String phoneNumber = "+1234567890";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setPhoneNumber(phoneNumber);
        user.setPasswordHash("hashedPassword");
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername(phoneNumber);
        assertThat(userDetails.getUsername()).isEqualTo(phoneNumber);
    }

    @Test
    void loadUserByUsername_whenUserDoesNotExist_shouldThrowException() {
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(phoneNumber);
        });
    }

    @Test
    void loadUserById_whenUserExists_shouldReturnUserDetails() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserById(userId);
        assertThat(((com.example.security.UserPrincipal) userDetails).getId()).isEqualTo(userId);
    }
}