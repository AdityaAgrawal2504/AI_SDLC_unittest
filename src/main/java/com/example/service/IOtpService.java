package com.example.service;

public interface IOtpService {
    String generateAndStoreOtp(String phoneNumber);
    boolean validateOtp(String phoneNumber, String otp);
}