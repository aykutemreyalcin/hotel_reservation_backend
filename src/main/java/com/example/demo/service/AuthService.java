package com.example.demo.service;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.resource.entity.User;
import com.example.demo.resource.enumeration.UserRole;
import com.example.demo.resource.repository.UserRepository;
import com.example.demo.security.AuthMapper;
import com.example.demo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JwtService jwt) {
        this.userRepository = userRepository; this.encoder = encoder; this.jwt = jwt;
    }

    public TokenResponse register(RegisterRequest r) {
        if (userRepository.existsByEmail(r.email)) throw new IllegalArgumentException("Email already in use");
        var u = new User();
        u.setEmail(r.email.trim().toLowerCase());
        u.setName(r.name);
        u.setPasswordHash(encoder.encode(r.password));
        u.setRole(UserRole.customer);
        userRepository.save(u);
        return tokenFor(u);
    }

    public TokenResponse login(LoginRequest r) {
        var u = userRepository.findByEmail(r.email.trim().toLowerCase()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(r.password, u.getPasswordHash())) throw new IllegalArgumentException("Invalid credentials");
        return tokenFor(u);
    }

    private TokenResponse tokenFor(User u) {
        String token = jwt.create(u.getId().toString(), Map.of("role", u.getRole().name(), "email", u.getEmail()));
        var tr = new TokenResponse();
        tr.accessToken = token;
        tr.expiresInSec = 60L * 60L; // config ile uyumlu
        tr.user = AuthMapper.toDto(u);
        return tr;
    }
}