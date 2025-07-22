package com.example.conversation.api.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A Log4j2 implementation of the StructuredLoggerFCA1 interface.
 * It uses the ThreadContext (MDC) to inject structured data into logs.
 *
 * <!--
 * A sample log4j2-conversation.xml for structured JSON logging via JsonTemplateLayout.
 * This configuration supports file-based logging and is ready for log shippers.
 *
 * <?xml version="1.0" encoding="UTF-8"?>
 * <Configuration status="WARN" monitorInterval="30">
 *     <Appenders>
 *         <Console name="Console" target="SYSTEM_OUT">
 *             <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
 *         </Console>
 *
 *         <RollingFile name="RollingFile"
 *                      fileName="logs/app.log"
 *                      filePattern="logs/app-%d{yyyy-MM-dd}-%i.log.gz">
 *             <JsonTemplateLayout eventTemplateUri="classpath:log4j2-json-template.json" />
 *             <Policies>
 *                 <TimeBasedTriggeringPolicy />
 *                 <SizeBasedTriggeringPolicy size="10 MB"/>
 *             </Policies>
 *             <DefaultRolloverStrategy max="10"/>
 *         </RollingFile>
 *     </Appenders>
 *
 *     <Loggers>
 *         <Root level="info">
 *             <AppenderRef ref="Console"/>
 *             <AppenderRef ref="RollingFile"/>
 *         </Root>
 *         <Logger name="com.example.conversation.api" level="debug" additivity="false">
 *             <AppenderRef ref="Console"/>
 *             <AppenderRef ref="RollingFile"/>
 *         </Logger>
 *     </Loggers>
 * </Configuration>
 *
 * And the corresponding log4j2-json-template.json:
 * {
 *   "timestamp": {
 *     "$resolver": "timestamp",
 *     "pattern": {
 *       "format": "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
 *       "timeZone": "UTC"
 *     }
 *   },
 *   "level": {
 *     "$resolver": "level",
 *     "field": "name"
 *   },
 *   "loggerName": {
 *     "$resolver": "logger",
 *     "field": "name"
 *   },
 *   "message": {
 *     "$resolver": "message",
 *     "stringified": true
 *   },
 *   "context": {
 *     "$resolver": "mdc"
 *   },
 *   "exception": {
 *     "$resolver": "exception",
 *     "stackTrace": {
 *       "stringified": true
 *     }
 *   }
 * }
 * -->
 */
@Component
public class Log4j2StructuredLoggerFCA1 implements StructuredLoggerFCA1 {

    private static final Logger logger = LogManager.getLogger(Log4j2StructuredLoggerFCA1.class);

    @Override
    public void info(String message, Map<String, Object> context) {
        withContext(context, () -> logger.info(message));
    }

    @Override
    public void error(String message, Map<String, Object> context, Throwable throwable) {
        withContext(context, () -> logger.error(message, throwable));
    }

    @Override
    public void log(String level, String message, Map<String, Object> context) {
        withContext(context, () -> logger.log(Level.toLevel(level, Level.INFO), message));
    }

    /**
     * Executes a logging operation within a ThreadContext.
     *
     * @param context The structured context map.
     * @param logAction The logging action to perform.
     */
    private void withContext(Map<String, Object> context, Runnable logAction) {
        if (context == null || context.isEmpty()) {
            logAction.run();
            return;
        }
        // Convert map values to strings for ThreadContext
        Map<String, String> stringContext = context.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> Objects.toString(e.getValue())));

        ThreadContext.putAll(stringContext);
        try {
            logAction.run();
        } finally {
            ThreadContext.clearMap();
        }
    }
}
src/main/java/com/example/conversation/api/logging/LoggingAspectFCA1.java