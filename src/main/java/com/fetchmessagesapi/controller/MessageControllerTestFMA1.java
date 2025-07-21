package com.fetchmessagesapi.controller;

import com.fetchmessagesapi.config.TestSecurityConfigFMA1;
import com.fetchmessagesapi.dto.MessageListResponseFMA1;
import com.fetchmessagesapi.dto.PaginationInfoFMA1;
import com.fetchmessagesapi.exception.PermissionDeniedExceptionFMA1;
import com.fetchmessagesapi.exception.ResourceNotFoundExceptionFMA1;
import com.fetchmessagesapi.service.MessageServiceFMA1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageControllerFMA1.class)
@Import(TestSecurityConfigFMA1.class)
class MessageControllerTestFMA1 {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageServiceFMA1 messageService;

    private final String API_ENDPOINT = "/v1/conversations/{conversationId}/messages";
    private final UUID testUserId = TestSecurityConfigFMA1.TEST_USER_ID;

    @Test
    @WithMockUser
    void fetchMessages_shouldReturn200_onSuccess() throws Exception {
        UUID conversationId = UUID.randomUUID();
        MessageListResponseFMA1 mockResponse = new MessageListResponseFMA1(
                Collections.emptyList(),
                new PaginationInfoFMA1(null, false)
        );

        when(messageService.fetchMessages(any(UUID.class), anyInt(), any(), any(UUID.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(get(API_ENDPOINT, conversationId)
                        .param("limit", "20")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pagination.has_more", is(false)));
    }

    @Test
    @WithMockUser
    void fetchMessages_shouldReturn400_forInvalidLimit() throws Exception {
        UUID conversationId = UUID.randomUUID();

        // Case 1: Limit too high
        mockMvc.perform(get(API_ENDPOINT, conversationId)
                        .param("limit", "101")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code", is("INVALID_PARAMETER")));

        // Case 2: Limit too low
        mockMvc.perform(get(API_ENDPOINT, conversationId)
                        .param("limit", "0")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code", is("INVALID_PARAMETER")));
    }

    @Test
    @WithMockUser
    void fetchMessages_shouldReturn400_forInvalidConversationIdFormat() throws Exception {
        mockMvc.perform(get(API_ENDPOINT, "not-a-uuid")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code", is("INVALID_PARAMETER")));
    }

    @Test
    @WithMockUser
    void fetchMessages_shouldReturn404_whenConversationNotFound() throws Exception {
        UUID conversationId = UUID.randomUUID();
        when(messageService.fetchMessages(eq(conversationId), anyInt(), any(), eq(testUserId)))
                .thenThrow(new ResourceNotFoundExceptionFMA1("Conversation not found."));

        mockMvc.perform(get(API_ENDPOINT, conversationId)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code", is("RESOURCE_NOT_FOUND")))
                .andExpect(jsonPath("$.message", is("Conversation not found.")));
    }

    @Test
    @WithMockUser
    void fetchMessages_shouldReturn403_whenUserLacksPermission() throws Exception {
        UUID conversationId = UUID.randomUUID();
        when(messageService.fetchMessages(eq(conversationId), anyInt(), any(), eq(testUserId)))
                .thenThrow(new PermissionDeniedExceptionFMA1("You do not have permission to access this conversation."));

        mockMvc.perform(get(API_ENDPOINT, conversationId)
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error_code", is("PERMISSION_DENIED")));
    }

    @Test
    void fetchMessages_shouldReturn401_whenNoAuthHeader() throws Exception {
        // Note: In a real test with full security filter chain, this would be 401.
        // @WebMvcTest may not fully invoke the security filter chain in the same way.
        // This test confirms the endpoint is secured.
        UUID conversationId = UUID.randomUUID();
        mockMvc.perform(get(API_ENDPOINT, conversationId))
                .andExpect(status().isUnauthorized()); // Or 403 if default denyAll is hit first
    }
}
```
```java
// FILENAME: src/test/java/com/fetchmessagesapi/config/TestSecurityConfigFMA1.java