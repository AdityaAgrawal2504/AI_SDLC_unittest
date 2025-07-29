package com.example.service.impl;

import com.example.exception.ResourceNotFoundException;
import com.example.model.Conversation;
import com.example.model.User;
import com.example.repository.IConversationRepository;
import com.example.repository.IUserRepository;
import com.example.service.IConversationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ConversationService implements IConversationService {

    private final IConversationRepository conversationRepository;
    private final IUserRepository userRepository;

    public ConversationService(IConversationRepository conversationRepository, IUserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a paginated list of conversations for a user.
     * @param userId The user's ID.
     * @param pageable Pagination information.
     * @return A Page of Conversations.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Conversation> getConversationsForUser(UUID userId, Pageable pageable) {
        return conversationRepository.findByParticipantId(userId, pageable);
    }
    
    /**
     * Finds a conversation by its ID.
     * @param conversationId The ID of the conversation.
     * @return An Optional containing the Conversation if found.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Conversation> findById(UUID conversationId) {
        return conversationRepository.findById(conversationId);
    }
    
    /**
     * Checks if a user is a participant in a given conversation.
     * @param conversationId The conversation's ID.
     * @param userId The user's ID.
     * @return true if the user is a participant, false otherwise.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isUserParticipant(UUID conversationId, UUID userId) {
        return conversationRepository.findById(conversationId)
            .map(c -> c.getParticipants().stream().anyMatch(p -> p.getId().equals(userId)))
            .orElse(false);
    }
    
    /**
     * Marks all messages in a conversation as read for a user.
     * Note: The actual logic for "read" status often lives in a separate join table (e.g., ReadReceipts)
     * for performance. This is a simplified implementation.
     * @param userId The user marking the conversation as read.
     * @param conversationId The conversation to mark.
     * @param timestamp The timestamp of the last read message.
     */
    @Override
    @Transactional
    public void markConversationAsRead(UUID userId, UUID conversationId, OffsetDateTime timestamp) {
        // In a real application, you would update a "lastReadTimestamp" for the user-conversation pair.
        // This could be stored in the Participant join table/entity if it's modeled as such.
        // For this spec, the action is acknowledged but has no persistent state change in this simplified model.
    }
    
    /**
     * Finds an existing private conversation between two users or creates a new one if none exists.
     * @param user1Id ID of the first user.
     * @param user2Id ID of the second user.
     * @return The existing or newly created Conversation.
     */
    @Override
    @Transactional
    public Conversation findOrCreateConversation(UUID user1Id, UUID user2Id) {
        return conversationRepository.findPrivateConversationBetweenUsers(user1Id, user2Id)
                .orElseGet(() -> {
                    User user1 = userRepository.findById(user1Id).orElseThrow(() -> new ResourceNotFoundException("User", "id", user1Id));
                    User user2 = userRepository.findById(user2Id).orElseThrow(() -> new ResourceNotFoundException("User", "id", user2Id));

                    Conversation newConversation = new Conversation();
                    newConversation.setParticipants(Set.of(user1, user2));
                    return conversationRepository.save(newConversation);
                });
    }
}