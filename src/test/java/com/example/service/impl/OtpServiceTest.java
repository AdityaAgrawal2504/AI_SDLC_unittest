package com.example.service.impl;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OtpServiceTest {

    private final OtpService otpService = new OtpService();

    @Test
    void generateOtp_shouldReturn6DigitString() {
        String otp = otpService.generateOtp();
        assertThat(otp).isNotNull();
        assertThat(otp.length()).isEqualTo(6);
        assertThat(otp).matches("\\d{6}");
    }
}