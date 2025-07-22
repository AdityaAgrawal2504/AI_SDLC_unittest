package com.example.auth.initiate.api_IA_9F3E.controller;

import com.example.auth.initiate.api_IA_9F3E.constants.ErrorCode_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.ApiErrorResponseDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginRequestDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.dto.InitiateLoginResponseDTO_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.InvalidCredentialsException_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.OtpServiceException_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.exception.UserNotFoundException_IA_9F3E;
import com.example.auth.initiate.api_IA_9F3E.service.IAuthService_IA_9F3E;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController_IA_9F3E.class)
@Import(com.example.auth.initiate.api_IA_9F3E.exception.GlobalExceptionHandler_IA_9F3E.class)
class AuthController_IA_9F3ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthService_IA_9F3E authService;

    @Test
    void initiateLogin_whenSuccessful_shouldReturn200Ok() throws Exception {
        InitiateLoginRequestDTO_IA_9F3E request = new InitiateLoginRequestDTO_IA_9F3E("9876543210", "MyS3cur3P@ssw0rd!");
        String transactionId = UUID.randomUUID().toString();
        InitiateLoginResponseDTO_IA_9F3E response = InitiateLoginResponseDTO_IA_9F3E.builder()
                .success(true)
                .message("OTP sent successfully to your registered mobile number.")
                .transactionId(transactionId)
                .build();

        when(authService.initiateLogin(any(InitiateLoginRequestDTO_IA_9F3E.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("OTP sent successfully to your registered mobile number."))
                .andExpect(jsonPath("$.transactionId").value(transactionId));
    }

    @Test
    void initiateLogin_whenInvalidPhoneNumber_shouldReturn400BadRequest() throws Exception {
        InitiateLoginRequestDTO_IA_9F3E request = new InitiateLoginRequestDTO_IA_9F3E("123", "password");

        mockMvc.perform(post("/api/v1/auth/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.details.phoneNumber").value("Phone number must be exactly 10 digits."));
    }

    @Test
    void initiateLogin_whenUserNotFound_shouldReturn404NotFound() throws Exception {
        InitiateLoginRequestDTO_IA_9F3E request = new InitiateLoginRequestDTO_IA_9F3E("9876543210", "MyS3cur3P@ssw0rd!");

        when(authService.initiateLogin(any(InitiateLoginRequestDTO_IA_9F3E.class))).thenThrow(new UserNotFoundException_IA_9F3E());

        mockMvc.perform(post("/api/v1/auth/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_IA_9F3E.USER_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value("No user found with the provided phone number."));
    }

    @Test
    void initiateLogin_whenInvalidCredentials_shouldReturn401Unauthorized() throws Exception {
        InitiateLoginRequestDTO_IA_9F3E request = new InitiateLoginRequestDTO_IA_9F3E("9876543210", "wrongPassword");

        when(authService.initiateLogin(any(InitiateLoginRequestDTO_IA_9F3E.class))).thenThrow(new InvalidCredentialsException_IA_9F3E());

        mockMvc.perform(post("/api/v1/auth/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_IA_9F3E.INVALID_CREDENTIALS.name()))
                .andExpect(jsonPath("$.message").value("Invalid phone number or password."));
    }

    @Test
    void initiateLogin_whenOtpServiceFails_shouldReturn503ServiceUnavailable() throws Exception {
        InitiateLoginRequestDTO_IA_9F3E request = new InitiateLoginRequestDTO_IA_9F3E("9876543210", "MyS3cur3P@ssw0rd!");

        when(authService.initiateLogin(any(InitiateLoginRequestDTO_IA_9F3E.class))).thenThrow(new OtpServiceException_IA_9F3E());

        mockMvc.perform(post("/api/v1/auth/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode_IA_9F3E.OTP_SERVICE_UNAVAILABLE.name()))
                .andExpect(jsonPath("$.message").value("Failed to send OTP. Please try again in a few minutes."));
    }
}
```
```java