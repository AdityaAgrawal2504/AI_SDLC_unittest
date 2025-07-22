package com.example.userregistration.controller;

import com.example.userregistration.dto.ApiError_URAPI_1;
import com.example.userregistration.dto.UserRegistrationRequest_URAPI_1;
import com.example.userregistration.dto.UserRegistrationResponse_URAPI_1;
import com.example.userregistration.exception.GlobalExceptionHandler_URAPI_1;
import com.example.userregistration.exception.PasswordHashingException_URAPI_1;
import com.example.userregistration.exception.UserAlreadyExistsException_URAPI_1;
import com.example.userregistration.model.ErrorCode_URAPI_1;
import com.example.userregistration.service.IUserRegistrationService_URAPI_1;
import com.example.userregistration.logging.StructuredLogger_URAPI_1; // Import StructuredLogger
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRegistrationController_URAPI_1.class)
class UserRegistrationController_URAPI_1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserRegistrationService_URAPI_1 userRegistrationService;

    @MockBean // Mock StructuredLogger as it's a dependency of GlobalExceptionHandler
    private StructuredLogger_URAPI_1 structuredLogger;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // If GlobalExceptionHandler is not directly part of the @WebMvcTest context,
        // it might need to be explicitly set up. However, @WebMvcTest usually
        // auto-configures @ControllerAdvice classes.
        // For robustness, sometimes we explicitly add it:
        mockMvc = MockMvcBuilders.standaloneSetup(new UserRegistrationController_URAPI_1(userRegistrationService))
                .setControllerAdvice(new GlobalExceptionHandler_URAPI_1(structuredLogger)) // Pass the mock structuredLogger
                .build();
    }

    @Test
    @DisplayName("Should register a user successfully and return 201 Created")
    void registerUser_Success() throws Exception {
        UserRegistrationRequest_URAPI_1 request = new UserRegistrationRequest_URAPI_1();
        request.setPhoneNumber("1234567890");
        request.setPassword("StrongPass123");

        UserRegistrationResponse_URAPI_1 serviceResponse = UserRegistrationResponse_URAPI_1.builder()
                .userId(UUID.randomUUID().toString())
                .message("User registered successfully.")
                .build();

        when(userRegistrationService.registerUser(any(UserRegistrationRequest_URAPI_1.class)))
                .thenReturn(serviceResponse);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.message").value("User registered successfully."));

        verify(userRegistrationService, times(1)).registerUser(any(UserRegistrationRequest_URAPI_1.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid phone number (too short)")
    void registerUser_InvalidPhoneNumberLength() throws Exception {
        UserRegistrationRequest_URAPI_1 request = new UserRegistrationRequest_URAPI_1();
        request.setPhoneNumber("12345"); // Too short
        request.setPassword("StrongPass123");

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_URAPI_1.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.message").value("Input validation failed."))
                .andExpect(jsonPath("$.details.phoneNumber").value("Phone number must be exactly 10 digits."));

        verify(userRegistrationService, never()).registerUser(any(UserRegistrationRequest_URAPI_1.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for blank password")
    void registerUser_BlankPassword() throws Exception {
        UserRegistrationRequest_URAPI_1 request = new UserRegistrationRequest_URAPI_1();
        request.setPhoneNumber("1234567890");
        request.setPassword(""); // Blank password

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_URAPI_1.VALIDATION_ERROR.name()))
                .andExpect(jsonPath("$.message").value("Input validation failed."))
                .andExpect(jsonPath("$.details.password").value("Password must be provided."));

        verify(userRegistrationService, never()).registerUser(any(UserRegistrationRequest_URAPI_1.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict if user already exists")
    void registerUser_UserAlreadyExists() throws Exception {
        UserRegistrationRequest_URAPI_1 request = new UserRegistrationRequest_URAPI_1();
        request.setPhoneNumber("1234567890");
        request.setPassword("StrongPass123");

        when(userRegistrationService.registerUser(any(UserRegistrationRequest_URAPI_1.class)))
                .thenThrow(new UserAlreadyExistsException_URAPI_1("User with this phone number already exists."));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_URAPI_1.USER_ALREADY_EXISTS.name()))
                .andExpect(jsonPath("$.message").value("User with this phone number already exists."));

        verify(userRegistrationService, times(1)).registerUser(any(UserRegistrationRequest_URAPI_1.class));
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for password hashing failure")
    void registerUser_PasswordHashingFailure() throws Exception {
        UserRegistrationRequest_URAPI_1 request = new UserRegistrationRequest_URAPI_1();
        request.setPhoneNumber("1234567890");
        request.setPassword("StrongPass123");

        when(userRegistrationService.registerUser(any(UserRegistrationRequest_URAPI_1.class)))
                .thenThrow(new PasswordHashingException_URAPI_1("Failed to hash password", new RuntimeException()));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_URAPI_1.PASSWORD_HASHING_FAILURE.name()))
                .andExpect(jsonPath("$.message").value("An internal security error occurred."));

        verify(userRegistrationService, times(1)).registerUser(any(UserRegistrationRequest_URAPI_1.class));
        verify(structuredLogger, times(1)).error(eq("Password Hashing Failure"), any(Throwable.class), anyMap());
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for generic unexpected exception")
    void registerUser_GenericException() throws Exception {
        UserRegistrationRequest_URAPI_1 request = new UserRegistrationRequest_URAPI_1();
        request.setPhoneNumber("1234567890");
        request.setPassword("StrongPass123");

        when(userRegistrationService.registerUser(any(UserRegistrationRequest_URAPI_1.class)))
                .thenThrow(new RuntimeException("Something unexpected happened"));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_URAPI_1.UNEXPECTED_ERROR.name()))
                .andExpect(jsonPath("$.message").value("An unexpected internal server error occurred."));

        verify(userRegistrationService, times(1)).registerUser(any(UserRegistrationRequest_URAPI_1.class));
        verify(structuredLogger, times(1)).error(eq("Unexpected Generic Exception"), any(Throwable.class), anyMap());
    }
}
```