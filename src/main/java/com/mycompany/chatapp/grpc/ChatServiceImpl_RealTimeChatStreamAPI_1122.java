package com.mycompany.chatapp.grpc;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import com.mycompany.chatapp.model.AuthenticatedUser_RealTimeChatStreamAPI_1122;
import com.mycompany.chatapp.model.ChatSessionContext_RealTimeChatStreamAPI_1122;
import com.mycompany.chatapp.service.*;
import com.mycompany.chatapp.util.GrpcErrorUtil_RealTimeChatStreamAPI_1122;
import com.mycompany.chatapp.validator.RequestValidator_RealTimeChatStreamAPI_1122;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

/**
 * Implements the gRPC ChatService for real-time bi-directional communication.
 */
@GrpcService
@RequiredArgsConstructor
public class ChatServiceImpl_RealTimeChatStreamAPI_1122 extends ChatServiceGrpc.ChatServiceImplBase {

    private static final Logger logger = LogManager.getLogger(ChatServiceImpl_RealTimeChatStreamAPI_1122.class);

    private final AuthenticationService_RealTimeChatStreamAPI_1122 authService;
    private final ChatRoomService_RealTimeChatStreamAPI_1122 chatRoomService;
    private final MessageService_RealTimeChatStreamAPI_1122 messageService;
    private final ChatStreamManager_RealTimeChatStreamAPI_1122 streamManager;
    private final RequestValidator_RealTimeChatStreamAPI_1122 validator;

    /**
     * Establishes the bi-directional chat stream.
     * @param responseObserver The observer for sending responses back to the client.
     * @return An observer for receiving requests from the client.
     */
    @Override
    public StreamObserver<ClientStreamRequest> chatStream(StreamObserver<ServerStreamResponse> responseObserver) {
        return new ClientStreamRequestHandler(responseObserver);
    }

    /**
     * Inner class to handle the lifecycle of a single client's stream.
     * An instance of this class is created for each new connection.
     */
    private class ClientStreamRequestHandler implements StreamObserver<ClientStreamRequest> {

        private final StreamObserver<ServerStreamResponse> responseObserver;
        private final ChatSessionContext_RealTimeChatStreamAPI_1122 sessionContext = new ChatSessionContext_RealTimeChatStreamAPI_1122();

        public ClientStreamRequestHandler(StreamObserver<ServerStreamResponse> responseObserver) {
            this.responseObserver = responseObserver;
        }

        @Override
        public void onNext(ClientStreamRequest request) {
            long startTime = System.currentTimeMillis();
            try {
                switch (request.getPayloadCase()) {
                    case INITIAL_CONNECT -> handleInitialConnect(request.getInitialConnect());
                    case SEND_MESSAGE -> handleSendMessage(request.getSendMessage());
                    case SEND_STATUS_UPDATE -> handleSendStatusUpdate(request.getSendStatusUpdate());
                    case SEND_MESSAGE_RECEIPT -> handleSendMessageReceipt(request.getSendMessageReceipt());
                    case PAYLOAD_NOT_SET -> throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("Request payload must be set.");
                    default -> throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("Unsupported request type.");
                }
            } catch (Exception e) {
                logger.error("Error processing client request: {}", request.toString(), e);
                responseObserver.onError(GrpcErrorUtil_RealTimeChatStreamAPI_1122.internal("Failed to process request: " + e.getMessage()));
            }
            logger.info("Processed onNext for {} in {}ms", request.getPayloadCase(), (System.currentTimeMillis() - startTime));
        }

        @Override
        public void onError(Throwable t) {
            logger.warn("Client stream error for user {} in chat {}: {}", sessionContext.getUserId(), sessionContext.getChatId(), t.getMessage());
            streamManager.deregister(sessionContext.getChatId(), responseObserver);
        }

        @Override
        public void onCompleted() {
            logger.info("Client stream completed for user {} in chat {}", sessionContext.getUserId(), sessionContext.getChatId());
            streamManager.deregister(sessionContext.getChatId(), responseObserver);
            responseObserver.onCompleted();
        }

