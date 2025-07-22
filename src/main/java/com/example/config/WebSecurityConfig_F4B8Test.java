package com.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
// Use a test profile to ensure specific properties if needed, though for config it's often not critical
// @ActiveProfiles("test") 
class WebSecurityConfig_F4B8Test {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    void contextLoads() {
        // Verify that the Spring application context loads successfully with the security configuration.
        assertNotNull(applicationContext);
    }

    @Test
    void passwordEncoderBeanExists() {
        // Verify that the PasswordEncoder bean is available in the context.
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void securityFilterChainBeanExists() {
        // Verify that the SecurityFilterChain bean is available in the context.
        SecurityFilterChain securityFilterChain = applicationContext.getBean(SecurityFilterChain.class);
        assertNotNull(securityFilterChain);
    }

    @Test
    void unauthenticatedAccessToPermittedEndpoints() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Test /users/register - should be permitted
        mockMvc.perform(post("/users/register").with(csrf()))
                .andExpect(status().isBadRequest()); // Expect 400 because validation will fail, but not 401/403

        // Test /h2-console - should be permitted
        mockMvc.perform(get("/h2-console/")).andExpect(status().isOk());

        // Test Swagger UI - should be permitted
        mockMvc.perform(get("/swagger-ui.html")).andExpect(status().isOk());
        mockMvc.perform(get("/api-docs")).andExpect(status().isOk());
    }

    @Test
    void unauthenticatedAccessToProtectedEndpoints() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Test any other arbitrary endpoint - should be authenticated (401 Unauthorized)
        // Since there are no other controllers defined, this might hit a 404 first.
        // A simple GET to a non-existent path will hit the security filter chain first.
        mockMvc.perform(get("/some-other-protected-path"))
                .andExpect(status().isUnauthorized());
    }

    // You might add more specific tests if you had more secured endpoints
    // For example, using @WithMockUser for authenticated requests
}
```
```java
src/test/java/com/example/util/LoggingAspect_F4B8Test.java