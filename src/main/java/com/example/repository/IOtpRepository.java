package com.example.repository;

import java.util.Optional;

public interface IOtpRepository {
    void save(String key, String value, long expiryInSeconds);
    Optional<String> get(String key);
    void delete(String key);
}