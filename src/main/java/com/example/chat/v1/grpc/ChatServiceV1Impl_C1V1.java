package com.example.chat.v1.grpc;

import com.example.chat.v1.logging.AbstractLoggerC1V1;
import com.example.chat.v1.service.ChatLogicServiceC1V1;
import com.example.chat.v1.service.EventDispatchServiceC1V1;
import com.example.chat.v1.service.UserStreamManagerC1V1;
import com.example.chat.v1.util.ProtoMapperC1V1;
import com.example.chat.v1.validation.RequestValidatorC1V1;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Map;

import static com.example.chat.v1.util.ConstantsC1V1.AUTH_USER_ID_CONTEXT_KEY;

/**
 * <!--
 * mermaid
 * graph TD
 *     A[Client] -- gRPC BiDi Stream --> B(ChatServiceV1Impl_C1V1);
 *     B -- Auth --> C(AuthInterceptor_C1V1);
 *     C -- Validated --> B;
 *     B -- New Connection --> D(UserStreamManager_C1V1);
 *     B -- ClientToServerEvent --> E(ClientToServerStreamObserverC1V1);
 *     E -- Validate --> F(RequestValidatorC1V1);
 *     E -- Process --> G(ChatLogicServiceC1V1);
 *     G -- Dispatch Event --> H(EventDispatchServiceC1V1);
 *     H -- Send to User --> D;
 *     D -- ServerToClientEvent --> A;
 * -->
 *
 * Implements the gRPC ChatService for bi-directional streaming.
 */
@GrpcService
public class ChatServiceV1Impl_C1V1 extends ChatServiceGrpc.ChatServiceImplBase {
    
    private final UserStreamManagerC1V1 userStreamManager;
    private final RequestValidatorC1V1 requestValidator;
    private final ChatLogicServiceC1V1 chatLogicService;
    private final EventDispatchServiceC1V1 eventDispatchService;
    private final ProtoMapperC1V1 protoMapper;
    private final AbstractLoggerC1V1 logger;

    @Autowired
    public ChatServiceV1Impl_C1V1(
        UserStreamManagerC1V1 userStreamManager, 
        RequestValidatorC1V1 requestValidator, 
        ChatLogicServiceC1V1 chatLogicService, 
        EventDispatchServiceC1V1 eventDispatchService, 
        ProtoMapperC1V1 protoMapper,
        AbstractLoggerC1V1 logger) {
        this.userStreamManager = userStreamManager;
        this.requestValidator = requestValidator;
        this.chatLogicService = chatLogicService;
        this.eventDispatchService = eventDispatchService;
        this.protoMapper = protoMapper;
        this.logger = logger;
    }

    /**
     * Establishes the bi-directional message stream.
     * This method is called once per client connection.
     * It returns a new StreamObserver to handle messages from the client.
     */
    @Override
    public StreamObserver<ClientToServerEvent> messageStream(StreamObserver<ServerToClientEvent> responseObserver) {
        String userId = AUTH_USER_ID_CONTEXT_KEY.get();
        if (userId == null || userId.isEmpty()) {
            // This should have been caught by the interceptor, but as a safeguard:
            logger.error("User ID not found in gRPC context for new stream connection. Closing stream.");
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("User ID not found in context.").asRuntimeException());
            return new NoOpStreamObserver<>();
        }

        logger.logInfo("New stream connection initiated", Map.of("userId", userId));

        // Register the client's response stream so the server can send messages to it.
        userStreamManager.registerStream(userId, responseObserver);

        // Send the initial confirmation event.
        sendStreamEstablishedEvent(userId, responseObserver);

        // Return an observer that will handle all incoming messages from this client.
        return new ClientToServerStreamObserverC1V1(
                userId,
                responseObserver,
                userStreamManager,
                requestValidator,
                chatLogicService,
                eventDispatchService,
                logger
        );
    }

    /**
     * Sends the initial StreamEstablishedEvent to the client.
     */
    private void sendStreamEstablishedEvent(String userId, StreamObserver<ServerToClientEvent> responseObserver) {
        Timestamp serverTimestamp = protoMapper.toProtoTimestamp(Instant.now());
        StreamEstablishedEvent establishedEvent = StreamEstablishedEvent.newBuilder()
                .setUserId(userId)
                .setServerTimestamp(serverTimestamp)
                .build();
        ServerToClientEvent event = ServerToClientEvent.newBuilder()
                .setStreamEstablishedEvent(establishedEvent)
                .build();
        try {
            responseObserver.onNext(event);
            logger.logInfo("Sent StreamEstablishedEvent", Map.of("userId", userId));
        } catch (Exception e) {
            logger.logError("Failed to send StreamEstablishedEvent to user", Map.of("userId", userId), e);
            // Don't close responseObserver here, ClientToServerStreamObserverC1V1.onError will handle it if stream is broken
        }
    }

    /**
     * A stream observer that does nothing, used for early termination.
     */
    private static class NoOpStreamObserver<T> implements StreamObserver<T> {
        @Override public void onNext(T value) {}
        @Override public void onError(Throwable t) {}
        @Override public void onCompleted() {}
    }
}
```
```java
// src/test/java/com/example/chat/v1/RealTimeChatStreamApplicationC1V1Tests.java