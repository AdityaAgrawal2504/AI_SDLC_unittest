package com.example.controller;

import com.example.dto.request.UserSignupRequest;
import com.example.dto.response.UserSignupResponse;
import com.example.service.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthController.class)
@Import(com.example.config.TestSecurityConfig.class) // Import a minimal security config
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUserAccount_withValidRequest_shouldReturnCreated() throws Exception {
        UserSignupRequest request = new UserSignupRequest();
        request.setPhoneNumber("+15551234567");
        request.setPassword("password123");

        UserSignupResponse response = UserSignupResponse.builder().build();
        when(authService.signUp(any(UserSignupRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUserAccount_withInvalidRequest_shouldReturnBadRequest() throws Exception {
        UserSignupRequest request = new UserSignupRequest();
        request.setPhoneNumber("invalid-phone"); // Invalid format
        request.setPassword("short"); // Too short

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}