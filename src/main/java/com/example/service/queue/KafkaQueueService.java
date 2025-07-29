package com.example.service.queue;

import com.example.model.Message;
import com.example.service.logging.IEventLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * An implementation of the queue service using Spring Kafka.
 */
@Service
@RequiredArgsConstructor
public class KafkaQueueService implements IQueueService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final IEventLogger eventLogger;

    @Value("${app.kafka.message-queue-topic}")
    private String topicName;

    /**
     * Sends a message to a Kafka topic.
     * @param message The message entity to be placed on the queue.
     */
    @Override
    public void enqueueMessage(Message message) {
        try {
            // In a real app, you'd send a DTO, not an entity.
            // This is simplified for demonstration.
            kafkaTemplate.send(topicName, message.getId().toString(), message);
            eventLogger.log("KafkaEnqueueSuccess", "Successfully sent message " + message.getId() + " to topic " + topicName);
        } catch (Exception e) {
            eventLogger.log("KafkaEnqueueFail", "Failed to send message " + message.getId() + " to Kafka: " + e.getMessage());
            // Add fallback logic (e.g., save to DB table for later retry)
        }
    }
}