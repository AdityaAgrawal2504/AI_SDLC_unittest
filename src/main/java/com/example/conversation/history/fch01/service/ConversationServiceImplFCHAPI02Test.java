package com.example.conversation.history.fch01.service;

import com.example.conversation.history.fch01.dto.MessageDtoFCHAPI01;
import com.example.conversation.history.fch01.dto.PaginatedMessagesResponseFCHAPI01;
import com.example.conversation.history.fch01.exception.PermissionDeniedExceptionFCHAPI01;
import com.example.conversation.history.fch01.exception.ResourceNotFoundExceptionFCHAPI01;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static com.example.conversation.history.fch01.service.ConversationServiceImplFCHAPI02.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConversationServiceImplFCHAPI02 Unit Tests")
class ConversationServiceImplFCHAPI02Test {

    @InjectMocks
    private ConversationServiceImplFCHAPI02 conversationService;

    @BeforeEach
    void setUp() {
        // No explicit setup needed for this mock service as it generates its own data.
        // If it had real dependencies (e.g., a repository), they would be mocked here.
    }

    @Test
    @DisplayName("Should fetch the first page of messages successfully with default limit")
    void fetchConversationHistory_success_firstPage() {
        // Given
        int defaultLimit = 20;

        // When
        PaginatedMessagesResponseFCHAPI01 response = conversationService.fetchConversationHistory(
                VALID_CONVERSATION_ID, defaultLimit, null);

        // Then
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(defaultLimit, response.getData().size(), "Should return the default number of messages");
        assertNotNull(response.getPagination());
        assertTrue(response.getPagination().isHasMore(), "Should have more messages");
        assertNotNull(response.getPagination().getNextCursor(), "Next cursor should not be null");

        // Verify messages are in reverse chronological order
        OffsetDateTime firstMsgTime = response.getData().get(0).getCreatedAt();
        OffsetDateTime lastMsgTime = response.getData().get(response.getData().size() - 1).getCreatedAt();
        assertTrue(firstMsgTime.isAfter(lastMsgTime), "Messages should be sorted newest first");
    }

    @Test
    @DisplayName("Should fetch messages successfully with a specified limit and before cursor")
    void fetchConversationHistory_success_withBeforeCursor() {
        // Given
        int limit = 10;
        // Get a cursor from a mock call to ensure it's valid for the mock data
        PaginatedMessagesResponseFCHAPI01 initialResponse = conversationService.fetchConversationHistory(
                VALID_CONVERSATION_ID, limit, null);
        OffsetDateTime beforeCursor = initialResponse.getPagination().getNextCursor();

        // When
        PaginatedMessagesResponseFCHAPI01 response = conversationService.fetchConversationHistory(
                VALID_CONVERSATION_ID, limit, beforeCursor);

        // Then
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(limit, response.getData().size(), "Should return the specified number of messages");
        assertNotNull(response.getPagination());
        assertTrue(response.getPagination().isHasMore(), "Should have more messages");
        assertNotNull(response.getPagination().getNextCursor(), "Next cursor should not be null");

        // Verify all fetched messages are older than the cursor
        assertTrue(response.getData().stream().allMatch(m -> m.getCreatedAt().isBefore(beforeCursor)),
                "All messages should be older than the 'before' cursor");
    }

    @Test
    @DisplayName("Should correctly handle fetching the last page of messages")
    void fetchConversationHistory_success_lastPage() {
        // Given
        int totalMessages = 200; // Based on mock data generation
        int limit = 50;

        // Simulate fetching all pages until the last one
        OffsetDateTime currentCursor = OffsetDateTime.now(ZoneOffset.UTC).plusYears(100); // Start very early
        PaginatedMessagesResponseFCHAPI01 response = null;
        for (int i = 0; i < (totalMessages / limit) + 1; i++) {
            response = conversationService.fetchConversationHistory(VALID_CONVERSATION_ID, limit, currentCursor);
            if (!response.getPagination().isHasMore()) {
                break;
            }
            currentCursor = response.getPagination().getNextCursor();
        }

        // Then
        assertNotNull(response);
        assertNotNull(response.getData());
        // The last page might have fewer than `limit` messages
        assertTrue(response.getData().size() <= limit, "Last page should have at most the limit number of messages");
        assertFalse(response.getPagination().isHasMore(), "Should indicate no more messages");
        assertNull(response.getPagination().getNextCursor(), "Next cursor should be null on the last page");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for a non-existent conversation ID")
    void fetchConversationHistory_resourceNotFound() {
        // Given a non-existent conversation ID
        UUID nonExistentId = NON_EXISTENT_CONVERSATION_ID;

        // When/Then
        assertThrows(ResourceNotFoundExceptionFCHAPI01.class, () ->
                        conversationService.fetchConversationHistory(nonExistentId, 20, null),
                "Should throw ResourceNotFoundException for non-existent ID");
    }

    @Test
    @DisplayName("Should throw PermissionDeniedException for an unauthorized conversation ID")
    void fetchConversationHistory_permissionDenied() {
        // Given an unauthorized conversation ID
        UUID unauthorizedId = UNAUTHORIZED_CONVERSATION_ID;

        // When/Then
        assertThrows(PermissionDeniedExceptionFCHAPI01.class, () ->
                        conversationService.fetchConversationHistory(unauthorizedId, 20, null),
                "Should throw PermissionDeniedException for unauthorized ID");
    }

    @Test
    @DisplayName("Should return empty data and no more pages if 'before' cursor is too old")
    void fetchConversationHistory_beforeCursorTooOld() {
        // Given
        OffsetDateTime veryOldCursor = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC); // Before any mock data

        // When
        PaginatedMessagesResponseFCHAPI01 response = conversationService.fetchConversationHistory(
                VALID_CONVERSATION_ID, 20, veryOldCursor);

        // Then
        assertNotNull(response);
        assertTrue(response.getData().isEmpty(), "Should return empty list if cursor is too old");
        assertNotNull(response.getPagination());
        assertFalse(response.getPagination().isHasMore(), "Should indicate no more messages");
        assertNull(response.getPagination().getNextCursor(), "Next cursor should be null");
    }

    @Test
    @DisplayName("Should return all messages if limit is larger than total available")
    void fetchConversationHistory_limitLargerThanAvailable() {
        // Given
        int largeLimit = 500; // Larger than 200 mock messages

        // When
        PaginatedMessagesResponseFCHAPI01 response = conversationService.fetchConversationHistory(
                VALID_CONVERSATION_ID, largeLimit, null);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getData().size(), "Should return all available mock messages (200)");
        assertNotNull(response.getPagination());
        assertFalse(response.getPagination().isHasMore(), "Should indicate no more messages");
        assertNull(response.getPagination().getNextCursor(), "Next cursor should be null");
    }
}
src/test/java/com/example/conversation/history/fch01/controller/ConversationControllerFCHAPI01Test.java