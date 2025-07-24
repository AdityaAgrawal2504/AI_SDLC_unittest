src/main/java/com/example/provider/impl/SmsProvider_Log_P1Q2R.java

<ctrl60>
package com.example.provider.impl;

import com.example.provider.interfaces.ISmsProvider_M9N0O;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Mock implementation of ISmsProvider that logs the message instead of sending an actual SMS.
 */
@Component
public class SmsProvider_Log_P1Q2R implements ISmsProvider_M9N0O {

    private static final Logger logger = LogManager.getLogger(SmsProvider_Log_P1Q2R.class);

    /**
     * Logs the SMS content to the console for simulation.
     * @param to The recipient phone number.
     * @param message The message content.
     * @return A completed CompletableFuture.
     */
    @Override
    public CompletableFuture<Void> send(String to, String message) {
        logger.info("--- SIMULATING SMS ---");
        logger.info("To: {}", to);
        logger.info("Message: {}", message);
        logger.info("----------------------");
        return CompletableFuture.completedFuture(null);
    }
}