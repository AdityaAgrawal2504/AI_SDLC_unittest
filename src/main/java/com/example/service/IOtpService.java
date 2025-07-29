package com.example.service;

public interface IOtpService {
    String generateOtp();
    void sendOtp(String phoneNumber, String otp);
}