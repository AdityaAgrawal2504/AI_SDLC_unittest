package com.example.messagingapp.service;

public interface OTPService {
    /**
     * Generates a new OTP and sends it to the user's phone number.
     * @param phoneNumber The target phone number in E.164 format.
     */
    void generateAndSend(String phoneNumber);

    /**
     * Verifies if the provided OTP is valid for the given phone number.
     * @param phoneNumber The phone number associated with the OTP.
     * @param otp The 6-digit OTP string to verify.
     * @return true if the OTP is valid, false otherwise.
     */
    boolean verify(String phoneNumber, String otp);
}