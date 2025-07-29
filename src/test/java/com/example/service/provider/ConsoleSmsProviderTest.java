package com.example.service.provider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsoleSmsProviderTest {

    @InjectMocks
    private ConsoleSmsProvider consoleSmsProvider;

    @Test
    void sendSms_shouldNotThrowException() {
        consoleSmsProvider.sendSms("+12345", "Test message");
        // Test passes if no exception is thrown
    }
}