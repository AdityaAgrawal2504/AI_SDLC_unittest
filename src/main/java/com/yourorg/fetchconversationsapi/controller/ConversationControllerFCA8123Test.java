package com.yourorg.fetchconversationsapi.controller;

import com.yourorg.fetchconversationsapi.dto.PaginatedConversationsResponseFCA8123;
import com.yourorg.fetchconversationsapi.dto.PaginationInfoFCA8123;
import com.yourorg.fetchconversationsapi.enums.ConversationSortFieldFCA8123;
import com.yourorg.fetchconversationsapi.enums.SortOrderFCA8123;
import com.yourorg.fetchconversationsapi.service.ConversationServiceFCA8123;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.yourorg.fetchconversationsapi.config.SecurityConfigFCA8123;
import com.yourorg.fetchconversationsapi.exception.GlobalExceptionHandlerFCA8123;
import com.yourorg.fetchconversationsapi.logging.LoggingAspectFCA8123;
import com.yourorg.fetchconversationsapi.logging.LoggerServiceFCA8123;
import com.yourorg.fetchconversationsapi.logging.Log4j2LoggerServiceFCA8123;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the ConversationControllerFCA8123 class.
 */
@WebMvcTest(ConversationControllerFCA8123.class)
@Import({SecurityConfigFCA8123.class, GlobalExceptionHandlerFCA8123.class, LoggingAspectFCA8123.class, Log4j2LoggerServiceFCA8123.class})
class ConversationControllerFCA8123Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversationServiceFCA8123 conversationService;
    
    @MockBean
    private LoggerServiceFCA8123 loggerService;


    private PaginatedConversationsResponseFCA8123 mockResponse;

    @BeforeEach
    void setUp() {
        PaginationInfoFCA8123 paginationInfo = PaginationInfoFCA8123.builder()
                .currentPage(1)
                .pageSize(25)
                .totalPages(1)
                .totalItems(0)
                .build();

        mockResponse = PaginatedConversationsResponseFCA8123.builder()
                .data(Collections.emptyList())
                .pagination(paginationInfo)
                .build();
    }

    /**
     * Tests successful retrieval of conversations with default parameters.
     */
    @Test
    @WithMockUser // Bypasses security
    void fetchConversations_withDefaultParams_shouldReturnOk() throws Exception {
        when(conversationService.fetchConversations(any(), any(ConversationSortFieldFCA8123.class), any(SortOrderFCA8123.class), anyInt(), anyInt()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/v1/conversations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.currentPage").value(1))
                .andExpect(jsonPath("$.pagination.pageSize").value(25));
    }
    
    /**
     * Tests successful retrieval of conversations with all parameters specified.
     */
    @Test
    @WithMockUser
    void fetchConversations_withAllParams_shouldReturnOk() throws Exception {
        when(conversationService.fetchConversations(anyString(), any(ConversationSortFieldFCA8123.class), any(SortOrderFCA8123.class), anyInt(), anyInt()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/v1/conversations")
                        .param("query", "test")
                        .param("sort", "seen")
                        .param("order", "asc")
                        .param("page", "2")
                        .param("pageSize", "50"))
                .andExpect(status().isOk());
    }

    /**
     * Tests the case where the user is not authenticated.
     */
    @Test
    void fetchConversations_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/v1/conversations"))
                .andExpect(status().isUnauthorized());
    }
    
    /**
     * Tests validation failure for page size being too large.
     */
    @Test
    @WithMockUser
    void fetchConversations_whenPageSizeTooLarge_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/v1/conversations").param("pageSize", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.details.pageSize").value("Page size must not exceed 100"));
    }
    
    /**
     * Tests validation failure for page number being invalid.
     */
    @Test
    @WithMockUser
    void fetchConversations_whenPageIsInvalid_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/v1/conversations").param("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.details.page").value("Page number must be at least 1"));
    }
    
    /**
     * Tests validation failure for an invalid sort parameter.
     */
    @Test
    @WithMockUser
    void fetchConversations_whenSortIsInvalid_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/v1/conversations").param("sort", "invalidSort"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_PARAMETER"))
                .andExpect(jsonPath("$.message").value("The parameter 'sort' has an invalid value 'invalidSort'."));
    }
}
```