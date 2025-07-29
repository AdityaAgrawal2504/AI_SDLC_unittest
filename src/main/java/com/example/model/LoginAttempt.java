package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt implements Serializable {
    private String phoneNumber;
    private String otpHash;
    private LocalDateTime expiresAt;
    private int attempts;
}