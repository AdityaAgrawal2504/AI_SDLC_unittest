package com.example.service.provider;

import com.example.dto.response.MessageDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class StubWebSocketServiceTest {

    @InjectMocks
    private StubWebSocketService stubWebSocketService;

    @Test
    void sendMessageToUser_shouldNotThrowException() {
        stubWebSocketService.sendMessageToUser(UUID.randomUUID(), new MessageDto());
        // Test passes if no exception is thrown
    }
}