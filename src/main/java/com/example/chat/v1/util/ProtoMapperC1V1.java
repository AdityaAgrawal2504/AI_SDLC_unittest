package com.example.chat.v1.util;

import com.example.chat.v1.domain.MessageC1V1;
import com.example.chat.v1.domain.UserC1V1;
import com.example.chat.v1.grpc.Message;
import com.example.chat.v1.grpc.User;
import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Maps between internal domain models and Protobuf-generated classes.
 */
@Component
public class ProtoMapperC1V1 {

    /**
     * Converts an Instant to a Protobuf Timestamp.
     */
    public Timestamp toProtoTimestamp(Instant instant) {
        if (instant == null) {
            return Timestamp.getDefaultInstance();
        }
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    /**
     * Converts a domain User to a Protobuf User.
     */
    public User toProtoUser(UserC1V1 domainUser) {
        if (domainUser == null) {
            return User.getDefaultInstance();
        }
        return User.newBuilder()
                .setId(domainUser.getId() != null ? domainUser.getId() : "")
                .setDisplayName(domainUser.getDisplayName() != null ? domainUser.getDisplayName() : "")
                .setAvatarUrl(domainUser.getAvatarUrl() != null ? domainUser.getAvatarUrl() : "")
                .build();
    }

    /**
     * Converts a domain Message to a Protobuf Message.
     */
    public Message toProtoMessage(MessageC1V1 domainMessage, UserC1V1 sender) {
        if (domainMessage == null) {
            return Message.getDefaultInstance();
        }
        return Message.newBuilder()
                .setId(domainMessage.getId() != null ? domainMessage.getId() : "")
                .setClientMessageId(domainMessage.getClientMessageId() != null ? domainMessage.getClientMessageId() : "")
                .setConversationId(domainMessage.getConversationId() != null ? domainMessage.getConversationId() : "")
                .setSender(toProtoUser(sender))
                .setContent(domainMessage.getContent() != null ? domainMessage.getContent() : "")
                .setCreatedAt(toProtoTimestamp(domainMessage.getCreatedAt()))
                .setStatus(domainMessage.getStatus() != null ? domainMessage.getStatus() : com.example.chat.v1.grpc.MessageStatus.MESSAGE_STATUS_UNSPECIFIED)
                .build();
    }

    /**
     * Converts a Protobuf Timestamp to an Instant.
     */
    public Instant toInstant(Timestamp timestamp) {
        if (timestamp == null || (timestamp.getSeconds() == 0 && timestamp.getNanos() == 0)) {
            return null;
        }
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
```
```java
// src/main/java/com/example/chat/v1/validation/RequestValidatorC1V1.java