package com.example.service;

public interface IPasswordService {
    String hashPassword(String password);
    boolean verifyPassword(String rawPassword, String encodedPassword);
}