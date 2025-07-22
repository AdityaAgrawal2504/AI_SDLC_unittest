package com.example.conversation.history.fch01.controller;

import com.example.conversation.history.fch01.dto.ApiErrorFCHAPI01;
import com.example.conversation.history.fch01.dto.MessageDtoFCHAPI01;
import com.example.conversation.history.fch01.dto.PaginatedMessagesResponseFCHAPI01;
import com.example.conversation.history.fch01.dto.PaginationInfoFCHAPI01;
import com.example.conversation.history.fch01.enums.ErrorCodeFCHAPI01;
import com.example.conversation.history.fch01.exception.PermissionDeniedExceptionFCHAPI01;
import com.example.conversation.history.fch01.exception.ResourceNotFoundExceptionFCHAPI01;
import com.example.conversation.history.fch01.service.ConversationServiceFCHAPI01;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.conversation.history.fch01.service.ConversationServiceImplFCHAPI02.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConversationControllerFCHAPI01.class)
@DisplayName("ConversationControllerFCHAPI01 Unit Tests")
class ConversationControllerFCHAPI01Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversationServiceFCHAPI01 conversationService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/conversations/{conversationId}/messages";

    @Test
    @DisplayName("Should return 200 OK with paginated messages for valid request")
    void fetchConversationHistory_success() throws Exception {
        // Given
        UUID convId = VALID_CONVERSATION_ID;
        int limit = 20;
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        MessageDtoFCHAPI01 mockMessage = MessageDtoFCHAPI01.builder()
                .id(UUID.randomUUID())
                .content("Test message")
                .createdAt(now)
                .build();
        PaginatedMessagesResponseFCHAPI01 mockResponse = new PaginatedMessagesResponseFCHAPI01(
                List.of(mockMessage), new PaginationInfoFCHAPI01(now.minusSeconds(1), true));

        when(conversationService.fetchConversationHistory(eq(convId), eq(limit), any(OffsetDateTime.class)))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get(BASE_URL, convId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].id").value(mockMessage.getId().toString()))
                .andExpect(jsonPath("$.pagination.hasMore").value(true));
    }

    @Test
    @DisplayName("Should return 200 OK when 'before' parameter is provided")
    void fetchConversationHistory_successWithBefore() throws Exception {
        // Given
        UUID convId = VALID_CONVERSATION_ID;
        OffsetDateTime beforeTime = OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(5);
        int limit = 10;

        PaginatedMessagesResponseFCHAPI01 mockResponse = new PaginatedMessagesResponseFCHAPI01(
                Collections.emptyList(), new PaginationInfoFCHAPI01(null, false));

        when(conversationService.fetchConversationHistory(eq(convId), eq(limit), eq(beforeTime)))
                .thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get(BASE_URL, convId)
                .param("limit", String.valueOf(limit))
                .param("before", beforeTime.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.pagination.hasMore").value(false));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid UUID format")
    void fetchConversationHistory_invalidUuidFormat() throws Exception {
        // Given
        String invalidConvId = "not-a-uuid";

        // When & Then
        mockMvc.perform(get(BASE_URL, invalidConvId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCodeFCHAPI01.INVALID_PARAMETER.name()))
                .andExpect(jsonPath("$.details[0].field").value("conversationId"))
                .andExpect(jsonPath("$.details[0].issue").value("conversationId must be a valid UUID."));
    }

    @Test
    @DisplayName("Should return 400 Bad Request if limit is too low")
    void fetchConversationHistory_limitTooLow() throws Exception {
        // Given
        UUID convId = VALID_CONVERSATION_ID;
        int invalidLimit = 0;

        // When & Then
        mockMvc.perform(get(BASE_URL, convId)
                .param("limit", String.valueOf(invalidLimit))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCodeFCHAPI01.INVALID_PARAMETER.name()))
                .andExpect(jsonPath("$.details[0].field").value("limit"))
                .andExpect(jsonPath("$.details[0].issue").value("Limit must be greater than 0."));
    }

    @Test
    @DisplayName("Should return 400 Bad Request if limit is too high")
    void fetchConversationHistory_limitTooHigh() throws Exception {
        // Given
        UUID convId = VALID_CONVERSATION_ID;
        int invalidLimit = 101;

        // When & Then
        mockMvc.perform(get(BASE_URL, convId)
                .param("limit", String.valueOf(invalidLimit))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCodeFCHAPI01.INVALID_PARAMETER.name()))
                .andExpect(jsonPath("$.details[0].field").value("limit"))
                .andExpect(jsonPath("$.details[0].issue").value("Limit must be less than or equal to 100."));
    }

    @Test
    @DisplayName("Should return 404 Not Found when ConversationService throws ResourceNotFoundException")
    void fetchConversationHistory_resourceNotFound() throws Exception {
        // Given
        UUID convId = NON_EXISTENT_CONVERSATION_ID;
        String errorMessage = "The specified conversation could not be found.";
        when(conversationService.fetchConversationHistory(eq(convId), any(Integer.class), any(OffsetDateTime.class)))
                .thenThrow(new ResourceNotFoundExceptionFCHAPI01(errorMessage));

        // When & Then
        mockMvc.perform(get(BASE_URL, convId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCodeFCHAPI01.CONVERSATION_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("Should return 403 Forbidden when ConversationService throws PermissionDeniedException")
    void fetchConversationHistory_permissionDenied() throws Exception {
        // Given
        UUID convId = UNAUTHORIZED_CONVERSATION_ID;
        String errorMessage = "You do not have permission to access this conversation.";
        when(conversationService.fetchConversationHistory(eq(convId), any(Integer.class), any(OffsetDateTime.class)))
                .thenThrow(new PermissionDeniedExceptionFCHAPI01(errorMessage));

        // When & Then
        mockMvc.perform(get(BASE_URL, convId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(ErrorCodeFCHAPI01.PERMISSION_DENIED.name()))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for unhandled exceptions from service")
    void fetchConversationHistory_internalServerError() throws Exception {
        // Given
        UUID convId = VALID_CONVERSATION_ID;
        when(conversationService.fetchConversationHistory(eq(convId), any(Integer.class), any(OffsetDateTime.class)))
                .thenThrow(new RuntimeException("Something unexpected happened!"));

        // When & Then
        mockMvc.perform(get(BASE_URL, convId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(ErrorCodeFCHAPI01.INTERNAL_SERVER_ERROR.name()))
                .andExpect(jsonPath("$.message").value(ErrorCodeFCHAPI01.INTERNAL_SERVER_ERROR.getDefaultMessage()));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid 'before' date format")
    void fetchConversationHistory_beforeInvalidFormat() throws Exception {
        // Given
        UUID convId = VALID_CONVERSATION_ID;
        String invalidBefore = "2023-10-26T10:00:00"; // Missing timezone offset

        // When & Then
        mockMvc.perform(get(BASE_URL, convId)
                .param("before", invalidBefore)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCodeFCHAPI01.INVALID_PARAMETER.name()))
                .andExpect(jsonPath("$.message").value(ErrorCodeFCHAPI01.INVALID_PARAMETER.getDefaultMessage()))
                .andExpect(jsonPath("$.details[0].field").value("before"))
                .andExpect(jsonPath("$.details[0].issue").value("Invalid value for parameter 'before'. Expected type 'OffsetDateTime'."));
    }
}