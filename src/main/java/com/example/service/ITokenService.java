package com.example.service;

import com.example.security.UserPrincipal;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface ITokenService {
    String generateToken(Authentication authentication);
    String generateToken(UserPrincipal userPrincipal);
    boolean validateToken(String token);
    UUID getUserIdFromToken(String token);
}