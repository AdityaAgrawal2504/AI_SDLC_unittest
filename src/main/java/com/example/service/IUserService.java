package com.example.service;

import com.example.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface IUserService extends UserDetailsService {
    Optional<User> findUserById(UUID id);
    Optional<User> findUserByPhoneNumber(String phoneNumber);
    UserDetails loadUserById(UUID id);
}