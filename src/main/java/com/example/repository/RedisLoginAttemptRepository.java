package com.example.repository;

import com.example.model.LoginAttempt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class RedisLoginAttemptRepository implements ILoginAttemptRepository {

    private final RedisTemplate<String, LoginAttempt> redisTemplate;
    private final Duration otpValidity;
    private static final String KEY_PREFIX = "login_attempt:";

    public RedisLoginAttemptRepository(RedisTemplate<String, LoginAttempt> redisTemplate,
                                       @Value("${otp.validity.duration.minutes:5}") long otpValidityMinutes) {
        this.redisTemplate = redisTemplate;
        this.otpValidity = Duration.ofMinutes(otpValidityMinutes);
    }

    /**
     * Saves a login attempt to Redis with an expiration.
     * @param attempt The login attempt to save.
     */
    @Override
    public void save(LoginAttempt attempt) {
        redisTemplate.opsForValue().set(KEY_PREFIX + attempt.getPhoneNumber(), attempt, otpValidity);
    }

    /**
     * Finds a login attempt by phone number.
     * @param phoneNumber The user's phone number.
     * @return An Optional containing the LoginAttempt if found.
     */
    @Override
    public Optional<LoginAttempt> findByPhoneNumber(String phoneNumber) {
        LoginAttempt attempt = redisTemplate.opsForValue().get(KEY_PREFIX + phoneNumber);
        return Optional.ofNullable(attempt);
    }
    
    /**
     * Deletes a login attempt from Redis.
     * @param phoneNumber The user's phone number.
     */
    @Override
    public void deleteByPhoneNumber(String phoneNumber) {
        redisTemplate.delete(KEY_PREFIX + phoneNumber);
    }
}