package com.example.repository;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOtpRepository implements IOtpRepository {

    private final ConcurrentHashMap<String, OtpEntry> store = new ConcurrentHashMap<>();

    @Override
    public void save(String key, String value, long expiryInSeconds) {
        Instant expiryTime = Instant.now().plusSeconds(expiryInSeconds);
        store.put(key, new OtpEntry(value, expiryTime));
    }

    @Override
    public Optional<String> get(String key) {
        OtpEntry entry = store.get(key);
        if (entry != null && Instant.now().isBefore(entry.expiryTime)) {
            return Optional.of(entry.value);
        }
        // Clean up expired entry
        if (entry != null) {
            store.remove(key);
        }
        return Optional.empty();
    }

    @Override
    public void delete(String key) {
        store.remove(key);
    }

    private static class OtpEntry {
        final String value;
        final Instant expiryTime;

        OtpEntry(String value, Instant expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }
    }
}