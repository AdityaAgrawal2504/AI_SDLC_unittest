package com.example.service.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * A mock SMS provider that logs the message to the console instead of sending an actual SMS.
 */
@Service
public class ConsoleSmsProvider implements ISmsProvider {

    private static final Logger logger = LogManager.getLogger(ConsoleSmsProvider.class);

    /**
     * "Sends" an SMS by printing its content to the log.
     * @param phoneNumber The destination phone number.
     * @param message The message content.
     */
    @Override
    public void sendSms(String phoneNumber, String message) {
        logger.info("--- MOCK SMS ---");
        logger.info("To: {}", phoneNumber);
        logger.info("Message: {}", message);
        logger.info("----------------");
    }
}