        private void handleInitialConnect(InitialConnectRequest request) {
            if (sessionContext.isConnected()) {
                throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.failedPrecondition("Stream already initialized.");
            }
            if (!validator.isRequired(request.getSessionToken())) {
                throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.unauthenticated("session_token is required.");
            }
            if (!validator.isValidUuid(request.getChatId())) {
                throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("chat_id must be a valid UUID.");
            }

            AuthenticatedUser_RealTimeChatStreamAPI_1122 user = authService.authenticate(request.getSessionToken())
                    .orElseThrow(() -> GrpcErrorUtil_RealTimeChatStreamAPI_1122.unauthenticated("Invalid session_token."));

            if (!chatRoomService.chatExists(request.getChatId())) {
                throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.notFound("chat_id does not exist.");
            }

            if (!chatRoomService.isUserMemberOfChat(user.getUserId(), request.getChatId())) {
                throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.permissionDenied("User is not a member of this chat.");
            }

            sessionContext.establishConnection(user, request.getChatId());
            streamManager.register(request.getChatId(), responseObserver);

            StreamConnectedEvent event = StreamConnectedEvent.newBuilder()
                    .setMessage("Connection successful.")
                    .setServerTime(Timestamps.fromMillis(Instant.now().toEpochMilli()))
                    .build();
            responseObserver.onNext(ServerStreamResponse.newBuilder().setStreamConnected(event).build());
        }

        private void ensureConnected() {
            if (!sessionContext.isConnected()) {
                throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.failedPrecondition("Client must send InitialConnectRequest first.");
            }
        }

        private void handleSendMessage(SendMessageRequest request) {
            ensureConnected();
            if (!validator.isValidUuid(request.getIdempotencyKey())) throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("idempotency_key must be a valid UUID.");
            if (!validator.isRequired(request.getContent()) || request.getContent().length() > 4096) throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("content is invalid.");
            if (request.hasParentMessageId() && !validator.isValidUuid(request.getParentMessageId())) throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("parent_message_id must be a valid UUID.");

            if (!messageService.claimIdempotencyKey(request.getIdempotencyKey())) {
                logger.warn("Duplicate message received with idempotency key: {}", request.getIdempotencyKey());
                return; // Silently drop duplicate
            }

            NewMessageEvent event = messageService.createNewMessage(
                sessionContext.getChatId(),
                request.getContent(),
                sessionContext.getUserId(),
                sessionContext.getDisplayName(),
                request.hasParentMessageId() ? request.getParentMessageId() : null
            );
            streamManager.broadcast(sessionContext.getChatId(), ServerStreamResponse.newBuilder().setNewMessage(event).build());
        }

        private void handleSendStatusUpdate(SendStatusUpdateRequest request) {
            ensureConnected();
            if (request.getStatus() == UserStatus.STATUS_UNSPECIFIED) throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("A valid status is required.");

            StatusUpdateEvent event = StatusUpdateEvent.newBuilder()
                    .setChatId(sessionContext.getChatId())
                    .setUserId(sessionContext.getUserId())
                    .setStatus(request.getStatus())
                    .setTimestamp(Timestamps.fromMillis(Instant.now().toEpochMilli()))
                    .build();
            streamManager.broadcast(sessionContext.getChatId(), ServerStreamResponse.newBuilder().setStatusUpdate(event).build());
        }

        private void handleSendMessageReceipt(SendMessageReceiptRequest request) {
            ensureConnected();
            if (!validator.isValidUuid(request.getMessageId())) throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("message_id must be a valid UUID.");
            if (request.getReceiptType() == ReceiptType.RECEIPT_UNSPECIFIED) throw GrpcErrorUtil_RealTimeChatStreamAPI_1122.invalidArgument("A valid receipt_type is required.");
            
            MessageReceiptEvent event = MessageReceiptEvent.newBuilder()
                    .setChatId(sessionContext.getChatId())
                    .setMessageId(request.getMessageId())
                    .setUserId(sessionContext.getUserId())
                    .setReceiptType(request.getReceiptType())
                    .setTimestamp(Timestamps.fromMillis(Instant.now().toEpochMilli()))
                    .build();
            streamManager.broadcast(sessionContext.getChatId(), ServerStreamResponse.newBuilder().setMessageReceipt(event).build());
        }
    }
}
```
```java
// src/test/java/com/mycompany/chatapp/service/ChatStreamManager_RealTimeChatStreamAPI_1122Test.java