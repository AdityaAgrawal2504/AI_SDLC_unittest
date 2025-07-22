package com.example.service;

import com.example.dto.UserRegistrationRequest_UATH_1006;
import com.example.dto.UserRegistrationResponse_UATH_1007;
import com.example.entity.UserEntity_UATH_1016;
import com.example.exception.UserAlreadyExistsException_UATH_1010;
import com.example.repository.UserRepository_UATH_1017;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserService_UATH_1009Test {

    @Mock
    private UserRepository_UATH_1017 userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService_UATH_1009 userService;

    @Test
    void registerUser_whenPhoneNumberIsNew_shouldSucceed() {
        UserRegistrationRequest_UATH_1006 request = new UserRegistrationRequest_UATH_1006("1112223333", "Password@123");
        UserEntity_UATH_1016 savedUser = new UserEntity_UATH_1016(request.phoneNumber(), "encodedPassword");

        when(userRepository.existsByPhoneNumber(request.phoneNumber())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity_UATH_1016.class))).thenReturn(savedUser);

        UserRegistrationResponse_UATH_1007 response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals("User registered successfully.", response.message());
    }

    @Test
    void registerUser_whenPhoneNumberExists_shouldThrowException() {
        UserRegistrationRequest_UATH_1006 request = new UserRegistrationRequest_UATH_1006("1112223333", "Password@123");

        when(userRepository.existsByPhoneNumber(request.phoneNumber())).thenReturn(true);

        assertThrows(UserAlreadyExistsException_UATH_1010.class, () -> {
            userService.registerUser(request);
        });
    }
}
```
```java
//
// Filename: src/test/java/com/example/service/OtpService_UATH_1018Test.java
//