package com.mycompany.chatapp.service;

import com.mycompany.chatapp.grpc.ServerStreamResponse;
import com.mycompany.chatapp.grpc.StreamConnectedEvent;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatStreamManager_RealTimeChatStreamAPI_1122Test {

    private ChatStreamManager_RealTimeChatStreamAPI_1122 chatStreamManager;

    @Mock
    private StreamObserver<ServerStreamResponse> observer1;
    @Mock
    private StreamObserver<ServerStreamResponse> observer2;

    @BeforeEach
    void setUp() {
        chatStreamManager = new ChatStreamManager_RealTimeChatStreamAPI_1122();
    }

    @Test
    void testRegisterAndDeregister() {
        String chatId = "chat-1";

        // Register two observers
        chatStreamManager.register(chatId, observer1);
        chatStreamManager.register(chatId, observer2);

        // Broadcast a message
        ServerStreamResponse response = ServerStreamResponse.newBuilder()
                .setStreamConnected(StreamConnectedEvent.newBuilder().setMessage("test").build())
                .build();
        chatStreamManager.broadcast(chatId, response);

        // Verify both observers received the message
        verify(observer1, times(1)).onNext(response);
        verify(observer2, times(1)).onNext(response);

        // Deregister one observer
        chatStreamManager.deregister(chatId, observer1);

        // Broadcast again
        chatStreamManager.broadcast(chatId, response);

        // Verify only the remaining observer received the second message
        verify(observer1, times(1)).onNext(response); // Still 1
        verify(observer2, times(2)).onNext(response); // Now 2
    }

    @Test
    void testBroadcast() {
        String chatId1 = "chat-1";
        String chatId2 = "chat-2";

        chatStreamManager.register(chatId1, observer1);
        chatStreamManager.register(chatId2, observer2);

        ServerStreamResponse response = ServerStreamResponse.newBuilder()
                .setStreamConnected(StreamConnectedEvent.newBuilder().setMessage("test").build())
                .build();

        // Broadcast to chat 1
        chatStreamManager.broadcast(chatId1, response);

        // Verify only observer1 got the message
        verify(observer1, times(1)).onNext(response);
        verify(observer2, never()).onNext(response);
    }
}
```
```java
// src/test/java/com/mycompany/chatapp/grpc/ChatServiceImpl_RealTimeChatStreamAPI_1122Test.java