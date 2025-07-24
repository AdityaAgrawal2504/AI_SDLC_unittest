package com.example.service;

import com.example.dto.RequestOtpRequest;
import com.example.dto.VerifyOtpRequest;
import com.example.dto.VerifyOtpResponse;

public interface IAuthService {
    void requestLoginOtp(RequestOtpRequest request);
    VerifyOtpResponse verifyLoginOtp(VerifyOtpRequest request);
}