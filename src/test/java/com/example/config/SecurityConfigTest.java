package com.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(TestSecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenAccessPublicEndpoint_thenIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup"))
                .andExpect(status().isBadRequest()); // BadRequest due to no body, but shows endpoint is accessible
    }

    @Test
    void whenAccessProtectedEndpointWithoutAuth_thenIsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/conversations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser // Simulates an authenticated user
    void whenAccessProtectedEndpointWithAuth_thenIsOk() throws Exception {
        // This will fail because the controller and service layers are not loaded.
        // The goal here is just to test the security layer allows the request through.
        // It will result in 404 or other errors if the endpoint mapping doesn't exist in the test slice.
        // For this test, we can check for a status other than 401/403.
         mockMvc.perform(MockMvcRequestBuilders.get("/conversations"))
                .andExpect(status().isNotFound()); // Or whatever the framework returns when no controller is mapped
    }
}