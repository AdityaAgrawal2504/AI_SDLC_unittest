package com.example.controller;

import com.example.dto.RequestOtpRequest;
import com.example.dto.VerifyOtpRequest;
import com.example.dto.VerifyOtpResponse;
import com.example.service.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.security.JwtAuthenticationFilter;
import com.example.config.AppConfig;
import com.example.security.SecurityConfig;
import com.example.security.UserDetailsServiceImpl;
import com.example.security.JwtUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, AppConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthService authService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void requestLoginOtp_shouldReturnOk() throws Exception {
        RequestOtpRequest request = new RequestOtpRequest();
        request.setPhoneNumber("+14155552671");
        request.setPassword("password");

        doNothing().when(authService).requestLoginOtp(any(RequestOtpRequest.class));

        mockMvc.perform(post("/auth/login/request-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OTP has been sent to your phone number."));
    }

    @Test
    void verifyLoginOtp_shouldReturnToken() throws Exception {
        VerifyOtpRequest request = new VerifyOtpRequest();
        request.setPhoneNumber("+14155552671");
        request.setOtp("123456");

        VerifyOtpResponse response = new VerifyOtpResponse("test-token", "Bearer");
        when(authService.verifyLoginOtp(any(VerifyOtpRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.token_type").value("Bearer"));
    }
}