package com.yourorg.yourapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourorg.yourapp.dto.request.LoginRequestLROA9123;
import com.yourorg.yourapp.dto.response.LoginSuccessResponseLROA9123;
import com.yourorg.yourapp.enums.ErrorCodeLROA9123;
import com.yourorg.yourapp.exception.ApiExceptionLROA9123;
import com.yourorg.yourapp.service.AuthServiceLROA9123;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthControllerV1_LROA9123.class)
class AuthControllerV1_LROA9123Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthServiceLROA9123 authService;

    private LoginRequestLROA9123 validLoginRequest;

    @BeforeEach
    void setUp() {
        validLoginRequest = LoginRequestLROA9123.builder()
            .phone("+14155552671")
            .password("MyS3cur3P@ssw0rd!")
            .build();
    }

    @Test
    void loginAndRequestOtp_Success_Returns200() throws Exception {
        // Arrange
        LoginSuccessResponseLROA9123 successResponse = LoginSuccessResponseLROA9123.builder()
            .status("success")
            .message("OTP has been sent successfully. Please check your device.")
            .otpSessionToken("mock-otp-token")
            .build();
        when(authService.loginAndRequestOtp(any(LoginRequestLROA9123.class))).thenReturn(successResponse);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("success"))
            .andExpect(jsonPath("$.otpSessionToken").value("mock-otp-token"));
    }

    @Test
    void loginAndRequestOtp_InvalidPhoneNumberFormat_Returns400() throws Exception {
        // Arrange
        LoginRequestLROA9123 invalidRequest = new LoginRequestLROA9123("12345", "password123");

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value(ErrorCodeLROA9123.VALIDATION_FAILED.getCode()))
            .andExpect(jsonPath("$.errorDetails[0].field").value("phone"));
    }

    @Test
    void loginAndRequestOtp_PasswordTooShort_Returns400() throws Exception {
        // Arrange
        LoginRequestLROA9123 invalidRequest = new LoginRequestLROA9123("+14155552671", "pass");

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value(ErrorCodeLROA9123.VALIDATION_FAILED.getCode()))
            .andExpect(jsonPath("$.errorDetails[0].field").value("password"));
    }

    @Test
    void loginAndRequestOtp_UserNotFound_Returns404() throws Exception {
        // Arrange
        when(authService.loginAndRequestOtp(any(LoginRequestLROA9123.class)))
            .thenThrow(new ApiExceptionLROA9123(ErrorCodeLROA9123.USER_NOT_FOUND));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value(ErrorCodeLROA9123.USER_NOT_FOUND.getCode()));
    }

    @Test
    void loginAndRequestOtp_InvalidCredentials_Returns401() throws Exception {
        // Arrange
        when(authService.loginAndRequestOtp(any(LoginRequestLROA9123.class)))
            .thenThrow(new ApiExceptionLROA9123(ErrorCodeLROA9123.INVALID_CREDENTIALS));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errorCode").value(ErrorCodeLROA9123.INVALID_CREDENTIALS.getCode()));
    }

    @Test
    void loginAndRequestOtp_AccountLocked_Returns403() throws Exception {
        // Arrange
        when(authService.loginAndRequestOtp(any(LoginRequestLROA9123.class)))
            .thenThrow(new ApiExceptionLROA9123(ErrorCodeLROA9123.ACCOUNT_LOCKED));
            
        // Act & Assert
        mockMvc.perform(post("/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.errorCode").value(ErrorCodeLROA9123.ACCOUNT_LOCKED.getCode()));
    }
}
```
src/test/java/com/yourorg/yourapp/YourAppApplicationTests.java
```java