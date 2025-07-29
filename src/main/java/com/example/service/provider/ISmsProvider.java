package com.example.service.provider;

/**
 * An abstraction for an external SMS sending service.
 */
public interface ISmsProvider {
    /**
     * Sends an SMS message to a given phone number.
     * @param phoneNumber The destination phone number.
     * @param message The content of the SMS.
     */
    void sendSms(String phoneNumber, String message);
}