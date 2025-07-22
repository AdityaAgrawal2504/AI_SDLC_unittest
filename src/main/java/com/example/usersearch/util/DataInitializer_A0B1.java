package com.example.usersearch.util;

import com.example.usersearch.model.BlockedUserEntity_A0B1;
import com.example.usersearch.model.ConversationParticipantEntity_A0B1;
import com.example.usersearch.model.UserEntity_A0B1;
import com.example.usersearch.repository.BlockedUserRepository_A0B1;
import com.example.usersearch.repository.ConversationRepository_A0B1;
import com.example.usersearch.repository.UserRepository_A0B1;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Initializes the in-memory database with sample data for demonstration purposes.
 * This component runs on application startup.
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializer_A0B1 implements CommandLineRunner {

    private final UserRepository_A0B1 userRepository;
    private final BlockedUserRepository_A0B1 blockedUserRepository;
    private final ConversationRepository_A0B1 conversationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting database initialization...");

        if (userRepository.count() > 0) {
            log.info("Database already contains data. Skipping initialization.");
            return;
        }

        // --- Create Users ---
        UserEntity_A0B1 alice = createUser("Alice Smith", "+15551112222", "https://i.pravatar.cc/150?u=alice");
        UserEntity_A0B1 bob = createUser("Bob Johnson", "+15553334444", "https://i.pravatar.cc/150?u=bob");
        UserEntity_A0B1 charlie = createUser("Charlie Brown", "+15555556666", "https://i.pravatar.cc/150?u=charlie");
        UserEntity_A0B1 diana = createUser("Diana Prince", "+15557778888", "https://i.pravatar.cc/150?u=diana");
        UserEntity_A0B1 eve = createUser("Eve Adams", "+15559990000", "https://i.pravatar.cc/150?u=eve");
        UserEntity_A0B1 frank = createUser("Frank Castle", "+15552223333", "https://i.pravatar.cc/150?u=frank");

        userRepository.saveAll(List.of(alice, bob, charlie, diana, eve, frank));
        log.info("Created 6 sample users.");

        // --- Create Block Relationship ---
        // Eve has blocked Alice. So if Alice searches, she won't see Eve.
        BlockedUserEntity_A0B1 eveBlocksAlice = BlockedUserEntity_A0B1.builder()
            .blockerId(eve.getId())
            .blockedId(alice.getId())
            .build();
        blockedUserRepository.save(eveBlocksAlice);
        log.info("Eve has blocked Alice.");

        // --- Create Existing Conversation ---
        // Alice and Bob already have a 1-on-1 conversation.
        UUID conversationId = UUID.randomUUID();
        ConversationParticipantEntity_A0B1 participant1 = ConversationParticipantEntity_A0B1.builder()
            .conversationId(conversationId)
            .userId(alice.getId())
            .build();
        ConversationParticipantEntity_A0B1 participant2 = ConversationParticipantEntity_A0B1.builder()
            .conversationId(conversationId)
            .userId(bob.getId())
            .build();
        conversationRepository.saveAll(Arrays.asList(participant1, participant2));
        log.info("Created a 1-on-1 conversation between Alice and Bob.");
        
        // --- Create a group conversation to ensure it's not picked up by the 1-on-1 check
        UUID groupConversationId = UUID.randomUUID();
        conversationRepository.saveAll(List.of(
            ConversationParticipantEntity_A0B1.builder().conversationId(groupConversationId).userId(alice.getId()).build(),
            ConversationParticipantEntity_A0B1.builder().conversationId(groupConversationId).userId(charlie.getId()).build(),
            ConversationParticipantEntity_A0B1.builder().conversationId(groupConversationId).userId(diana.getId()).build()
        ));
        log.info("Created a group conversation between Alice, Charlie, and Diana.");


        log.info("Database initialization complete.");
        log.info("======================================================================================");
        log.info("Sample User for Testing: Alice Smith, Phone: +15551112222, Password: password");
        log.info("You can use Postman or cURL with a Bearer token for this user.");
        log.info("Example Search (as Alice): GET http://localhost:8080/users/search?q=bob");
        log.info("This should return Bob with 'hasExistingConversation: true'");
        log.info("Example Search (as Alice): GET http://localhost:8080/users/search?q=eve");
        log.info("This should return no results for Eve because Eve blocked Alice.");
        log.info("Example Search (as Bob): GET http://localhost:8080/users/search?q=eve");
        log.info("This should return Eve because Bob has not been blocked by Eve.");
        log.info("======================================================================================");
    }

    private UserEntity_A0B1 createUser(String name, String phone, String pfpUrl) {
        return UserEntity_A0B1.builder()
            .name(name)
            .phoneNumber(phone)
            .password(passwordEncoder.encode("password")) // All users have the same password for simplicity
            .profilePictureUrl(pfpUrl)
            .build();
    }
}
```
pom.xml
```xml