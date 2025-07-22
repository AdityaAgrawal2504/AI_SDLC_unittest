package com.example.auth.config;

import com.example.auth.logging.StructuredLogger_AuthVerifyOtp_17169;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Configuration for setting up the structured logger bean.
 */
@Configuration
public class Log4j2Config_AuthVerifyOtp_17169 {

    /**
     * Provides a bean for the structured logger implementation.
     * @return An instance of StructuredLogger.
     */
    @Bean
    public StructuredLogger_AuthVerifyOtp_17169 structuredLogger() {
        final Logger logger = LogManager.getLogger("com.example.auth");
        final ObjectMapper objectMapper = new ObjectMapper();

        return new StructuredLogger_AuthVerifyOtp_17169() {
            @Override
            public void logInfo(String message, Map<String, Object> context) {
                try {
                    String jsonContext = objectMapper.writeValueAsString(context);
                    logger.info("{} - Context: {}", message, jsonContext);
                } catch (JsonProcessingException e) {
                    logger.error("Failed to serialize log context", e);
                }
            }

            @Override
            public void logWarn(String message, Map<String, Object> context) {
                 try {
                    String jsonContext = objectMapper.writeValueAsString(context);
                    logger.warn("{} - Context: {}", message, jsonContext);
                } catch (JsonProcessingException e) {
                    logger.error("Failed to serialize log context", e);
                }
            }

            @Override
            public void logError(String message, Map<String, Object> context, Throwable throwable) {
                 try {
                    String jsonContext = objectMapper.writeValueAsString(context);
                    logger.error("{} - Context: {}", message, jsonContext, throwable);
                } catch (JsonProcessingException e) {
                    logger.error("Failed to serialize log context", e);
                }
            }
        };
    }
}
src/main/java/com/example/auth/logging/LoggingAspect_AuthVerifyOtp_17169.java