package com.example.chat.v1.util;

import com.example.chat.v1.domain.MessageC1V1;
import com.example.chat.v1.domain.UserC1V1;
import com.example.chat.v1.grpc.Message;
import com.example.chat.v1.grpc.MessageStatus;
import com.example.chat.v1.grpc.User;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ProtoMapperC1V1Test {

    private ProtoMapperC1V1 protoMapper;

    @BeforeEach
    void setUp() {
        protoMapper = new ProtoMapperC1V1();
    }

    @Test
    void toProtoTimestamp_ValidInstant_ReturnsCorrectTimestamp() {
        Instant now = Instant.now();
        Timestamp timestamp = protoMapper.toProtoTimestamp(now);

        assertNotNull(timestamp);
        assertEquals(now.getEpochSecond(), timestamp.getSeconds());
        assertEquals(now.getNano(), timestamp.getNanos());
    }

    @Test
    void toProtoTimestamp_NullInstant_ReturnsDefaultInstance() {
        Timestamp timestamp = protoMapper.toProtoTimestamp(null);
        assertNotNull(timestamp);
        assertEquals(Timestamp.getDefaultInstance(), timestamp);
    }

    @Test
    void toInstant_ValidTimestamp_ReturnsCorrectInstant() {
        Instant originalInstant = Instant.now();
        Timestamp protoTimestamp = protoMapper.toProtoTimestamp(originalInstant);
        
        Instant convertedInstant = protoMapper.toInstant(protoTimestamp);
        
        assertNotNull(convertedInstant);
        assertEquals(originalInstant.getEpochSecond(), convertedInstant.getEpochSecond());
        assertEquals(originalInstant.getNano(), convertedInstant.getNano());
    }

    @Test
    void toInstant_DefaultTimestamp_ReturnsNull() {
        Timestamp defaultTimestamp = Timestamp.getDefaultInstance();
        Instant convertedInstant = protoMapper.toInstant(defaultTimestamp);
        assertNull(convertedInstant);
    }

    @Test
    void toInstant_NullTimestamp_ReturnsNull() {
        Instant convertedInstant = protoMapper.toInstant(null);
        assertNull(convertedInstant);
    }

    @Test
    void toProtoUser_ValidDomainUser_ReturnsCorrectProtoUser() {
        UserC1V1 domainUser = new UserC1V1("user1", "John Doe", "http://example.com/avatar.jpg");
        User protoUser = protoMapper.toProtoUser(domainUser);

        assertNotNull(protoUser);
        assertEquals("user1", protoUser.getId());
        assertEquals("John Doe", protoUser.getDisplayName());
        assertEquals("http://example.com/avatar.jpg", protoUser.getAvatarUrl());
    }

    @Test
    void toProtoUser_NullDomainUser_ReturnsDefaultInstance() {
        User protoUser = protoMapper.toProtoUser(null);
        assertNotNull(protoUser);
        assertEquals(User.getDefaultInstance(), protoUser);
    }

    @Test
    void toProtoUser_DomainUserWithNullFields_ReturnsProtoUserWithEmptyStrings() {
        UserC1V1 domainUser = new UserC1V1(null, null, null);
        User protoUser = protoMapper.toProtoUser(domainUser);

        assertNotNull(protoUser);
        assertTrue(protoUser.getId().isEmpty());
        assertTrue(protoUser.getDisplayName().isEmpty());
        assertTrue(protoUser.getAvatarUrl().isEmpty());
    }

    @Test
    void toProtoMessage_ValidDomainMessageAndSender_ReturnsCorrectProtoMessage() {
        UserC1V1 sender = new UserC1V1("user1", "John Doe", "http://example.com/avatar.jpg");
        Instant createdAt = Instant.now();
        MessageC1V1 domainMessage = MessageC1V1.builder()
                .id("msg123")
                .clientMessageId("clientMsg1")
                .conversationId("convABC")
                .senderId(sender.getId())
                .content("Hello chat")
                .createdAt(createdAt)
                .status(MessageStatus.SENT)
                .build();

        Message protoMessage = protoMapper.toProtoMessage(domainMessage, sender);

        assertNotNull(protoMessage);
        assertEquals("msg123", protoMessage.getId());
        assertEquals("clientMsg1", protoMessage.getClientMessageId());
        assertEquals("convABC", protoMessage.getConversationId());
        assertEquals("Hello chat", protoMessage.getContent());
        assertEquals(MessageStatus.SENT, protoMessage.getStatus());
        assertEquals(sender.getId(), protoMessage.getSender().getId());
        assertEquals(protoMapper.toProtoTimestamp(createdAt), protoMessage.getCreatedAt());
    }

    @Test
    void toProtoMessage_NullDomainMessage_ReturnsDefaultInstance() {
        UserC1V1 sender = new UserC1V1("user1", "John Doe", "http://example.com/avatar.jpg");
        Message protoMessage = protoMapper.toProtoMessage(null, sender);
        assertNotNull(protoMessage);
        assertEquals(Message.getDefaultInstance(), protoMessage);
    }

    @Test
    void toProtoMessage_DomainMessageWithNullFields_ReturnsProtoMessageWithDefaultsOrEmptyStrings() {
        UserC1V1 sender = new UserC1V1("user1", "John Doe", "http://example.com/avatar.jpg");
        MessageC1V1 domainMessage = MessageC1V1.builder()
                .id(null).clientMessageId(null).conversationId(null)
                .senderId(null).content(null).createdAt(null)
                .status(null)
                .build();
        
        Message protoMessage = protoMapper.toProtoMessage(domainMessage, sender);
        
        assertNotNull(protoMessage);
        assertTrue(protoMessage.getId().isEmpty());
        assertTrue(protoMessage.getClientMessageId().isEmpty());
        assertTrue(protoMessage.getConversationId().isEmpty());
        assertTrue(protoMessage.getContent().isEmpty());
        assertEquals(MessageStatus.MESSAGE_STATUS_UNSPECIFIED, protoMessage.getStatus());
        assertNotNull(protoMessage.getCreatedAt()); // Default timestamp
        assertEquals(sender.getId(), protoMessage.getSender().getId()); // Sender should still be mapped
    }

    @Test
    void toProtoMessage_NullSender_ReturnsProtoMessageWithDefaultSender() {
        Instant createdAt = Instant.now();
        MessageC1V1 domainMessage = MessageC1V1.builder()
                .id("msg123")
                .clientMessageId("clientMsg1")
                .conversationId("convABC")
                .senderId("user1")
                .content("Hello chat")
                .createdAt(createdAt)
                .status(MessageStatus.SENT)
                .build();

        Message protoMessage = protoMapper.toProtoMessage(domainMessage, null);
        
        assertNotNull(protoMessage);
        assertEquals(User.getDefaultInstance(), protoMessage.getSender());
    }
}
```
```java
// src/test/java/com/example/chat/v1/validation/RequestValidatorC1V1Test.java