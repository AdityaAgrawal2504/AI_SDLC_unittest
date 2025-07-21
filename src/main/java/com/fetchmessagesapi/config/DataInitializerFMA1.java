package com.fetchmessagesapi.config;

import com.fetchmessagesapi.entity.ConversationFMA1;
import com.fetchmessagesapi.entity.ConversationParticipantFMA1;
import com.fetchmessagesapi.entity.MessageFMA1;
import com.fetchmessagesapi.enums.ContentTypeFMA1;
import com.fetchmessagesapi.enums.SenderTypeFMA1;
import com.fetchmessagesapi.repository.ConversationParticipantRepositoryFMA1;
import com.fetchmessagesapi.repository.ConversationRepositoryFMA1;
import com.fetchmessagesapi.repository.MessageRepositoryFMA1;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

/**
 * Initializes the database with sample data for demonstration purposes.
 */
@Component
@RequiredArgsConstructor
public class DataInitializerFMA1 implements CommandLineRunner {

    private final ConversationRepositoryFMA1 conversationRepository;
    private final MessageRepositoryFMA1 messageRepository;
    private final ConversationParticipantRepositoryFMA1 participantRepository;

    // Use a fixed UUID from the test security config for predictability
    private final UUID testUserId = UUID.fromString("e9f2f8f8-c2b1-4a1e-843c-ae742d4a6a9b");
    private final UUID otherUserId = UUID.randomUUID();

    @Override
    public void run(String... args) throws Exception {
        // Create a conversation
        ConversationFMA1 conv = new ConversationFMA1();
        conversationRepository.save(conv);

        // Add participants
        ConversationParticipantFMA1 p1 = new ConversationParticipantFMA1();
        p1.setConversation(conv);
        p1.setUserId(testUserId);
        participantRepository.save(p1);

        ConversationParticipantFMA1 p2 = new ConversationParticipantFMA1();
        p2.setConversation(conv);
        p2.setUserId(otherUserId);
        participantRepository.save(p2);

        // Add messages
        for (int i = 1; i <= 120; i++) {
            MessageFMA1 msg = new MessageFMA1();
            msg.setConversationId(conv.getId());
            msg.setContentType(ContentTypeFMA1.TEXT);
            msg.setTextContent("This is message number " + i);
            msg.setCreatedAt(Instant.now().minusSeconds(i * 60L)); // Stagger timestamps

            if (i % 2 == 0) {
                msg.setSenderId(testUserId);
                msg.setSenderDisplayName("Test User");
                msg.setSenderType(SenderTypeFMA1.USER);
            } else {
                msg.setSenderId(otherUserId);
                msg.setSenderDisplayName("Other User");
                msg.setSenderType(SenderTypeFMA1.USER);
            }
            messageRepository.save(msg);
        }

        // Create a conversation the user cannot access
        ConversationFMA1 restrictedConv = new ConversationFMA1();
        conversationRepository.save(restrictedConv);
        ConversationParticipantFMA1 p3 = new ConversationParticipantFMA1();
        p3.setConversation(restrictedConv);
        p3.setUserId(UUID.randomUUID());
        participantRepository.save(p3);
    }
}

```