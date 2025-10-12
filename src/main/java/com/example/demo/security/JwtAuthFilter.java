package com.example.demo.security;

import com.example.demo.resource.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwt;
    private final UserRepository users;

    public JwtAuthFilter(JwtService jwt, UserRepository users) {
        this.jwt = jwt;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String authz = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authz) && authz.startsWith("Bearer ")) {
            String token = authz.substring(7);
            try {
                var jws = jwt.parse(token);
                Integer userId = Integer.parseInt(jws.getBody().getSubject());

                var u = users.findById(userId).orElse(null);
                if (u != null) {
                    var auth = new UsernamePasswordAuthenticationToken(
                            u.getEmail(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()))
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // Controller için:
                    req.setAttribute("userId", u.getId());
                }
            } catch (Exception ignored) {
                // Geçersiz/expired token -> auth set edilmez; 401'i entry point verir
            }
        }
        chain.doFilter(req, res);
    }
}