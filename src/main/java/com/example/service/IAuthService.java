package com.example.service;

import com.example.dto.LoginInitiateDto;
import com.example.dto.LoginVerifyDto;
import com.example.dto.UserSignupDto;
import com.example.model.User;

public interface IAuthService {
    User registerUser(UserSignupDto userSignupDto);
    void initiateLogin(LoginInitiateDto loginInitiateDto);
    String completeLogin(LoginVerifyDto loginVerifyDto);
}