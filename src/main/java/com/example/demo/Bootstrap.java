package com.example.demo;

import com.example.demo.resource.entity.User;
import com.example.demo.resource.enumeration.UserRole;
import com.example.demo.resource.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
    private final UserRepository users; private final PasswordEncoder enc;
    @Value("${app.admin.email}") String adminEmail;
    @Value("${app.admin.password}") String adminPass;

    public Bootstrap(UserRepository users, PasswordEncoder enc) { this.users = users; this.enc = enc; }

    @Override public void run(String... args) {
        users.findByEmail(adminEmail).ifPresentOrElse(u -> {}, () -> {
            var u = new User();
            u.setEmail(adminEmail); u.setName("Admin"); u.setPasswordHash(enc.encode(adminPass)); u.setRole(UserRole.admin);
            users.save(u);
        });
    }
}