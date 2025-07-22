package com.example.controller;

import com.example.dto.UserRegistrationRequest_F4B8;
import com.example.dto.UserRegistrationResponse_F4B8;
import com.example.exception.PasswordHashingException_F4B8;
import com.example.exception.UserAlreadyExistsException_F4B8;
import com.example.service.IUserRegistrationService_F4B8;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRegistrationController_F4B8.class)
class UserRegistrationController_F4B8Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserRegistrationService_F4B8 userRegistrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_Success() throws Exception {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "SecurePass123");
        UserRegistrationResponse_F4B8 response = new UserRegistrationResponse_F4B8(UUID.randomUUID().toString(), "User registered successfully.");

        when(userRegistrationService.registerUser(any(UserRegistrationRequest_F4B8.class))).thenReturn(response);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(response.getUserId()))
                .andExpect(jsonPath("$.message").value(response.getMessage()));
    }

    @Test
    void registerUser_ValidationFailure_PhoneNumberBlank() throws Exception {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("", "SecurePass123");

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed."))
                .andExpect(jsonPath("$.details.phoneNumber").value("Phone number must be provided."));
    }

    @Test
    void registerUser_ValidationFailure_PasswordTooShort() throws Exception {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "short");

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.password").value("Password must be at least 8 characters long."));
    }

    @Test
    void registerUser_UserAlreadyExists() throws Exception {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "SecurePass123");
        String errorMessage = "A user with the phone number 1234567890 already exists.";

        when(userRegistrationService.registerUser(any(UserRegistrationRequest_F4B8.class)))
                .thenThrow(new UserAlreadyExistsException_F4B8(errorMessage));

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("USER_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    void registerUser_PasswordHashingFailure() throws Exception {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "SecurePass123");

        when(userRegistrationService.registerUser(any(UserRegistrationRequest_F4B8.class)))
                .thenThrow(new PasswordHashingException_F4B8("Failed to hash.", new RuntimeException()));

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("PASSWORD_HASHING_FAILURE"))
                .andExpect(jsonPath("$.message").value("Could not process registration due to a security configuration error."));
    }

    @Test
    void registerUser_InternalServerError() throws Exception {
        UserRegistrationRequest_F4B8 request = new UserRegistrationRequest_F4B8("1234567890", "SecurePass123");

        when(userRegistrationService.registerUser(any(UserRegistrationRequest_F4B8.class)))
                .thenThrow(new RuntimeException("Some unexpected error"));

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("UNEXPECTED_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected internal server error occurred."));
    }
}
```