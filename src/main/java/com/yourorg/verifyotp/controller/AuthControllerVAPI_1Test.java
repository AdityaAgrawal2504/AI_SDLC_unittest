package com.yourorg.verifyotp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourorg.verifyotp.constants.ErrorCodeVAPI_1;
import com.yourorg.verifyotp.dto.VerifyOtpRequestVAPI_1;
import com.yourorg.verifyotp.dto.VerifyOtpSuccessResponseVAPI_1;
import com.yourorg.verifyotp.exception.CustomApiExceptionVAPI_1;
import com.yourorg.verifyotp.service.IAuthServiceVAPI_1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Import necessary configurations for the test context
@Import({com.yourorg.verifyotp.exception.GlobalExceptionHandlerVAPI_1.class, com.yourorg.verifyotp.config.LoggingAspectVAPI_1.class})
@WebMvcTest(controllers = AuthControllerVAPI_1.class)
class AuthControllerVAPI_1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthServiceVAPI_1 authService;

    @Test
    void verifyOtp_Success_Returns200() throws Exception {
        VerifyOtpRequestVAPI_1 request = new VerifyOtpRequestVAPI_1();
        request.setVerificationToken("valid-token");
        request.setOtpCode("123456");

        VerifyOtpSuccessResponseVAPI_1 successResponse = new VerifyOtpSuccessResponseVAPI_1("access-token", 3600, "refresh-token");

        when(authService.verifyOtp(any(VerifyOtpRequestVAPI_1.class))).thenReturn(successResponse);

        mockMvc.perform(post("/v1/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
    
    @Test
    void verifyOtp_InvalidRequest_Returns400() throws Exception {
        VerifyOtpRequestVAPI_1 request = new VerifyOtpRequestVAPI_1();
        request.setVerificationToken("valid-token");
        request.setOtpCode("123"); // Invalid OTP format

        mockMvc.perform(post("/v1/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    void verifyOtp_InvalidCode_Returns401() throws Exception {
        VerifyOtpRequestVAPI_1 request = new VerifyOtpRequestVAPI_1();
        request.setVerificationToken("valid-token");
        request.setOtpCode("123456");

        when(authService.verifyOtp(any(VerifyOtpRequestVAPI_1.class)))
                .thenThrow(new CustomApiExceptionVAPI_1(ErrorCodeVAPI_1.INVALID_OTP));

        mockMvc.perform(post("/v1/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("INVALID_OTP"))
                .andExpect(jsonPath("$.errorMessage").value("The OTP code provided is incorrect."));
    }

    @Test
    void verifyOtp_TokenNotFound_Returns404() throws Exception {
        VerifyOtpRequestVAPI_1 request = new VerifyOtpRequestVAPI_1();
        request.setVerificationToken("invalid-token");
        request.setOtpCode("123456");

        when(authService.verifyOtp(any(VerifyOtpRequestVAPI_1.class)))
                .thenThrow(new CustomApiExceptionVAPI_1(ErrorCodeVAPI_1.TOKEN_NOT_FOUND));

        mockMvc.perform(post("/v1/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("TOKEN_NOT_FOUND"));
    }
}
```