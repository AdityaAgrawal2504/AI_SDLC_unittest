package com.example.auth.initiate.api_IA_9F3E.service;

import com.example.auth.initiate.api_IA_9F3E.entity.User_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.repository.UserRepository_IA_9F3E;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImpl_IA_9F3ETest {

    @Mock
    private UserRepository_IA_9F3E userRepository;

    @InjectMocks
    private UserServiceImpl_IA_9F3E userService;

    @Test
    void findByPhoneNumber_whenUserExists_shouldReturnUser() {
        String phoneNumber = "1234567890";
        User_IA_9F3E user = User_IA_9F3E.builder().phoneNumber(phoneNumber).build();
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(user));

        Optional<User_IA_9F3E> foundUser = userService.findByPhoneNumber(phoneNumber);

        assertTrue(foundUser.isPresent());
        assertEquals(phoneNumber, foundUser.get().getPhoneNumber());
    }

    @Test
    void findByPhoneNumber_whenUserDoesNotExist_shouldReturnEmptyOptional() {
        String phoneNumber = "1234567890";
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        Optional<User_IA_9F3E> foundUser = userService.findByPhoneNumber(phoneNumber);

        assertTrue(foundUser.isEmpty());
    }
}
```
```java