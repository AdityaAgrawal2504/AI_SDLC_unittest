package com.yourorg.fetchconversationsapi.logging;

/**
 * An abstract logging service interface.
 * This allows for swapping the underlying logging implementation (e.g., Log4j2, Kafka)
 * without changing the application code that uses it.
 */
public interface LoggerServiceFCA8123 {

    void info(String message, Object... args);

    void warn(String message, Object... args);

    void error(String message, Throwable t, Object... args);
}
```
src/main/java/com/yourorg/fetchconversationsapi/logging/Log4j2LoggerServiceFCA8123.java
<ctrl60>package com.yourorg.fetchconversationsapi.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * A Log4j2-based implementation of the LoggerService.
 * It acts as a wrapper around a Log4j2 logger instance.
 */
@Service
public class Log4j2LoggerServiceFCA8123 implements LoggerServiceFCA8123 {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    @Override
    public void error(String message, Throwable t, Object... args) {
        logger.error(message, t, args);
    }
}
```
src/main/java/com/yourorg/fetchconversationsapi/logging/LoggingAspectFCA8123.java