package com.example.controller;

import com.example.dto.response.PaginatedConversationsResponse;
import com.example.model.User;
import com.example.service.IConversationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConversationController.class)
@Import(com.example.config.TestSecurityConfig.class)
class ConversationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IConversationService conversationService;

    // A mock user ID for a user that is "authenticated" by @WithMockUser
    private final String MOCK_USER_ID = "a1b2c3d4-e5f6-7890-1234-567890abcdef";

    @Test
    @WithMockUser(username = MOCK_USER_ID)
    void listConversations_shouldReturnOk() throws Exception {
        PaginatedConversationsResponse response = PaginatedConversationsResponse.builder().build();
        when(conversationService.findUserConversations(any(UUID.class), anyInt(), anyInt())).thenReturn(response);
        
        // Mock the User object that will be injected by @AuthenticationPrincipal
        User mockUser = User.builder().id(UUID.fromString(MOCK_USER_ID)).build();
        mockMvc.perform(get("/conversations").principal(() -> mockUser.getUsername()))
                .andExpect(status().isOk());
    }

    @Test
    void listConversations_withoutAuth_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/conversations"))
                .andExpect(status().isUnauthorized());
    }
}