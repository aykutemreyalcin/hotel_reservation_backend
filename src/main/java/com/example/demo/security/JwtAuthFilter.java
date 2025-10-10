package com.example.demo.security;

import com.example.demo.resource.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class JwtAuthFilter extends GenericFilter {
    private final JwtService jwt;
    private final UserRepository users;

    public JwtAuthFilter(JwtService jwt, UserRepository users) { this.jwt = jwt; this.users = users; }

    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        String header = r.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var jws = jwt.parse(token);
                String userId = jws.getBody().getSubject();
                var user = users.findById(Integer.valueOf(userId)).orElse(null);
                if (user != null) {
                    GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_" + user.getRole().name().toUpperCase());
                    Authentication a = new UsernamePasswordAuthenticationToken(user.getEmail(), null, List.of(auth));
                    SecurityContextHolder.getContext().setAuthentication(a);
                }
            } catch (Exception ignored) {}
        }
        chain.doFilter(req, res);
    }
}