package com.example.conversation.api.controller;

import com.example.conversation.api.config.SecurityConfigFCA1;
import com.example.conversation.api.dto.ConversationListResponseFCA1;
import com.example.conversation.api.dto.PaginationInfoFCA1;
import com.example.conversation.api.enums.SortByFCA1;
import com.example.conversation.api.exception.GlobalExceptionHandlerFCA1;
import com.example.conversation.api.security.SecurityUtilFCA1;
import com.example.conversation.api.service.ConversationServiceFCA1;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConversationControllerFCA1.class)
@Import({SecurityConfigFCA1.class, GlobalExceptionHandlerFCA1.class}) // Import security config and exception handler for comprehensive testing
class ConversationControllerFCA1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversationServiceFCA1 conversationService;

    @MockBean
    private SecurityUtilFCA1 securityUtil;

    @Autowired
    private ObjectMapper objectMapper; // To deserialize error responses

    private static final UUID TEST_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @BeforeEach
    void setUp() {
        when(securityUtil.getCurrentUserId()).thenReturn(TEST_USER_ID);

        // Default mock response for conversationService
        ConversationListResponseFCA1 mockResponse = new ConversationListResponseFCA1(
            Collections.emptyList(), // Data will be populated by specific test cases if needed
            new PaginationInfoFCA1(1, 25, 0, 0)
        );
        when(conversationService.fetchConversations(any(UUID.class), anyString(), any(SortByFCA1.class), anyInt(), anyInt()))
            .thenReturn(mockResponse);
        when(conversationService.fetchConversations(any(UUID.class), isNull(), any(SortByFCA1.class), anyInt(), anyInt()))
            .thenReturn(mockResponse);
    }

    @Test
    @WithMockUser
    void fetchConversations_defaultParams_returnsOk() throws Exception {
        ConversationListResponseFCA1 mockResponse = new ConversationListResponseFCA1(
            Collections.emptyList(),
            new PaginationInfoFCA1(1, 25, 10, 1)
        );
        when(conversationService.fetchConversations(eq(TEST_USER_ID), isNull(), eq(SortByFCA1.RECENCY), eq(1), eq(25)))
            .thenReturn(mockResponse);

        mockMvc.perform(get("/conversations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.currentPage").value(1))
                .andExpect(jsonPath("$.pagination.pageSize").value(25));
    }

    @Test
    @WithMockUser
    void fetchConversations_withSearchQueryAndSortBy_returnsFilteredAndSorted() throws Exception {
        String searchQuery = "test";
        SortByFCA1 sortBy = SortByFCA1.SEEN;
        int page = 1;
        int pageSize = 10;

        ConversationListResponseFCA1 mockResponse = new ConversationListResponseFCA1(
            Collections.emptyList(),
            new PaginationInfoFCA1(page, pageSize, 5, 1)
        );
        when(conversationService.fetchConversations(eq(TEST_USER_ID), eq(searchQuery), eq(sortBy), eq(page), eq(pageSize)))
            .thenReturn(mockResponse);

        mockMvc.perform(get("/conversations")
                .param("searchQuery", searchQuery)
                .param("sortBy", sortBy.getValue())
                .param("page", String.valueOf(page))
                .param("pageSize", String.valueOf(pageSize))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.currentPage").value(page))
                .andExpect(jsonPath("$.pagination.pageSize").value(pageSize));
    }

    @Test
    @WithMockUser
    void fetchConversations_invalidSearchQueryTooLong_returnsBadRequest() throws Exception {
        String longSearchQuery = "a".repeat(257); // Max is 256
        mockMvc.perform(get("/conversations")
                .param("searchQuery", longSearchQuery)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
                .andExpect(jsonPath("$.errorMessage").value("Input validation failed."))
                .andExpect(jsonPath("$.details[0].field").value("searchQuery"))
                .andExpect(jsonPath("$.details[0].message").value("searchQuery must be between 0 and 256 characters."));
    }

    @Test
    @WithMockUser
    void fetchConversations_invalidPageMin_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/conversations")
                .param("page", "0") // Min is 1
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("page"))
                .andExpect(jsonPath("$.details[0].message").value("page must be greater than or equal to 1."));
    }

    @Test
    @WithMockUser
    void fetchConversations_invalidPageSizeMin_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/conversations")
                .param("pageSize", "0") // Min is 1
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("pageSize"))
                .andExpect(jsonPath("$.details[0].message").value("pageSize must be greater than or equal to 1."));
    }

    @Test
    @WithMockUser
    void fetchConversations_invalidPageSizeMax_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/conversations")
                .param("pageSize", "101") // Max is 100
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("pageSize"))
                .andExpect(jsonPath("$.details[0].message").value("pageSize must be less than or equal to 100."));
    }

    @Test
    void fetchConversations_unauthenticated_returnsUnauthorized() throws Exception {
        // No @WithMockUser annotation, so it's unauthenticated
        mockMvc.perform(get("/conversations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHENTICATED"))
                .andExpect(jsonPath("$.errorMessage").value("Authentication credentials were not provided or are invalid."));
    }

    @Test
    @WithMockUser
    void fetchConversations_serviceThrowsException_returnsInternalServerError() throws Exception {
        when(conversationService.fetchConversations(any(), any(), any(), anyInt(), anyInt()))
            .thenThrow(new RuntimeException("Something went wrong in service"));

        mockMvc.perform(get("/conversations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("An unexpected error occurred while processing the request."));
    }
}