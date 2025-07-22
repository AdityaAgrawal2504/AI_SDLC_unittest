package com.example.controller;

import com.example.dto.*;
import com.example.service.AuthService_UATH_1008;
import com.example.service.UserService_UATH_1009;
import com.example.util.StructuredLogger_UTIL_9999;
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

@WebMvcTest(UserAuthController_UATH_1001.class)
class UserAuthController_UATH_1001Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService_UATH_1009 userService;

    @MockBean
    private AuthService_UATH_1008 authService;

    @MockBean
    private StructuredLogger_UTIL_9999 logger; // Mock because it's injected

    @Test
    void registerUser_whenValidRequest_shouldReturnCreated() throws Exception {
        UserRegistrationRequest_UATH_1006 request = new UserRegistrationRequest_UATH_1006("1234567890", "Password@123");
        UserRegistrationResponse_UATH_1007 response = new UserRegistrationResponse_UATH_1007(UUID.randomUUID(), "User registered successfully.");

        when(userService.registerUser(any(UserRegistrationRequest_UATH_1006.class))).thenReturn(response);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(response.userId().toString()));
    }

    @Test
    void registerUser_whenInvalidPhoneNumber_shouldReturnBadRequest() throws Exception {
        UserRegistrationRequest_UATH_1006 request = new UserRegistrationRequest_UATH_1006("123", "Password@123");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void initiateLogin_whenValidCredentials_shouldReturnOk() throws Exception {
        LoginInitiateRequest_UATH_1004 request = new LoginInitiateRequest_UATH_1004("1234567890", "password");
        GenericSuccessResponse_UATH_1003 response = new GenericSuccessResponse_UATH_1003("OTP has been sent to your phone number.");

        when(authService.initiateLogin(any(LoginInitiateRequest_UATH_1004.class))).thenReturn(response);

        mockMvc.perform(post("/auth/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(response.message()));
    }

    @Test
    void verifyLogin_whenValidOtp_shouldReturnTokens() throws Exception {
        OtpVerifyRequest_UATH_1005 request = new OtpVerifyRequest_UATH_1005("1234567890", "123456");
        AuthTokenResponse_UATH_1002 response = new AuthTokenResponse_UATH_1002("access.token", "refresh.token", 3600L);

        when(authService.verifyOtpAndLogin(any(OtpVerifyRequest_UATH_1005.class))).thenReturn(response);

        mockMvc.perform(post("/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access.token"));
    }
}
```
```java
//
// Filename: src/test/java/com/example/controller/ConversationController_CHAT_2001Test.java
//