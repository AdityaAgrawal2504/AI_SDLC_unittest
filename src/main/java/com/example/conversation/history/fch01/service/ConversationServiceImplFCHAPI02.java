package com.example.conversation.history.fch01.service;

import com.example.conversation.history.fch01.dto.*;
import com.example.conversation.history.fch01.enums.MessageTypeFCHAPI01;
import com.example.conversation.history.fch01.exception.PermissionDeniedExceptionFCHAPI01;
import com.example.conversation.history.fch01.exception.ResourceNotFoundExceptionFCHAPI01;
import com.example.conversation.history.fch01.logging.LoggableFCHAPI04;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service implementation for managing conversation history.
 * NOTE: This implementation uses mocked data for demonstration purposes.
 * In a real application, this would interact with a database repository.
 */
@Service
@LoggableFCHAPI04
public class ConversationServiceImplFCHAPI02 implements ConversationServiceFCHAPI01 {

    // --- Mock Data ---
    public static final UUID VALID_CONVERSATION_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final UUID UNAUTHORIZED_CONVERSATION_ID = UUID.fromString("00000000-e89b-12d3-a456-426614174000");
    public static final UUID NON_EXISTENT_CONVERSATION_ID = UUID.fromString("99999999-e89b-12d3-a456-426614174000"); // Added for explicit "not found"
    public static final UUID CURRENT_USER_ID = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");

    /**
     * Mocked fetch implementation.
     *
     * In a real system, this method would:
     * 1. Authenticate the current user (e.g., from a Spring Security context).
     * 2. Verify the conversation exists in the database.
     * 3. Check if the authenticated user is a participant of the conversation.
     * 4. Query the database for messages matching the criteria (conversationId, before cursor, limit).
     *    - The query would be something like: `SELECT * FROM messages WHERE conversation_id = ? AND created_at < ? ORDER BY created_at DESC LIMIT ?`
     * 5. Map the database entities to DTOs.
     * 6. Construct the pagination response.
     */
    @Override
    public PaginatedMessagesResponseFCHAPI01 fetchConversationHistory(UUID conversationId, int limit, OffsetDateTime before) {
        // 1. & 2. & 3. Mocked validation and authorization
        validateAndAuthorize(conversationId);

        // 4. Mocked data fetching
        // Note: For a real application, messages would be sorted descending by createdAt.
        // Our mock generation already produces them in descending order.
        List<MessageDtoFCHAPI01> allMessages = generateMockMessages(VALID_CONVERSATION_ID, 200);

        // Apply pagination filter
        // If 'before' is null, use a timestamp far in the future to get the latest messages.
        OffsetDateTime cursor = (before != null) ? before : OffsetDateTime.now(ZoneOffset.UTC).plusYears(100);

        List<MessageDtoFCHAPI01> pageMessages = allMessages.stream()
            .filter(m -> m.getCreatedAt().isBefore(cursor))
            .limit(limit + 1) // Fetch one extra item to determine if there are more pages
            .collect(Collectors.toList());

        // 5. & 6. Construct response
        boolean hasMore = pageMessages.size() > limit;
        List<MessageDtoFCHAPI01> data = hasMore ? pageMessages.subList(0, limit) : pageMessages;

        OffsetDateTime nextCursor = null;
        if (hasMore && !data.isEmpty()) {
            // The next cursor should be the createdAt of the last message on the current page.
            nextCursor = data.get(data.size() - 1).getCreatedAt();
        }

        PaginationInfoFCHAPI01 paginationInfo = new PaginationInfoFCHAPI01(nextCursor, hasMore);
        return new PaginatedMessagesResponseFCHAPI01(data, paginationInfo);
    }

    /**
     * Mocks the validation and authorization process.
     */
    private void validateAndAuthorize(UUID conversationId) {
        if (conversationId.equals(UNAUTHORIZED_CONVERSATION_ID)) {
            // Simulates the user not being a member of the conversation.
            throw new PermissionDeniedExceptionFCHAPI01("You do not have permission to access this conversation.");
        }
        if (conversationId.equals(NON_EXISTENT_CONVERSATION_ID)) {
             // Simulates the conversation not existing.
            throw new ResourceNotFoundExceptionFCHAPI01("The specified conversation could not be found.");
        }
        if (!conversationId.equals(VALID_CONVERSATION_ID) &&
            !conversationId.equals(UNAUTHORIZED_CONVERSATION_ID) &&
            !conversationId.equals(NON_EXISTENT_CONVERSATION_ID)) {
            // For any other invalid UUID not explicitly mocked
            throw new ResourceNotFoundExceptionFCHAPI01("The specified conversation could not be found.");
        }
        // If we reach here, the user is authorized for VALID_CONVERSATION_ID.
    }
    
    /**
     * Generates a list of mock messages for demonstration.
     */
    private List<MessageDtoFCHAPI01> generateMockMessages(UUID conversationId, int count) {
        List<MessageDtoFCHAPI01> messages = new ArrayList<>();
        UserSummaryDtoFCHAPI01 sender1 = UserSummaryDtoFCHAPI01.builder()
            .id(CURRENT_USER_ID)
            .displayName("Alice")
            .avatarUrl("https://example.com/avatars/alice.png")
            .build();
        UserSummaryDtoFCHAPI01 sender2 = UserSummaryDtoFCHAPI01.builder()
            .id(UUID.randomUUID())
            .displayName("Bob")
            .avatarUrl("https://example.com/avatars/bob.png")
            .build();
        
        // Generate messages in reverse chronological order
        IntStream.range(0, count).forEach(i -> {
            OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC).minusSeconds(i); // Each message 1 second older
            MessageDtoFCHAPI01 msg = MessageDtoFCHAPI01.builder()
                .id(UUID.randomUUID())
                .type(MessageTypeFCHAPI01.TEXT)
                .content("This is message number " + (count - i) + " for conversation " + conversationId)
                .sender(i % 2 == 0 ? sender1 : sender2)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .metadata(Map.of("read", "true", "seq", count - i))
                .build();
            messages.add(msg);
        });
        // Important for cursor-based pagination: messages must be sorted by createdAt descending
        messages.sort((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()));
        return messages;
    }
}
src/main/java/com/example/conversation/history/fch01/controller/ConversationControllerFCHAPI01.java