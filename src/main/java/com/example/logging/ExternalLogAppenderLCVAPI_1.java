package com.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Plugin(name = "ExternalLogAppenderLCVAPI_1", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class ExternalLogAppenderLCVAPI_1 extends AbstractAppender {

    private static final Logger logger = LogManager.getLogger(ExternalLogAppenderLCVAPI_1.class);

    protected ExternalLogAppenderLCVAPI_1(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions, null); // Property array is null
    }

    /**
     * Factory method for creating the appender.
     * @param name The name of the appender.
     * @param filter The filter to use.
     * @param layout The layout to use for formatting log events.
     * @param ignoreExceptions Whether to ignore exceptions.
     * @return A new instance of ExternalLogAppenderLCVAPI_1.
     */
    @PluginFactory
    public static ExternalLogAppenderLCVAPI_1 createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
        if (name == null) {
            LOGGER.error("No name provided for ExternalLogAppenderLCVAPI_1");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new ExternalLogAppenderLCVAPI_1(name, filter, layout, ignoreExceptions);
    }
    
    /**
     * This is where the log event is processed. In a real scenario, this would
     * contain the logic to send the log message to an external system like Kafka.
     * @param event The log event.
     */
    @Override
    public void append(LogEvent event) {
        // In a real implementation, you would have a KafkaProducer or EventHubClient here.
        // final byte[] bytes = getLayout().toByteArray(event);
        // kafkaProducer.send(new ProducerRecord<>("log-topic", bytes));
        
        // For demonstration, we just log that we would be sending it.
        // This avoids logging in a loop. Use a different logger or console output.
        String formattedMessage = new String(getLayout().toByteArray(event), StandardCharsets.UTF_8);
        System.out.println("[EXTERNAL LOG SIMULATOR] " + formattedMessage.trim());
    }
}
```

src/main/resources/application.properties
```properties
# Server Configuration
server.port=8080

# Spring JPA, Hibernate, and H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Custom Application Properties
app.security.max-failed-attempts=5

# Logging configuration file
logging.config=classpath:log4j2.xml
```

src/main/resources/log4j2.xml
```xml