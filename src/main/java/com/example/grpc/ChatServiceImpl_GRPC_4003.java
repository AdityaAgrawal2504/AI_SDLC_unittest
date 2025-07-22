package com.example.grpc;

import com.example.chat.v1.*;
import com.example.util.StructuredLogger_UTIL_9999;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.UUID;

/**
 * gRPC service implementation for the bi-directional chat stream.
 */
@GrpcService
@RequiredArgsConstructor
public class ChatServiceImpl_GRPC_4003 extends ChatServiceGrpc.ChatServiceImplBase {

    private final StructuredLogger_UTIL_9999 logger;
    // In a real application, this would delegate to MessageService and ConversationService.
    // This is a placeholder implementation.

    /**
     * Handles the bi-directional stream connection for real-time chat.
     */
    @Override
    public StreamObserver<ClientStreamMessage> connectStream(StreamObserver<ServerStreamMessage> responseObserver) {
        
        return new StreamObserver<>() {
            @Override
            public void onNext(ClientStreamMessage value) {
                // Get authentication from context populated by interceptor
                Authentication authentication = GrpcAuthContext_GRPC_4002.AUTHENTICATION_KEY.get();
                if (authentication == null) {
                    sendError(responseObserver, 16, "Unauthenticated");
                    responseObserver.onCompleted();
                    return;
                }
                String userId = authentication.getName();
                
                logger.info("gRPC message received from user: " + userId);

                // Handle different message types
                if (value.hasSendMessage()) {
                    handleSendMessage(value.getSendMessage(), userId, responseObserver);
                } else if (value.hasReadReceipt()) {
                    handleReadReceipt(value.getReadReceipt(), userId, responseObserver);
                } else if (value.hasTypingIndicator()) {
                    // Handle typing...
                } else {
                    sendError(responseObserver, 3, "Invalid message type");
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.error("gRPC stream error: " + t.getMessage(), t);
            }

            @Override
            public void onCompleted() {
                logger.info("gRPC stream completed by client.");
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * Handles a new message payload from the client.
     */
    private void handleSendMessage(SendMessagePayload payload, String senderId, StreamObserver<ServerStreamMessage> responseObserver) {
        // Mock implementation: echo the message back as a NewMessagePayload
        // In a real app: save to DB, find recipient, push to their stream
        logger.info(String.format("User %s sent message to convo %s: '%s'", senderId, payload.getConversationId(), payload.getContent()));

        NewMessagePayload newMessage = NewMessagePayload.newBuilder()
                .setMessageId(UUID.randomUUID().toString())
                .setConversationId(payload.getConversationId())
                .setSenderId(senderId)
                .setContent("Echo: " + payload.getContent())
                .setTimestamp(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
                .build();
        
        ServerStreamMessage serverMessage = ServerStreamMessage.newBuilder()
                .setNewMessage(newMessage)
                .build();

        responseObserver.onNext(serverMessage);
    }
    
    /**
     * Handles a read receipt from the client.
     */
    private void handleReadReceipt(ReadReceiptPayload payload, String userId, StreamObserver<ServerStreamMessage> responseObserver) {
        logger.info(String.format("User %s read convo %s", userId, payload.getConversationId()));
        // In a real app: update message statuses to 'SEEN' in the DB and notify sender.
        // For now, we can send a mock status update.
        MessageStatusUpdate statusUpdate = MessageStatusUpdate.newBuilder()
                .setMessageId(UUID.randomUUID().toString()) // Should be an actual message ID
                .setStatus(MessageStatusUpdate.Status.SEEN)
                .setUserIdWhoUpdated(userId)
                .build();

        ServerStreamMessage serverMessage = ServerStreamMessage.newBuilder()
                .setStatusUpdate(statusUpdate)
                .build();
        responseObserver.onNext(serverMessage);
    }

    /**
     * Sends a stream error back to the client.
     */
    private void sendError(StreamObserver<ServerStreamMessage> observer, int code, String message) {
        StreamError error = StreamError.newBuilder().setCode(code).setMessage(message).build();
        observer.onNext(ServerStreamMessage.newBuilder().setError(error).build());
    }
}
```
```java
//
// Filename: src/main/java/com/example/repository/UserRepository_UATH_1017.java
//