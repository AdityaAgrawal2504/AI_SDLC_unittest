package com.example.messagingapp.controller;

import com.example.messagingapp.dto.UserRegistrationRequest;
import com.example.messagingapp.dto.UserResponse;
import com.example.messagingapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    /**
     * Tests successful user registration.
     */
    @Test
    void registerUser_shouldReturnCreated() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest("+19876543210", "securePassword123");
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
                .andExpect(jsonPath("$.phoneNumber").value(request.getPhoneNumber()));
    }
    
    /**
     * Tests user search functionality.
     */
    @Test
    @WithMockUser // Endpoint is secured
    void searchUsers_shouldReturnListOfUsers() throws Exception {
        UserResponse response = UserResponse.builder()
                .id(UUID.randomUUID())
                .phoneNumber("+15551234567")
                .createdAt(OffsetDateTime.now())
                .build();
        
        when(userService.searchUsers(anyString())).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/users/search").param("q", "555"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phoneNumber").value("+15551234567"));
    }
}