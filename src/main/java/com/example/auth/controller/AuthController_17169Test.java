package com.example.auth.controller;

import com.example.auth.dto.request.VerifyOtpRequest_17169;
import com.example.auth.dto.response.ErrorResponse_17169;
import com.example.auth.dto.response.VerifyOtpSuccessResponse_17169;
import com.example.auth.enums.ErrorCode_AuthVerifyOtp_17169;
import com.example.auth.exception.ApplicationException_AuthVerifyOtp_17169;
import com.example.auth.service.AuthService_17169;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController_17169.class)
class AuthController_17169Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService_17169 authService;

    @Autowired
    private ObjectMapper objectMapper;

    private VerifyOtpRequest_17169 validRequest;
    private VerifyOtpSuccessResponse_17169 successResponse;

    @BeforeEach
    void setUp() {
        validRequest = new VerifyOtpRequest_17169();
        validRequest.setPhoneNumber("+1234567890");
        validRequest.setOtp("123456");

        successResponse = VerifyOtpSuccessResponse_17169.builder()
                .sessionToken("mockJwtToken")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .build();
    }

    @Test
    void verifyOtp_Success() throws Exception {
        when(authService.verifyOtpAndCreateSession(any(VerifyOtpRequest_17169.class)))
                .thenReturn(successResponse);

        mockMvc.perform(post("/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionToken").value("mockJwtToken"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600L));
    }

    @Test
    void verifyOtp_InvalidPhoneNumberFormat() throws Exception {
        validRequest.setPhoneNumber("12345"); // Invalid format
        performInvalidRequestAndExpectError(validRequest, ErrorCode_AuthVerifyOtp_17169.INVALID_PHONE_FORMAT.getCode(), 400);
    }

    @Test
    void verifyOtp_InvalidOtpFormat() throws Exception {
        validRequest.setOtp("abc"); // Invalid format
        performInvalidRequestAndExpectError(validRequest, ErrorCode_AuthVerifyOtp_17169.INVALID_OTP_FORMAT.getCode(), 400);
    }

    @Test
    void verifyOtp_MissingPhoneNumber() throws Exception {
        validRequest.setPhoneNumber(""); // Missing/Blank
        performInvalidRequestAndExpectError(validRequest, ErrorCode_AuthVerifyOtp_17169.INVALID_PHONE_FORMAT.getCode(), 400);
    }

    @Test
    void verifyOtp_MissingOtp() throws Exception {
        validRequest.setOtp(""); // Missing/Blank
        performInvalidRequestAndExpectError(validRequest, ErrorCode_AuthVerifyOtp_17169.INVALID_OTP_FORMAT.getCode(), 400);
    }

    @Test
    void verifyOtp_OtpNotFound() throws Exception {
        when(authService.verifyOtpAndCreateSession(any(VerifyOtpRequest_17169.class)))
                .thenThrow(new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.OTP_NOT_FOUND));

        performApplicationExceptionTest(validRequest, ErrorCode_AuthVerifyOtp_17169.OTP_NOT_FOUND.getCode(), 404);
    }

    @Test
    void verifyOtp_OtpIncorrect() throws Exception {
        when(authService.verifyOtpAndCreateSession(any(VerifyOtpRequest_17169.class)))
                .thenThrow(new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.OTP_INCORRECT));

        performApplicationExceptionTest(validRequest, ErrorCode_AuthVerifyOtp_17169.OTP_INCORRECT.getCode(), 401);
    }

    @Test
    void verifyOtp_OtpExpired() throws Exception {
        when(authService.verifyOtpAndCreateSession(any(VerifyOtpRequest_17169.class)))
                .thenThrow(new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.OTP_EXPIRED));

        performApplicationExceptionTest(validRequest, ErrorCode_AuthVerifyOtp_17169.OTP_EXPIRED.getCode(), 401);
    }

    @Test
    void verifyOtp_TooManyAttempts() throws Exception {
        when(authService.verifyOtpAndCreateSession(any(VerifyOtpRequest_17169.class)))
                .thenThrow(new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.TOO_MANY_ATTEMPTS));

        performApplicationExceptionTest(validRequest, ErrorCode_AuthVerifyOtp_17169.TOO_MANY_ATTEMPTS.getCode(), 429);
    }

    @Test
    void verifyOtp_SessionCreationFailed() throws Exception {
        when(authService.verifyOtpAndCreateSession(any(VerifyOtpRequest_17169.class)))
                .thenThrow(new ApplicationException_AuthVerifyOtp_17169(ErrorCode_AuthVerifyOtp_17169.SESSION_CREATION_FAILED));

        performApplicationExceptionTest(validRequest, ErrorCode_AuthVerifyOtp_17169.SESSION_CREATION_FAILED.getCode(), 500);
    }

    @Test
    void verifyOtp_InternalServerError() throws Exception {
        when(authService.verifyOtpAndCreateSession(any(VerifyOtpRequest_17169.class)))
                .thenThrow(new RuntimeException("Something unexpected happened"));

        performApplicationExceptionTest(validRequest, ErrorCode_AuthVerifyOtp_17169.INTERNAL_SERVER_ERROR.getCode(), 500);
    }

    private void performInvalidRequestAndExpectError(VerifyOtpRequest_17169 request, String expectedErrorCode, int expectedHttpStatus) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(expectedHttpStatus))
                .andReturn();

        ErrorResponse_17169 errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse_17169.class);
        assertEquals(expectedErrorCode, errorResponse.getErrorCode());
        // You might also assert the message, but it's often more dynamic with validation errors.
        // For simplicity, just checking errorCode and status.
    }

    private void performApplicationExceptionTest(VerifyOtpRequest_17169 request, String expectedErrorCode, int expectedHttpStatus) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(expectedHttpStatus))
                .andReturn();

        ErrorResponse_17169 errorResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse_17169.class);
        assertEquals(expectedErrorCode, errorResponse.getErrorCode());
        // For application exceptions, message should match enum
        assertEquals(ErrorCode_AuthVerifyOtp_17169.valueOf(expectedErrorCode).getMessage(), errorResponse.getMessage());
    }
}