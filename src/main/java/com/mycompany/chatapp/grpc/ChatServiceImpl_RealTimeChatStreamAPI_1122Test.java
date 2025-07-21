package com.mycompany.chatapp.grpc;

import com.mycompany.chatapp.model.AuthenticatedUser_RealTimeChatStreamAPI_1122;
import com.mycompany.chatapp.service.*;
import com.mycompany.chatapp.validator.RequestValidator_RealTimeChatStreamAPI_1122;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImpl_RealTimeChatStreamAPI_1122Test {

    @Mock
    private AuthenticationService_RealTimeChatStreamAPI_1122 authService;
    @Mock
    private ChatRoomService_RealTimeChatStreamAPI_1122 chatRoomService;
    @Mock
    private MessageService_RealTimeChatStreamAPI_1122 messageService;
    @Mock
    private ChatStreamManager_RealTimeChatStreamAPI_1122 streamManager;
    @Mock
    private StreamObserver<ServerStreamResponse> responseObserver;
    @Captor
    private ArgumentCaptor<ServerStreamResponse> responseCaptor;
    @Captor
    private ArgumentCaptor<Throwable> errorCaptor;

    private ChatServiceImpl_RealTimeChatStreamAPI_1122 chatService;
    private StreamObserver<ClientStreamRequest> requestObserver;
    private RequestValidator_RealTimeChatStreamAPI_1122 validator;

    private final String VALID_TOKEN = "valid-token-user1";
    private final String VALID_CHAT_ID = "a1b2c3d4-e5f6-7890-1234-567890abcdef";
    private final String VALID_USER_ID = "3325c8d3-5240-3b47-8a62-7935406c8842";
    private final AuthenticatedUser_RealTimeChatStreamAPI_1122 MOCK_USER = new AuthenticatedUser_RealTimeChatStreamAPI_1122(VALID_USER_ID, "MockUser");

    @BeforeEach
    void setUp() {
        validator = new RequestValidator_RealTimeChatStreamAPI_1122();
        chatService = new ChatServiceImpl_RealTimeChatStreamAPI_1122(authService, chatRoomService, messageService, streamManager, validator);
        requestObserver = chatService.chatStream(responseObserver);
    }

    private void mockSuccessfulAuthentication() {
        when(authService.authenticate(VALID_TOKEN)).thenReturn(Optional.of(MOCK_USER));
        when(chatRoomService.chatExists(VALID_CHAT_ID)).thenReturn(true);
        when(chatRoomService.isUserMemberOfChat(VALID_USER_ID, VALID_CHAT_ID)).thenReturn(true);
    }

    @Test
    void chatStream_successfulConnection() {
        // Arrange
        mockSuccessfulAuthentication();
        InitialConnectRequest connectRequest = InitialConnectRequest.newBuilder()
                .setSessionToken(VALID_TOKEN)
                .setChatId(VALID_CHAT_ID)
                .build();
        ClientStreamRequest request = ClientStreamRequest.newBuilder().setInitialConnect(connectRequest).build();

        // Act
        requestObserver.onNext(request);

        // Assert
        verify(responseObserver).onNext(responseCaptor.capture());
        ServerStreamResponse response = responseCaptor.getValue();
        assertTrue(response.hasStreamConnected());
        assertEquals("Connection successful.", response.getStreamConnected().getMessage());
        verify(streamManager).register(eq(VALID_CHAT_ID), any());
    }

    @Test
    void chatStream_sendMessageAfterSuccessfulConnection() {
        // Arrange
        mockSuccessfulAuthentication();
        // 1. Connect first
        requestObserver.onNext(ClientStreamRequest.newBuilder().setInitialConnect(
                InitialConnectRequest.newBuilder().setSessionToken(VALID_TOKEN).setChatId(VALID_CHAT_ID).build()
        ).build());

        // 2. Then send message
        String idempotencyKey = UUID.randomUUID().toString();
        String messageContent = "Hello World";
        SendMessageRequest sendMessageRequest = SendMessageRequest.newBuilder()
                .setIdempotencyKey(idempotencyKey)
                .setChatId(VALID_CHAT_ID)
                .setContent(messageContent)
                .build();
        ClientStreamRequest request = ClientStreamRequest.newBuilder().setSendMessage(sendMessageRequest).build();
        when(messageService.claimIdempotencyKey(idempotencyKey)).thenReturn(true);
        when(messageService.createNewMessage(anyString(), anyString(), anyString(), anyString(), any()))
                .thenReturn(NewMessageEvent.newBuilder().setContent(messageContent).build());

        // Act
        requestObserver.onNext(request);

        // Assert
        verify(messageService).createNewMessage(VALID_CHAT_ID, messageContent, VALID_USER_ID, MOCK_USER.getDisplayName(), null);
        verify(streamManager).broadcast(eq(VALID_CHAT_ID), any(ServerStreamResponse.class));
    }

    @Test
    void chatStream_failsIfSendMessageBeforeInitialConnect() {
        // Arrange
        SendMessageRequest sendMessageRequest = SendMessageRequest.newBuilder()
                .setIdempotencyKey(UUID.randomUUID().toString())
                .setContent("test")
                .build();
        ClientStreamRequest request = ClientStreamRequest.newBuilder().setSendMessage(sendMessageRequest).build();

        // Act & Assert
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> requestObserver.onNext(request));
        assertEquals(Status.FAILED_PRECONDITION.getCode(), exception.getStatus().getCode());
        assertTrue(exception.getMessage().contains("Client must send InitialConnectRequest first."));
    }

    @Test
    void chatStream_failsWithInvalidToken() {
        // Arrange
        String invalidToken = "invalid-token";
        when(authService.authenticate(invalidToken)).thenReturn(Optional.empty());
        InitialConnectRequest connectRequest = InitialConnectRequest.newBuilder()
                .setSessionToken(invalidToken)
                .setChatId(VALID_CHAT_ID)
                .build();
        ClientStreamRequest request = ClientStreamRequest.newBuilder().setInitialConnect(connectRequest).build();

        // Act & Assert
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> requestObserver.onNext(request));
        assertEquals(Status.UNAUTHENTICATED.getCode(), exception.getStatus().getCode());
    }

    @Test
    void chatStream_failsWithNonexistentChat() {
        // Arrange
        String nonExistentChatId = UUID.randomUUID().toString();
        when(authService.authenticate(VALID_TOKEN)).thenReturn(Optional.of(MOCK_USER));
        when(chatRoomService.chatExists(nonExistentChatId)).thenReturn(false);
        InitialConnectRequest connectRequest = InitialConnectRequest.newBuilder()
                .setSessionToken(VALID_TOKEN)
                .setChatId(nonExistentChatId)
                .build();
        ClientStreamRequest request = ClientStreamRequest.newBuilder().setInitialConnect(connectRequest).build();

        // Act & Assert
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> requestObserver.onNext(request));
        assertEquals(Status.NOT_FOUND.getCode(), exception.getStatus().getCode());
    }
    
    @Test
    void chatStream_failsWhenUserNotMember() {
        // Arrange
        when(authService.authenticate(VALID_TOKEN)).thenReturn(Optional.of(MOCK_USER));
        when(chatRoomService.chatExists(VALID_CHAT_ID)).thenReturn(true);
        when(chatRoomService.isUserMemberOfChat(VALID_USER_ID, VALID_CHAT_ID)).thenReturn(false); // User is not a member
        InitialConnectRequest connectRequest = InitialConnectRequest.newBuilder()
                .setSessionToken(VALID_TOKEN)
                .setChatId(VALID_CHAT_ID)
                .build();
        ClientStreamRequest request = ClientStreamRequest.newBuilder().setInitialConnect(connectRequest).build();

        // Act & Assert
        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> requestObserver.onNext(request));
        assertEquals(Status.PERMISSION_DENIED.getCode(), exception.getStatus().getCode());
    }

    @Test
    void onCompleted_deregistersStream() {
        // Arrange
        mockSuccessfulAuthentication();
        requestObserver.onNext(ClientStreamRequest.newBuilder().setInitialConnect(
                InitialConnectRequest.newBuilder().setSessionToken(VALID_TOKEN).setChatId(VALID_CHAT_ID).build()
        ).build());

        // Act
        requestObserver.onCompleted();

        // Assert
        verify(streamManager).deregister(eq(VALID_CHAT_ID), any());
        verify(responseObserver).onCompleted();
    }
    
    @Test
    void onError_deregistersStream() {
        // Arrange
        mockSuccessfulAuthentication();
        requestObserver.onNext(ClientStreamRequest.newBuilder().setInitialConnect(
                InitialConnectRequest.newBuilder().setSessionToken(VALID_TOKEN).setChatId(VALID_CHAT_ID).build()
        ).build());

        // Act
        requestObserver.onError(new RuntimeException("Client disconnected"));

        // Assert
        verify(streamManager).deregister(eq(VALID_CHAT_ID), any());
        verify(responseObserver, never()).onError(any()); // The server doesn't propagate the client-side error
    }
}
```