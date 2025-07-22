package com.example.conversation.api.service;

import com.example.conversation.api.dto.*;
import com.example.conversation.api.enums.SortByFCA1;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Service implementation for fetching user conversations with mock data.
 */
@Service
public class ConversationServiceImplFCA1 implements ConversationServiceFCA1 {

    private static final List<ConversationFCA1> MOCK_CONVERSATIONS = new CopyOnWriteArrayList<>();
    private static final UUID CURRENT_USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID OTHER_USER_ID_1 = UUID.fromString("a1b2c3d4-e5f6-7890-1234-567890abcdef");
    private static final UUID OTHER_USER_ID_2 = UUID.fromString("fedcba09-8765-4321-fedc-ba0987654321");

    // Static initializer to populate mock data
    static {
        MOCK_CONVERSATIONS.addAll(IntStream.range(0, 150).mapToObj(i -> {
            boolean isSeen = (i % 3 == 0);
            int unreadCount = isSeen ? 0 : (i % 5 + 1);
            OffsetDateTime lastUpdate = OffsetDateTime.now(ZoneOffset.UTC).minusHours(i);
            UUID lastSender = (i % 2 == 0) ? CURRENT_USER_ID : OTHER_USER_ID_1;

            return new ConversationFCA1(
                UUID.randomUUID(),
                "Group Chat " + (i + 1),
                List.of(
                    new ParticipantFCA1(CURRENT_USER_ID, "Current User", null),
                    new ParticipantFCA1(OTHER_USER_ID_1, "Alice", null),
                    new ParticipantFCA1(OTHER_USER_ID_2, "Bob " + i, null)
                ),
                new LastMessageSummaryFCA1(
                    UUID.randomUUID(),
                    "This is the last message content for conversation " + i + ". Search for 'message' or 'test'.",
                    lastSender,
                    lastUpdate
                ),
                unreadCount,
                isSeen,
                lastUpdate
            );
        }).toList());
    }

    /**
     * Fetches conversations, applying filtering, sorting, and pagination to the mock data.
     * Note: The current mock data doesn't filter by `userId` explicitly, assuming the mock data
     * already pertains to the `CURRENT_USER_ID`. In a real scenario, a database query would
     * filter by `userId`.
     */
    @Override
    public ConversationListResponseFCA1 fetchConversations(UUID userId, String searchQuery, SortByFCA1 sortBy, int page, int pageSize) {
        Stream<ConversationFCA1> conversationStream = MOCK_CONVERSATIONS.stream();

        // 1. Filter by search query
        if (searchQuery != null && !searchQuery.isBlank()) {
            final String lowerCaseSearchQuery = searchQuery.toLowerCase();
            conversationStream = conversationStream.filter(c ->
                c.lastMessage().text().toLowerCase().contains(lowerCaseSearchQuery) ||
                c.title() != null && c.title().toLowerCase().contains(lowerCaseSearchQuery) ||
                c.participants().stream().anyMatch(p -> p.displayName().toLowerCase().contains(lowerCaseSearchQuery))
            );
        }

        // 2. Sort
        Comparator<ConversationFCA1> comparator = switch (sortBy) {
            case RECENCY -> Comparator.comparing(ConversationFCA1::updatedAt).reversed();
            // For 'SEEN', unseen conversations (isSeen = false) should come first, then by recency.
            case SEEN -> Comparator.comparing(ConversationFCA1::isSeen)
                .thenComparing(ConversationFCA1::updatedAt, Comparator.reverseOrder());
        };
        List<ConversationFCA1> sortedConversations = conversationStream.sorted(comparator).toList();

        // 3. Paginate
        long totalItems = sortedConversations.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int correctedPage = Math.max(1, page); // Ensure page is at least 1

        List<ConversationFCA1> paginatedData = sortedConversations.stream()
            .skip((long) (correctedPage - 1) * pageSize)
            .limit(pageSize)
            .collect(Collectors.toList());

        PaginationInfoFCA1 pagination = new PaginationInfoFCA1(correctedPage, pageSize, totalItems, totalPages);

        return new ConversationListResponseFCA1(paginatedData, pagination);
    }
}
src/main/java/com.example/conversation/api/controller/ConversationControllerFCA1.java