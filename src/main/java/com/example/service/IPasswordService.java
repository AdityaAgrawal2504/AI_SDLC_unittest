package com.example.service;

public interface IPasswordService {
    String hashPassword(String password);
    boolean comparePassword(String plainPassword, String hashedPassword);
}