package com.yourorg.userregistration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourorg.userregistration.dto.UserRegistrationRequestURAPI_1201;
import com.yourorg.userregistration.dto.UserRegistrationResponseURAPI_1201;
import com.yourorg.userregistration.exception.UserAlreadyExistsExceptionURAPI_1201;
import com.yourorg.userregistration.service.IAuthServiceURAPI_1201;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthControllerURAPI_1201.class)
class AuthControllerURAPI_1201Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthServiceURAPI_1201 authService;

    private UserRegistrationRequestURAPI_1201 validRequest;
    private UserRegistrationRequestURAPI_1201 invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new UserRegistrationRequestURAPI_1201();
        validRequest.setPhoneNumber("+14155552671");
        validRequest.setPassword("P@ssw0rd123!");

        invalidRequest = new UserRegistrationRequestURAPI_1201();
        invalidRequest.setPhoneNumber("12345");
        invalidRequest.setPassword("short");
    }

    /**
     * Tests the POST /v1/auth/register endpoint for a successful registration.
     */
    @Test
    void registerUser_withValidRequest_shouldReturn201Created() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserRegistrationResponseURAPI_1201 response = new UserRegistrationResponseURAPI_1201(userId, "REGISTRATION_SUCCESSFUL");
        when(authService.register(any(UserRegistrationRequestURAPI_1201.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(userId.toString())))
                .andExpect(jsonPath("$.status", is("REGISTRATION_SUCCESSFUL")));
    }

    /**
     * Tests the endpoint for invalid input, expecting a 400 Bad Request.
     */
    @Test
    void registerUser_withInvalidRequest_shouldReturn400BadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details.phoneNumber").exists())
                .andExpect(jsonPath("$.details.password").exists());
    }

    /**
     * Tests the endpoint when a user already exists, expecting a 409 Conflict.
     */
    @Test
    void registerUser_whenUserExists_shouldReturn409Conflict() throws Exception {
        // Arrange
        when(authService.register(any(UserRegistrationRequestURAPI_1201.class)))
                .thenThrow(new UserAlreadyExistsExceptionURAPI_1201(validRequest.getPhoneNumber()));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode", is("USER_ALREADY_EXISTS")));
    }

    /**
     * Tests the endpoint for an unexpected internal error, expecting a 500 Internal Server Error.
     */
    @Test
    void registerUser_whenInternalErrorOccurs_shouldReturn500InternalServerError() throws Exception {
        // Arrange
        when(authService.register(any(UserRegistrationRequestURAPI_1201.class)))
                .thenThrow(new RuntimeException("Unexpected DB error"));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is("INTERNAL_SERVER_ERROR")));
    }
}
```