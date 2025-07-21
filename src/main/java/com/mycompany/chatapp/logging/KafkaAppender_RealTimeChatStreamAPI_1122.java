package com.mycompany.chatapp.logging;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;

/**
 * A conceptual appender demonstrating an abstraction layer for external logging systems like Kafka.
 * In a real implementation, this would contain a Kafka producer client.
 */
@Plugin(name = "KafkaAppender_RealTimeChatStreamAPI_1122", category = "Core", elementType = "appender", printObject = true)
public class KafkaAppender_RealTimeChatStreamAPI_1122 extends AbstractAppender {

    protected KafkaAppender_RealTimeChatStreamAPI_1122(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
    }

    @PluginFactory
    public static KafkaAppender_RealTimeChatStreamAPI_1122 createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter) {
        return new KafkaAppender_RealTimeChatStreamAPI_1122(name, filter, layout, true, null);
    }

    @Override
    public void append(LogEvent event) {
        // In a real application, you would initialize a KafkaProducer here
        // and send the formatted log message to a Kafka topic.
        final byte[] bytes = getLayout().toByteArray(event);
        // producer.send(new ProducerRecord<>("log-topic", bytes));
        System.out.println("KAFKA_APPENDER_STUB: " + new String(bytes));
    }
}
```
```java
// src/main/java/com/mycompany/chatapp/grpc/ChatServiceImpl_RealTimeChatStreamAPI_1122.java