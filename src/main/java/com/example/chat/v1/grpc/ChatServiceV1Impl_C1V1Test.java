package com.example.chat.v1.grpc;

import com.example.chat.v1.logging.AbstractLoggerC1V1;
import com.example.chat.v1.service.ChatLogicServiceC1V1;
import com.example.chat.v1.service.EventDispatchServiceC1V1;
import com.example.chat.v1.service.UserStreamManagerC1V1;
import com.example.chat.v1.util.ProtoMapperC1V1;
import com.example.chat.v1.validation.RequestValidatorC1V1;
import com.google.protobuf.Timestamp;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;

import static com.example.chat.v1.util.ConstantsC1V1.AUTH_USER_ID_CONTEXT_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceV1Impl_C1V1Test {

    @Mock
    private UserStreamManagerC1V1 userStreamManager;
    @Mock
    private RequestValidatorC1V1 requestValidator;
    @Mock
    private ChatLogicServiceC1V1 chatLogicService;
    @Mock
    private EventDispatchServiceC1V1 eventDispatchService;
    @Mock
    private ProtoMapperC1V1 protoMapper;
    @Mock
    private AbstractLoggerC1V1 logger;
    @Mock
    private StreamObserver<ServerToClientEvent> responseObserver;

    private ChatServiceV1Impl_C1V1 chatService;
    private String testUserId = "user_test123";

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceV1Impl_C1V1(
                userStreamManager, requestValidator, chatLogicService, eventDispatchService, protoMapper, logger
        );

        // Mock common behavior for protoMapper to return a valid Timestamp
        when(protoMapper.toProtoTimestamp(any(Instant.class))).thenReturn(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build());
    }

    @Test
    void messageStream_ValidUserIdInContext_EstablishesStreamAndSendsInitialEvent() {
        // Arrange
        // Mock gRPC Context to return a user ID
        try (MockedStatic<Context> mockedContext = mockStatic(Context.class)) {
            Context mockGrpcContext = mock(Context.class);
            when(Context.current()).thenReturn(mockGrpcContext);
            when(mockGrpcContext.get(AUTH_USER_ID_CONTEXT_KEY)).thenReturn(testUserId);

            // Capture the event sent to the client
            ArgumentCaptor<ServerToClientEvent> eventCaptor = ArgumentCaptor.forClass(ServerToClientEvent.class);

            // Act
            StreamObserver<ClientToServerEvent> clientObserver = chatService.messageStream(responseObserver);

            // Assert
            assertNotNull(clientObserver);
            assertTrue(clientObserver instanceof ClientToServerStreamObserverC1V1);

            verify(logger, times(1)).logInfo(eq("New stream connection initiated"), anyMap());
            verify(userStreamManager, times(1)).registerStream(testUserId, responseObserver);
            
            // Verify StreamEstablishedEvent was sent
            verify(responseObserver, times(1)).onNext(eventCaptor.capture());
            ServerToClientEvent capturedEvent = eventCaptor.getValue();
            assertTrue(capturedEvent.hasStreamEstablishedEvent());
            assertEquals(testUserId, capturedEvent.getStreamEstablishedEvent().getUserId());
            verify(logger, times(1)).logInfo(eq("Sent StreamEstablishedEvent"), anyMap());
        }
    }

    @Test
    void messageStream_NullUserIdInContext_ClosesStreamWithUnauthenticatedStatus() {
        // Arrange
        // Mock gRPC Context to return null for user ID
        try (MockedStatic<Context> mockedContext = mockStatic(Context.class)) {
            Context mockGrpcContext = mock(Context.class);
            when(Context.current()).thenReturn(mockGrpcContext);
            when(mockGrpcContext.get(AUTH_USER_ID_CONTEXT_KEY)).thenReturn(null);

            // Act
            StreamObserver<ClientToServerEvent> clientObserver = chatService.messageStream(responseObserver);

            // Assert
            assertNotNull(clientObserver);
            assertTrue(clientObserver instanceof ChatServiceV1Impl_C1V1.NoOpStreamObserver); // Should return NoOpObserver

            ArgumentCaptor<Status> statusCaptor = ArgumentCaptor.forClass(Status.class);
            verify(responseObserver, times(1)).onError(statusCaptor.capture());
            assertEquals(Status.UNAUTHENTICATED.getCode(), statusCaptor.getValue().getCode());
            assertEquals("User ID not found in context.", statusCaptor.getValue().getDescription());
            verify(logger, times(1)).error(eq("User ID not found in gRPC context for new stream connection. Closing stream."), any());
            verify(userStreamManager, never()).registerStream(anyString(), any());
            verify(responseObserver, never()).onNext(any());
        }
    }

    @Test
    void messageStream_EmptyUserIdInContext_ClosesStreamWithUnauthenticatedStatus() {
        // Arrange
        // Mock gRPC Context to return an empty string for user ID
        try (MockedStatic<Context> mockedContext = mockStatic(Context.class)) {
            Context mockGrpcContext = mock(Context.class);
            when(Context.current()).thenReturn(mockGrpcContext);
            when(mockGrpcContext.get(AUTH_USER_ID_CONTEXT_KEY)).thenReturn("");

            // Act
            StreamObserver<ClientToServerEvent> clientObserver = chatService.messageStream(responseObserver);

            // Assert
            assertNotNull(clientObserver);
            assertTrue(clientObserver instanceof ChatServiceV1Impl_C1V1.NoOpStreamObserver); // Should return NoOpObserver

            ArgumentCaptor<Status> statusCaptor = ArgumentCaptor.forClass(Status.class);
            verify(responseObserver, times(1)).onError(statusCaptor.capture());
            assertEquals(Status.UNAUTHENTICATED.getCode(), statusCaptor.getValue().getCode());
            assertEquals("User ID not found in context.", statusCaptor.getValue().getDescription());
            verify(logger, times(1)).error(eq("User ID not found in gRPC context for new stream connection. Closing stream."), any());
            verify(userStreamManager, never()).registerStream(anyString(), any());
            verify(responseObserver, never()).onNext(any());
        }
    }

    @Test
    void sendStreamEstablishedEvent_OnNextThrowsException_LogsErrorAndDoesNotCloseObserver() {
        // Arrange
        doThrow(new RuntimeException("Simulated onNext exception")).when(responseObserver).onNext(any(ServerToClientEvent.class));

        // Act
        // Call the private method via reflection or make it package-private for testing
        // For simplicity, we can let it run and check side effects
        // Or, more robustly, call it via an instance of ChatServiceV1Impl_C1V1
        // which the current setup already supports.
        try (MockedStatic<Context> mockedContext = mockStatic(Context.class)) {
            Context mockGrpcContext = mock(Context.class);
            when(Context.current()).thenReturn(mockGrpcContext);
            when(mockGrpcContext.get(AUTH_USER_ID_CONTEXT_KEY)).thenReturn(testUserId);
            
            chatService.messageStream(responseObserver); // This will call sendStreamEstablishedEvent
        }

        // Assert
        verify(responseObserver, times(1)).onNext(any(ServerToClientEvent.class)); // onNext was attempted
        verify(logger, times(1)).logError(eq("Failed to send StreamEstablishedEvent to user"), anyMap(), any(RuntimeException.class));
        verify(responseObserver, never()).onError(any(Throwable.class)); // Importantly, it does not explicitly close or error the stream
    }
}
```