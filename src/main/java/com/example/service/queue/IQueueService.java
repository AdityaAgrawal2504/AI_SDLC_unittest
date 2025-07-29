package com.example.service.queue;

import com.example.model.Message;

/**
 * An abstraction for a message queuing system like Kafka or RabbitMQ.
 */
public interface IQueueService {
    /**
     * Enqueues a message for asynchronous processing.
     * @param message The message entity to be placed on the queue.
     */
    void enqueueMessage(Message message);
}