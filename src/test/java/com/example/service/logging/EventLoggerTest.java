package com.example.service.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventLoggerTest {

    @InjectMocks
    private EventLogger eventLogger;

    @Test
    void log_shouldNotThrowException() {
        // This is a simple test to ensure the logging call completes without error.
        // Verifying log output requires a more complex setup with Appender mocks.
        eventLogger.log("TestEvent", "This is a test message.");
    }
}