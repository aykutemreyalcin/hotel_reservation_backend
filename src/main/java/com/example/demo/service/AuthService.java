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
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public TokenResponse register(RegisterRequest r) {
        userRepository.findByEmail(r.email).ifPresent(u -> {
            throw new IllegalArgumentException("email_in_use");
        });

        var u = new User();
        u.setName(r.name);
        u.setEmail(r.email);
        u.setPasswordHash(encoder.encode(r.password));
        u.setRole(UserRole.customer);
        userRepository.save(u);

        return tokenFor(u);
    }

    public TokenResponse login(LoginRequest r) {
        var u = userRepository.findByEmail(r.email)
                .orElseThrow(() -> new IllegalArgumentException("invalid_credentials"));
        if (!encoder.matches(r.password, u.getPasswordHash())) {
            throw new IllegalArgumentException("invalid_credentials");
        }
        return tokenFor(u);
    }

    private TokenResponse tokenFor(User user) {
        String token = jwt.create(user.getId().toString(), Map.of(
                "role", user.getRole().name(),
                "email", user.getEmail()
        ));
        var tr = new TokenResponse();
        tr.accessToken = token;
        tr.expiresInSec = 60L * 60L;
        tr.user = AuthMapper.toDto(user);
        return tr;
    }
}