package com.example.service.queue;

import com.example.model.Message;
import com.example.service.logging.IEventLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.UUID;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class KafkaQueueServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Mock
    private IEventLogger eventLogger;

    @InjectMocks
    private KafkaQueueService kafkaQueueService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaQueueService, "topicName", "test-topic");
    }

    @Test
    void enqueueMessage_shouldSendMessageToKafka() {
        Message message = Message.builder().id(UUID.randomUUID()).build();
        kafkaQueueService.enqueueMessage(message);
        verify(kafkaTemplate).send(eq("test-topic"), anyString(), any(Message.class));
        verify(eventLogger).log(eq("KafkaEnqueueSuccess"), anyString());
    }
}