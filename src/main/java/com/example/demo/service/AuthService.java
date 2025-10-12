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

    public TokenResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email)) throw new IllegalArgumentException("Email already in use");
        var user = new User();
        user.setEmail(registerRequest.email.trim().toLowerCase());
        user.setName(registerRequest.name);
        user.setPasswordHash(encoder.encode(registerRequest.password));
        user.setRole(UserRole.customer);
        user.setPhone(registerRequest.phone);
        userRepository.save(user);
        return tokenFor(user);
    }

    public TokenResponse login(LoginRequest loginRequest) {
        var u = userRepository.findByEmail(loginRequest.email.trim().toLowerCase()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!encoder.matches(loginRequest.password, u.getPasswordHash())) throw new IllegalArgumentException("Invalid credentials");
        return tokenFor(u);
    }

    private TokenResponse tokenFor(User user) {
        String token = jwt.create(user.getId().toString(), Map.of("role", user.getRole().name(), "email", user.getEmail()));
        var tr = new TokenResponse();
        tr.accessToken = token;
        tr.expiresInSec = 60L * 60L; // config ile uyumlu
        tr.user = AuthMapper.toDto(user);
        return tr;
    }
}