package com.example.chat.v1.grpc;

import com.example.chat.v1.enums.ErrorEventCodeC1V1;
import com.example.chat.v1.exception.ChatValidationExceptionC1V1;
import com.example.chat.v1.logging.AbstractLoggerC1V1;
import com.example.chat.v1.service.ChatLogicServiceC1V1;
import com.example.chat.v1.service.EventDispatchServiceC1V1;
import com.example.chat.v1.service.UserStreamManagerC1V1;
import com.example.chat.v1.validation.RequestValidatorC1V1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientToServerStreamObserverC1V1Test {

    @Mock
    private StreamObserver<ServerToClientEvent> responseObserver;
    @Mock
    private UserStreamManagerC1V1 userStreamManager;
    @Mock
    private RequestValidatorC1V1 requestValidator;
    @Mock
    private ChatLogicServiceC1V1 chatLogicService;
    @Mock
    private EventDispatchServiceC1V1 eventDispatchService;
    @Mock
    private AbstractLoggerC1V1 logger;

    private ClientToServerStreamObserverC1V1 observer;
    private String userId;
    private SendMessageRequest sendMessageRequest;
    private MarkAsSeenRequest markAsSeenRequest;
    private StartTypingRequest startTypingRequest;
    private StopTypingRequest stopTypingRequest;

    @BeforeEach
    void setUp() {
        userId = "user_test1";
        observer = new ClientToServerStreamObserverC1V1(
                userId, responseObserver, userStreamManager, requestValidator, chatLogicService, eventDispatchService, logger
        );

        sendMessageRequest = SendMessageRequest.newBuilder()
                .setClientMessageId(UUID.randomUUID().toString())
                .setConversationId("conv_abc")
                .setContent("Hello")
                .build();

        markAsSeenRequest = MarkAsSeenRequest.newBuilder()
                .setConversationId("conv_abc")
                .setLastSeenMessageId("msg_xyz")
                .build();
        
        startTypingRequest = StartTypingRequest.newBuilder()
                .setConversationId("conv_abc")
                .build();

        stopTypingRequest = StopTypingRequest.newBuilder()
                .setConversationId("conv_abc")
                .build();
    }

    // --- onNext Tests ---

    @Test
    void onNext_SendMessageRequest_Success() {
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(sendMessageRequest).build();

        observer.onNext(event);

        verify(logger, times(1)).logInfo(eq("Received client event"), anyMap());
        verify(requestValidator, times(1)).validate(event);
        verify(chatLogicService, times(1)).processSendMessage(sendMessageRequest, userId);
        verify(responseObserver, never()).onNext(any(ServerToClientEvent.class)); // No error event
    }

    @Test
    void onNext_MarkAsSeenRequest_Success() {
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setMarkAsSeenRequest(markAsSeenRequest).build();

        observer.onNext(event);

        verify(logger, times(1)).logInfo(eq("Received client event"), anyMap());
        verify(requestValidator, times(1)).validate(event);
        verify(chatLogicService, times(1)).processMarkAsSeen(markAsSeenRequest, userId);
        verify(responseObserver, never()).onNext(any(ServerToClientEvent.class));
    }

    @Test
    void onNext_StartTypingRequest_Success() {
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setStartTypingRequest(startTypingRequest).build();

        observer.onNext(event);

        verify(logger, times(1)).logInfo(eq("Received client event"), anyMap());
        verify(requestValidator, times(1)).validate(event);
        verify(chatLogicService, times(1)).processTyping(startTypingRequest.getConversationId(), userId, true);
        verify(responseObserver, never()).onNext(any(ServerToClientEvent.class));
    }

    @Test
    void onNext_StopTypingRequest_Success() {
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setStopTypingRequest(stopTypingRequest).build();

        observer.onNext(event);

        verify(logger, times(1)).logInfo(eq("Received client event"), anyMap());
        verify(requestValidator, times(1)).validate(event);
        verify(chatLogicService, times(1)).processTyping(stopTypingRequest.getConversationId(), userId, false);
        verify(responseObserver, never()).onNext(any(ServerToClientEvent.class));
    }

    @Test
    void onNext_ValidationFails_SendsErrorEvent() {
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(sendMessageRequest).build();
        ChatValidationExceptionC1V1 validationException = new ChatValidationExceptionC1V1(
                "Invalid content", ErrorEventCodeC1V1.VALIDATION_FAILED, sendMessageRequest.getClientMessageId(), "SendMessageRequest"
        );
        doThrow(validationException).when(requestValidator).validate(event);

        ServerToClientEvent errorEventMock = ServerToClientEvent.newBuilder().setErrorEvent(ErrorEvent.newBuilder().setErrorCode("VALIDATION_FAILED").build()).build();
        when(eventDispatchService.buildErrorEvent(anyString(), anyString(), anyString(), anyString())).thenReturn(errorEventMock);

        observer.onNext(event);

        verify(logger, times(1)).logInfo(eq("Received client event"), anyMap());
        verify(requestValidator, times(1)).validate(event);
        verify(logger, times(1)).logWarn(eq("Client event validation failed"), anyMap());
        verify(eventDispatchService, times(1)).buildErrorEvent(
                eq("VALIDATION_FAILED"), eq("Invalid content"), eq(sendMessageRequest.getClientMessageId()), eq("SendMessageRequest")
        );
        verify(responseObserver, times(1)).onNext(errorEventMock);
        verify(chatLogicService, never()).processSendMessage(any(), any());
    }

    @Test
    void onNext_LogicServiceThrowsChatValidationException_SendsErrorEvent() {
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(sendMessageRequest).build();
        ChatValidationExceptionC1V1 logicException = new ChatValidationExceptionC1V1(
                "Duplicate ID", ErrorEventCodeC1V1.IDEMPOTENCY_KEY_REPLAY, sendMessageRequest.getClientMessageId(), "SendMessageRequest"
        );
        doThrow(logicException).when(chatLogicService).processSendMessage(sendMessageRequest, userId);

        ServerToClientEvent errorEventMock = ServerToClientEvent.newBuilder().setErrorEvent(ErrorEvent.newBuilder().setErrorCode("IDEMPOTENCY_KEY_REPLAY").build()).build();
        when(eventDispatchService.buildErrorEvent(anyString(), anyString(), anyString(), anyString())).thenReturn(errorEventMock);

        observer.onNext(event);

        verify(logger, times(1)).logInfo(eq("Received client event"), anyMap());
        verify(requestValidator, times(1)).validate(event);
        verify(logger, times(1)).logWarn(eq("Client event validation failed"), anyMap());
        verify(eventDispatchService, times(1)).buildErrorEvent(
                eq("IDEMPOTENCY_KEY_REPLAY"), eq("Duplicate ID"), eq(sendMessageRequest.getClientMessageId()), eq("SendMessageRequest")
        );
        verify(responseObserver, times(1)).onNext(errorEventMock);
    }

    @Test
    void onNext_LogicServiceThrowsGenericException_SendsUnknownErrorEvent() {
        ClientToServerEvent event = ClientToServerEvent.newBuilder().setSendMessageRequest(sendMessageRequest).build();
        RuntimeException genericException = new RuntimeException("Database connection failed");
        doThrow(genericException).when(chatLogicService).processSendMessage(sendMessageRequest, userId);

        ServerToClientEvent errorEventMock = ServerToClientEvent.newBuilder().setErrorEvent(ErrorEvent.newBuilder().setErrorCode("UNKNOWN_ERROR").build()).build();
        when(eventDispatchService.buildErrorEvent(eq("UNKNOWN_ERROR"), anyString(), isNull(), anyString())).thenReturn(errorEventMock);

        observer.onNext(event);

        verify(logger, times(1)).logInfo(eq("Received client event"), anyMap());
        verify(requestValidator, times(1)).validate(event);
        verify(logger, times(1)).logError(eq("Unexpected error processing client event"), anyMap(), eq(genericException));
        verify(eventDispatchService, times(1)).buildErrorEvent(
                eq("UNKNOWN_ERROR"), eq("An internal error occurred"), isNull(), eq("SEND_MESSAGE_REQUEST")
        );
        verify(responseObserver, times(1)).onNext(errorEventMock);
    }

    // --- onError Tests ---

    @Test
    void onError_RemovesStreamAndLogs() {
        RuntimeException error = new RuntimeException("Stream disconnected");

        observer.onError(error);

        verify(logger, times(1)).logWarn(eq("Client stream error"), anyMap(), eq(error));
        verify(userStreamManager, times(1)).removeStream(userId);
        verify(responseObserver, never()).onCompleted(); // onError should not call onCompleted
    }

    // --- onCompleted Tests ---

    @Test
    void onCompleted_RemovesStreamAndCallsResponseOnCompleted() {
        observer.onCompleted();

        verify(logger, times(1)).logInfo(eq("Client stream completed"), anyMap());
        verify(userStreamManager, times(1)).removeStream(userId);
        verify(responseObserver, times(1)).onCompleted();
    }
}
```
```java
// src/test/java/com/example/chat/v1/grpc/ChatServiceV1Impl_C1V1Test.java