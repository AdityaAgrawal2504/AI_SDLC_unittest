package com.auth.api.controller;

import com.auth.api.dto.UserRegAPI_UserRegistrationRequest_33A1;
import com.auth.api.dto.UserRegAPI_UserRegistrationResponse_33A1;
import com.auth.api.enums.UserRegAPI_RegistrationStatus_33A1;
import com.auth.api.exception.UserRegAPI_ConflictException_33A1;
import com.auth.api.service.UserRegAPI_AuthService_33A1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.auth.api.exception.UserRegAPI_GlobalExceptionHandler_33A1;


import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRegAPI_AuthController_33A1.class)
@Import(UserRegAPI_GlobalExceptionHandler_33A1.class) // Import global handler to test error responses
class UserRegAPI_AuthController_33A1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRegAPI_AuthService_33A1 authService;

    /**
     * Positive test case: Verifies successful user registration returns 201 Created.
     */
    @Test
    void registerUser_whenValidRequest_shouldReturn201Created() throws Exception {
        // Arrange
        UserRegAPI_UserRegistrationRequest_33A1 request = new UserRegAPI_UserRegistrationRequest_33A1("+14155552671", "P@ssw0rd123!");
        UserRegAPI_UserRegistrationResponse_33A1 response = new UserRegAPI_UserRegistrationResponse_33A1(UUID.randomUUID(), UserRegAPI_RegistrationStatus_33A1.REGISTRATION_SUCCESSFUL);

        when(authService.register(any(UserRegAPI_UserRegistrationRequest_33A1.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(response.getUserId().toString()))
                .andExpect(jsonPath("$.status").value("REGISTRATION_SUCCESSFUL"));
    }

    /**
     * Negative test case: Verifies invalid phone number format returns 400 Bad Request.
     */
    @Test
    void registerUser_whenInvalidPhoneNumber_shouldReturn400BadRequest() throws Exception {
        // Arrange
        UserRegAPI_UserRegistrationRequest_33A1 request = new UserRegAPI_UserRegistrationRequest_33A1("12345", "P@ssw0rd123!");

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.phoneNumber").exists());
    }

    /**
     * Negative test case: Verifies weak password returns 400 Bad Request.
     */
    @Test
    void registerUser_whenWeakPassword_shouldReturn400BadRequest() throws Exception {
        // Arrange
        UserRegAPI_UserRegistrationRequest_33A1 request = new UserRegAPI_UserRegistrationRequest_33A1("+14155552671", "weak");

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.password").exists());
    }

    /**
     * Negative test case: Verifies that a conflict (user exists) returns 409 Conflict.
     */
    @Test
    void registerUser_whenUserExists_shouldReturn409Conflict() throws Exception {
        // Arrange
        UserRegAPI_UserRegistrationRequest_33A1 request = new UserRegAPI_UserRegistrationRequest_33A1("+14155552671", "P@ssw0rd123!");
        when(authService.register(any(UserRegAPI_UserRegistrationRequest_33A1.class)))
            .thenThrow(new UserRegAPI_ConflictException_33A1("A user with this phone number already exists."));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("USER_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("A user with this phone number already exists."));
    }
}
```