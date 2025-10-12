package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
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
public class AdminStaticTokenFilter extends OncePerRequestFilter {

    private final boolean enabled;
    private final String headerName;
    private final String tokenValue;

    public AdminStaticTokenFilter(
            @Value("${admin.static-token-enabled:true}") boolean enabled,
            @Value("${admin.static-token-header:X-Admin-Token}") String headerName,
            @Value("${admin.static-token:}") String tokenValue
    ) {
        this.enabled = enabled;
        this.headerName = headerName;
        this.tokenValue = tokenValue;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        if (enabled) {
            String token = req.getHeader(headerName);
            if (StringUtils.hasText(token)) {
                if (!tokenValue.equals(token)) {
                    res.setStatus(403);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"invalid_admin_token\"}");
                    return;
                }
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                "admin-static", null,
                                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        )
                );
            }
        }
        chain.doFilter(req, res);
    }
}