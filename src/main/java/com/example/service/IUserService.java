package com.example.service;

import com.example.dto.UserRegistrationRequest;
import com.example.dto.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse registerUser(UserRegistrationRequest registrationRequest);
    List<UserResponse> searchUsersByPhoneNumber(String phoneNumber);
}