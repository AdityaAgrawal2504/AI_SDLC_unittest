package com.example.chat.v1.service;

import com.example.chat.v1.grpc.ServerToClientEvent;
import com.example.chat.v1.logging.AbstractLoggerC1V1;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStreamManagerC1V1Test {

    @Mock
    private AbstractLoggerC1V1 logger;

    @InjectMocks
    private UserStreamManagerC1V1 userStreamManager;

    @Mock
    private StreamObserver<ServerToClientEvent> mockStreamObserver;

    private String userId = "testUser123";
    private ServerToClientEvent testEvent;

    @BeforeEach
    void setUp() {
        testEvent = ServerToClientEvent.newBuilder()
                .setStreamEstablishedEvent(
                        com.example.chat.v1.grpc.StreamEstablishedEvent.newBuilder().setUserId(userId).build()
                )
                .build();
    }

    @Test
    void registerStream_AddsStreamToMap() {
        userStreamManager.registerStream(userId, mockStreamObserver);

        userStreamManager.sendToUser(userId, testEvent); // Attempt to send to verify registration
        verify(mockStreamObserver, times(1)).onNext(testEvent);
        verify(logger, times(1)).logInfo(eq("Stream registered"), anyMap());
        verify(logger, times(1)).logInfo(eq("Event sent to user"), anyMap());
    }

    @Test
    void removeStream_RemovesStreamFromMap() {
        userStreamManager.registerStream(userId, mockStreamObserver);
        userStreamManager.removeStream(userId);

        userStreamManager.sendToUser(userId, testEvent); // Attempt to send after removal
        verify(mockStreamObserver, never()).onNext(testEvent); // Should not be called
        verify(logger, times(1)).logInfo(eq("Stream registered"), anyMap());
        verify(logger, times(1)).logInfo(eq("Stream removed"), anyMap());
        verify(logger, times(1)).logWarn(eq("Attempted to send event to disconnected user"), anyMap());
    }

    @Test
    void sendToUser_StreamExists_CallsOnNext() {
        userStreamManager.registerStream(userId, mockStreamObserver);

        userStreamManager.sendToUser(userId, testEvent);

        verify(mockStreamObserver, times(1)).onNext(testEvent);
        verify(logger, times(1)).logInfo(eq("Event sent to user"), anyMap());
    }

    @Test
    void sendToUser_StreamDoesNotExist_DoesNothing() {
        userStreamManager.sendToUser("nonExistentUser", testEvent);

        verify(mockStreamObserver, never()).onNext(any(ServerToClientEvent.class));
        verify(logger, times(1)).logWarn(eq("Attempted to send event to disconnected user"), anyMap());
        verify(logger, never()).logInfo(eq("Event sent to user"), anyMap());
    }

    @Test
    void sendToUser_OnNextThrowsException_RemovesStreamAndLogsError() {
        userStreamManager.registerStream(userId, mockStreamObserver);
        doThrow(new RuntimeException("Simulated stream error")).when(mockStreamObserver).onNext(any(ServerToClientEvent.class));

        assertDoesNotThrow(() -> userStreamManager.sendToUser(userId, testEvent)); // Should catch and handle internally

        verify(mockStreamObserver, times(1)).onNext(testEvent);
        verify(logger, times(1)).logError(eq("Error sending event to user, removing stream"), anyMap(), any(RuntimeException.class));
        verify(logger, times(1)).logInfo(eq("Stream removed"), anyMap()); // Verify removeStream was called
    }

    @Test
    void sendToUser_NullEvent_HandlesGracefully() {
        userStreamManager.registerStream(userId, mockStreamObserver);
        
        userStreamManager.sendToUser(userId, null);

        // Expect onNext to be called with null, which gRPC stub might handle or throw.
        // If it throws, the error path is tested by sendToUser_OnNextThrowsException.
        // If it does not throw (e.g. proto message default instance), it just continues.
        // For this test, we verify it doesn't crash the manager.
        verify(mockStreamObserver, times(1)).onNext(null);
        verify(logger, times(1)).logInfo(eq("Event sent to user"), anyMap());
    }
}
```
```java
// src/test/java/com/example/chat/v1/util/IdGeneratorC1V1Test.java