package com.example.controller;

import com.example.dto.UserRegistrationRequest;
import com.example.dto.UserResponse;
import com.example.service.IUserService;
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

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, AppConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUserService userService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void registerUser_whenValidInput_shouldReturnCreated() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setPhoneNumber("+14155552671");
        request.setPassword("StrongP@ssw0rd!");

        UserResponse response = UserResponse.builder()
                .id(UUID.randomUUID())
                .phoneNumber(request.getPhoneNumber())
                .createdAt(OffsetDateTime.now())
                .build();

        when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId().toString()))
                .andExpect(jsonPath("$.phone_number").value(request.getPhoneNumber()));
    }
    
    @Test
    void registerUser_whenInvalidInput_shouldReturnBadRequest() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setPhoneNumber("invalid-phone"); // Invalid format
        request.setPassword("weak"); // Invalid format

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}