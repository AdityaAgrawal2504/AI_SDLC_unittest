package com.example.controller;

import com.example.dto.LoginInitiateDto;
import com.example.dto.LoginVerifyDto;
import com.example.dto.UserSignupDto;
import com.example.model.User;
import com.example.service.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserSignupDto userSignupDto;
    private User user;

    @BeforeEach
    void setUp() {
        userSignupDto = new UserSignupDto();
        userSignupDto.setName("Test User");
        userSignupDto.setPhoneNumber("+15555555555");
        userSignupDto.setPassword("Password123!");

        user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userSignupDto.getName());
        user.setPhoneNumber(userSignupDto.getPhoneNumber());
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void userSignup_whenValidInput_shouldReturnCreated() throws Exception {
        when(authService.registerUser(any(UserSignupDto.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    @Test
    void initiateLogin_whenValidCredentials_shouldReturnOk() throws Exception {
        LoginInitiateDto loginDto = new LoginInitiateDto();
        loginDto.setPhoneNumber("+15555555555");
        loginDto.setPassword("Password123!");
        
        doNothing().when(authService).initiateLogin(any(LoginInitiateDto.class));

        mockMvc.perform(post("/auth/login/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP sent to your registered phone number."));
    }

    @Test
    void verifyLogin_whenValidOtp_shouldReturnToken() throws Exception {
        LoginVerifyDto verifyDto = new LoginVerifyDto();
        verifyDto.setPhoneNumber("+15555555555");
        verifyDto.setOtp("123456");

        String fakeToken = "fake.jwt.token";
        when(authService.completeLogin(any(LoginVerifyDto.class))).thenReturn(fakeToken);

        mockMvc.perform(post("/auth/login/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(fakeToken))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }
}