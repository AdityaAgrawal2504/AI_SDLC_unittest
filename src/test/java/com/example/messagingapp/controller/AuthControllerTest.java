package com.example.messagingapp.controller;

import com.example.messagingapp.dto.AuthTokenResponse;
import com.example.messagingapp.dto.LoginInitiateRequest;
import com.example.messagingapp.dto.LoginVerifyRequest;
import com.example.messagingapp.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    /**
     * Tests successful login initiation.
     */
    @Test
    void initiateLogin_shouldReturnOk() throws Exception {
        LoginInitiateRequest request = new LoginInitiateRequest("+15555555555", "password123");
        doNothing().when(authService).initiateLogin(any(LoginInitiateRequest.class));

        mockMvc.perform(post("/auth/login/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP has been sent to your phone number."));
    }

    /**
     * Tests successful login verification.
     */
    @Test
    void verifyLogin_shouldReturnToken() throws Exception {
        LoginVerifyRequest request = new LoginVerifyRequest("+15555555555", "123456");
        AuthTokenResponse response = new AuthTokenResponse("test.token.string");
        when(authService.verifyLogin(any(LoginVerifyRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("test.token.string"));
    }
}