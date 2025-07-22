package com.example.chat.v1.service;

import com.example.chat.v1.domain.MessageC1V1;
import com.example.chat.v1.domain.UserC1V1;
import com.example.chat.v1.grpc.MessageStatus;
import com.example.chat.v1.util.IdGeneratorC1V1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MockDataRepositoryC1V1Test {

    @Mock
    private IdGeneratorC1V1 idGenerator;

    @InjectMocks
    private MockDataRepositoryC1V1 mockDataRepository;

    private String user1Id, user2Id, conv1Id, conv2Id;

    @BeforeEach
    void setUp() {
        // Setup mock ID generation for deterministic tests
        when(idGenerator.newUserId()).thenReturn("user_id_1", "user_id_2", "user_id_3");
        when(idGenerator.newConversationId()).thenReturn("conv_id_1", "conv_id_2");
        when(idGenerator.newMessageId()).thenReturn("msg_id_1", "msg_id_2");

        // Initialize the repository. This will call @PostConstruct init().
        mockDataRepository.init();

        user1Id = "user_id_1";
        user2Id = "user_id_2";
        conv1Id = "conv_id_1";
        conv2Id = "conv_id_2";
    }

    @Test
    void init_PopulatesInitialData() {
        // Assert users are created
        assertTrue(mockDataRepository.findUserById(user1Id).isPresent());
        assertTrue(mockDataRepository.findUserById(user2Id).isPresent());
        assertTrue(mockDataRepository.findUserById("user_id_3").isPresent()); // Charlie

        // Assert conversations are created
        assertTrue(mockDataRepository.conversationExists(conv1Id));
        assertTrue(mockDataRepository.conversationExists(conv2Id));
        
        // Assert conversation memberships
        assertTrue(mockDataRepository.isUserMemberOfConversation(user1Id, conv1Id));
        assertTrue(mockDataRepository.isUserMemberOfConversation(user2Id, conv1Id));
        assertFalse(mockDataRepository.isUserMemberOfConversation("user_id_3", conv1Id));

        assertTrue(mockDataRepository.isUserMemberOfConversation(user1Id, conv2Id));
        assertTrue(mockDataRepository.isUserMemberOfConversation("user_id_3", conv2Id));
        assertFalse(mockDataRepository.isUserMemberOfConversation(user2Id, conv2Id));

        // Assert initial messages
        assertTrue(mockDataRepository.findMessageByClientMessageId("mock_client_msg_1")
                .isPresent() || mockDataRepository.findMessageByClientMessageId("mock_client_msg_2").isPresent());
        // Note: the original init uses UUID.randomUUID(), so we can't assert specific client IDs without modifying init().
        // For this test, verifying presence of any messages is sufficient if init uses UUID.randomUUID().
        // If it used fixed strings, we'd verify those.
    }

    @Test
    void findUserById_ExistingUser_ReturnsUser() {
        Optional<UserC1V1> user = mockDataRepository.findUserById(user1Id);
        assertTrue(user.isPresent());
        assertEquals("Alice", user.get().getDisplayName());
    }

    @Test
    void findUserById_NonExistingUser_ReturnsEmptyOptional() {
        Optional<UserC1V1> user = mockDataRepository.findUserById("non_existent_user");
        assertFalse(user.isPresent());
    }

    @Test
    void saveMessage_NewMessage_SavesAndIndexes() {
        String testClientMsgId = UUID.randomUUID().toString();
        String testMsgId = "test_msg_id";
        when(idGenerator.newMessageId()).thenReturn(testMsgId);

        MessageC1V1 newMessage = MessageC1V1.builder()
                .clientMessageId(testClientMsgId)
                .conversationId(conv1Id)
                .senderId(user1Id)
                .content("Test message")
                .createdAt(Instant.now())
                .status(MessageStatus.SENT)
                .build();

        MessageC1V1 savedMessage = mockDataRepository.saveMessage(newMessage);

        assertNotNull(savedMessage.getId());
        assertEquals(testMsgId, savedMessage.getId());
        assertTrue(mockDataRepository.findMessageById(testMsgId).isPresent());
        assertTrue(mockDataRepository.findMessageByClientMessageId(testClientMsgId).isPresent());
        assertEquals(savedMessage, mockDataRepository.findMessageById(testMsgId).get());
    }

    @Test
    void findMessageByClientMessageId_ExistingMessage_ReturnsMessage() {
        String clientMsgId = "client_id_for_test";
        String msgId = "test_message_1";
        MessageC1V1 message = MessageC1V1.builder()
                .id(msgId)
                .clientMessageId(clientMsgId)
                .conversationId(conv1Id)
                .senderId(user1Id)
                .content("Hey")
                .createdAt(Instant.now())
                .status(MessageStatus.SENT)
                .build();
        mockDataRepository.saveMessage(message); // Using the actual save to populate internal maps

        Optional<MessageC1V1> foundMessage = mockDataRepository.findMessageByClientMessageId(clientMsgId);
        assertTrue(foundMessage.isPresent());
        assertEquals(message, foundMessage.get());
    }

    @Test
    void findMessageByClientMessageId_NonExistingMessage_ReturnsEmptyOptional() {
        Optional<MessageC1V1> foundMessage = mockDataRepository.findMessageByClientMessageId("non_existent_client_id");
        assertFalse(foundMessage.isPresent());
    }

    @Test
    void isUserMemberOfConversation_UserIsMember_ReturnsTrue() {
        assertTrue(mockDataRepository.isUserMemberOfConversation(user1Id, conv1Id));
    }

    @Test
    void isUserMemberOfConversation_UserIsNotMember_ReturnsFalse() {
        assertFalse(mockDataRepository.isUserMemberOfConversation("user_id_3", conv1Id));
    }

    @Test
    void isUserMemberOfConversation_ConversationDoesNotExist_ReturnsFalse() {
        assertFalse(mockDataRepository.isUserMemberOfConversation(user1Id, "non_existent_conv"));
    }

    @Test
    void conversationExists_ExistingConversation_ReturnsTrue() {
        assertTrue(mockDataRepository.conversationExists(conv1Id));
    }

    @Test
    void conversationExists_NonExistingConversation_ReturnsFalse() {
        assertFalse(mockDataRepository.conversationExists("non_existent_conv"));
    }

    @Test
    void getConversationMemberIds_ExistingConversation_ReturnsMembers() {
        List<String> members = mockDataRepository.getConversationMemberIds(conv1Id);
        assertNotNull(members);
        assertEquals(2, members.size());
        assertTrue(members.contains(user1Id));
        assertTrue(members.contains(user2Id));
    }

    @Test
    void getConversationMemberIds_NonExistingConversation_ReturnsEmptyList() {
        List<String> members = mockDataRepository.getConversationMemberIds("non_existent_conv");
        assertNotNull(members);
        assertTrue(members.isEmpty());
    }

    @Test
    void getAllUsers_ReturnsAllUsers() {
        Collection<UserC1V1> users = mockDataRepository.getAllUsers();
        assertEquals(3, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(user1Id)));
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(user2Id)));
        assertTrue(users.stream().anyMatch(u -> u.getId().equals("user_id_3")));
    }

    @Test
    void getAllConversationIds_ReturnsAllConversationIds() {
        Collection<String> convIds = mockDataRepository.getAllConversationIds();
        assertEquals(2, convIds.size());
        assertTrue(convIds.contains(conv1Id));
        assertTrue(convIds.contains(conv2Id));
    }
}
```
```java
// src/test/java/com/example/chat/v1/service/UserStreamManagerC1V1Test.java