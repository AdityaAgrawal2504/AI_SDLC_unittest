package com.example.repository;

import com.example.model.LoginAttempt;
import java.util.Optional;

public interface ILoginAttemptRepository {
    void save(LoginAttempt attempt);
    Optional<LoginAttempt> findByPhoneNumber(String phoneNumber);
    void deleteByPhoneNumber(String phoneNumber);
}