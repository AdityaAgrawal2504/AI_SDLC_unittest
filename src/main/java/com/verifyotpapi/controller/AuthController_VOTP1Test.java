package com.verifyotpapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verifyotpapi.dto.request.VerifyOtpRequest_VOTP1;
import com.verifyotpapi.dto.response.VerifyOtpSuccessResponse_VOTP1;
import com.verifyotpapi.exception.InvalidOtpException_VOTP1;
import com.verifyotpapi.exception.ResourceNotFoundException_VOTP1;
import com.verifyotpapi.service.IAuthService_VOTP1;
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

@WebMvcTest(AuthController_VOTP1.class)
class AuthController_VOTP1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthService_VOTP1 authService;

    @Test
    void verifyOtp_Success_Returns200Ok() throws Exception {
        // Arrange
        VerifyOtpRequest_VOTP1 request = new VerifyOtpRequest_VOTP1("valid-token", "123456");
        VerifyOtpSuccessResponse_VOTP1 response = VerifyOtpSuccessResponse_VOTP1.builder()
            .accessToken("jwt-access-token")
            .refreshToken("jwt-refresh-token")
            .expiresIn(3600)
            .tokenType("Bearer")
            .build();
        when(authService.verifyOtp(any(VerifyOtpRequest_VOTP1.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("jwt-access-token"))
            .andExpect(jsonPath("$.refreshToken").value("jwt-refresh-token"))
            .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void verifyOtp_InvalidRequest_Returns400BadRequest() throws Exception {
        // Arrange
        VerifyOtpRequest_VOTP1 request = new VerifyOtpRequest_VOTP1("valid-token", "123"); // Invalid OTP format

        // Act & Assert
        mockMvc.perform(post("/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void verifyOtp_InvalidCode_Returns401Unauthorized() throws Exception {
        // Arrange
        VerifyOtpRequest_VOTP1 request = new VerifyOtpRequest_VOTP1("valid-token", "111111");
        when(authService.verifyOtp(any(VerifyOtpRequest_VOTP1.class))).thenThrow(new InvalidOtpException_VOTP1());

        // Act & Assert
        mockMvc.perform(post("/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.errorCode").value("INVALID_OTP"));
    }

    @Test
    void verifyOtp_TokenNotFound_Returns404NotFound() throws Exception {
        // Arrange
        VerifyOtpRequest_VOTP1 request = new VerifyOtpRequest_VOTP1("invalid-token", "123456");
        when(authService.verifyOtp(any(VerifyOtpRequest_VOTP1.class))).thenThrow(new ResourceNotFoundException_VOTP1());

        // Act & Assert
        mockMvc.perform(post("/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("TOKEN_NOT_FOUND"));
    }

    @Test
    void verifyOtp_InternalServerError_Returns500() throws Exception {
        // Arrange
        VerifyOtpRequest_VOTP1 request = new VerifyOtpRequest_VOTP1("valid-token", "123456");
        when(authService.verifyOtp(any(VerifyOtpRequest_VOTP1.class))).thenThrow(new RuntimeException("Database is down"));

        // Act & Assert
        mockMvc.perform(post("/v1/auth/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"));
    }
}
```