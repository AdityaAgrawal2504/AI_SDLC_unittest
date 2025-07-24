package com.example.service;

import com.example.model.User;

public interface ITokenService {
    String generateToken(User user);
}