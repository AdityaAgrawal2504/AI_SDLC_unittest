package com.fetchconversations.api.controller;

import com.fetchconversations.api.config.SecurityConfigFCA911;
import com.fetchconversations.api.config.WebConfigFCA911;
import com.fetchconversations.api.dto.PaginatedConversationsResponseFCA911;
import com.fetchconversations.api.dto.PaginationInfoFCA911;
import com.fetchconversations.api.enums.SortFieldFCA911;
import com.fetchconversations.api.enums.SortOrderFCA911;
import com.fetchconversations.api.exception.GlobalExceptionHandlerFCA911;
import com.fetchconversations.api.service.ConversationServiceFCA911;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConversationControllerFCA911.class)
@Import({SecurityConfigFCA911.class, WebConfigFCA911.class, GlobalExceptionHandlerFCA911.class})
class ConversationControllerFCA911Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversationServiceFCA911 conversationService;

    // We don't need to mock AuthUtil here because WithMockUser handles the SecurityContext
    // and the service test is responsible for testing AuthUtil interactions.
    
    @Test
    @WithMockUser(username = "a81d4a3e-63f3-4a73-9a3b-3d3a6b2807e4")
    void fetchConversations_shouldReturn200_whenRequestIsValid() throws Exception {
        // Arrange
        PaginatedConversationsResponseFCA911 mockResponse = PaginatedConversationsResponseFCA911.builder()
                .data(Collections.emptyList())
                .pagination(PaginationInfoFCA911.builder().currentPage(1).pageSize(25).totalPages(0).totalItems(0).build())
                .build();
        
        given(conversationService.getConversations(anyString(), any(SortFieldFCA911.class), any(SortOrderFCA911.class), anyInt(), anyInt()))
                .willReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/v1/conversations")
                        .param("query", "hello")
                        .param("sort", "lastActivity")
                        .param("order", "desc")
                        .param("page", "1")
                        .param("pageSize", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.currentPage").value(1))
                .andExpect(jsonPath("$.pagination.pageSize").value(25));
    }

    @Test
    @WithMockUser // A user is required for the endpoint to be accessed
    void fetchConversations_shouldReturn400_whenPageIsInvalid() throws Exception {
        mockMvc.perform(get("/v1/conversations").param("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.details.page").exists());
    }

    @Test
    @WithMockUser
    void fetchConversations_shouldReturn400_whenPageSizeIsTooLarge() throws Exception {
        mockMvc.perform(get("/v1/conversations").param("pageSize", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.details.pageSize").exists());
    }

    @Test
    @WithMockUser
    void fetchConversations_shouldReturn400_whenSortFieldIsInvalid() throws Exception {
        mockMvc.perform(get("/v1/conversations").param("sort", "invalidSort"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.message").value("The parameter 'sort' has an invalid value 'invalidSort'."));
    }

    @Test
    void fetchConversations_shouldReturn401_whenUserIsUnauthenticated() throws Exception {
        mockMvc.perform(get("/v1/conversations"))
                .andExpect(status().isUnauthorized());
    }
}
```