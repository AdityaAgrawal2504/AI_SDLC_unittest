package com.example.service;

import com.example.model.User;
import com.example.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_shouldCallRepository() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));
        assertTrue(userService.findById(id).isPresent());
        verify(userRepository).findById(id);
    }

    @Test
    void findByPhoneNumber_shouldCallRepository() {
        String phone = "+123";
        when(userRepository.findByPhoneNumber(phone)).thenReturn(Optional.of(new User()));
        assertTrue(userService.findByPhoneNumber(phone).isPresent());
        verify(userRepository).findByPhoneNumber(phone);
    }
}