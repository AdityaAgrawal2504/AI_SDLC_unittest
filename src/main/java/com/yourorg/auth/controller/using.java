package com.yourorg.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourorg.auth.dto.request.LoginRequest_LROA938;
import com.yourorg.auth.dto.response.LoginSuccessResponse_LROA938;
import com.yourorg.auth.enums.ErrorCode_LROA938;
import com.yourorg.auth.exception.ApplicationException_LROA938;
import com.yourorg.auth.service.AuthenticationService_LROA938;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.yourorg.auth.config.SecurityConfig_LROA938;
import com.yourorg.auth.exception.GlobalExceptionHandler_LROA938;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the LoginController class using WebMvcTest.
 */
@WebMvcTest(LoginController_LROA938.class)
@Import({SecurityConfig_LROA938.class, GlobalExceptionHandler_LROA938.class})
class LoginController_LROA938Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService_LROA938 authenticationService;

    /**
     * Tests the positive case where login is successful and returns 200 OK.
     */
    @Test
    void loginAndRequestOtp_Success_Returns200() throws Exception {
        // Arrange
        LoginRequest_LROA938 request = new LoginRequest_LROA938("+14155552671", "MyS3cur3P@ssw0rd!");
        LoginSuccessResponse_LROA938 successResponse = LoginSuccessResponse_LROA938.builder()
                .status("success")
                .message("OTP has been sent successfully. Please check your device.")
                .otpSessionToken("a-valid-jwt")
                .build();

        when(authenticationService.loginAndRequestOtp(any(LoginRequest_LROA938.class))).thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.otpSessionToken").value("a-valid-jwt"));
    }

    /**
     * Tests validation failure for a phone number with an invalid format, expecting 400 Bad Request.
     */
    @Test
    void loginAndRequestOtp_InvalidPhoneFormat_Returns400() throws Exception {
        // Arrange
        LoginRequest_LROA938 request = new LoginRequest_LROA938("12345", "MyS3cur3P@ssw0rd!");

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.errorDetails[0].field").value("phone"));
    }

    /**
     * Tests validation failure for a password that is too short, expecting 400 Bad Request.
     */
    @Test
    void loginAndRequestOtp_PasswordTooShort_Returns400() throws Exception {
        // Arrange
        LoginRequest_LROA938 request = new LoginRequest_LROA938("+14155552671", "short");

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.errorDetails[0].field").value("password"));
    }

    /**
     * Tests the case where the service layer throws USER_NOT_FOUND, expecting 404 Not Found.
     */
    @Test
    void loginAndRequestOtp_UserNotFound_Returns404() throws Exception {
        // Arrange
        LoginRequest_LROA938 request = new LoginRequest_LROA938("+14155552671", "MyS3cur3P@ssw0rd!");
        when(authenticationService.loginAndRequestOtp(any(LoginRequest_LROA938.class)))
                .thenThrow(new ApplicationException_LROA938(ErrorCode_LROA938.USER_NOT_FOUND));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
    }

    /**
     * Tests the case where the service layer throws ACCOUNT_LOCKED, expecting 403 Forbidden.
     */
    @Test
    void loginAndRequestOtp_AccountLocked_Returns403() throws Exception {
        // Arrange
        LoginRequest_LROA938 request = new LoginRequest_LROA938("+14155552671", "MyS3cur3P@ssw0rd!");
        when(authenticationService.loginAndRequestOtp(any(LoginRequest_LROA938.class)))
                .thenThrow(new ApplicationException_LROA938(ErrorCode_LROA938.ACCOUNT_LOCKED));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("ACCOUNT_LOCKED"));
    }
}
```