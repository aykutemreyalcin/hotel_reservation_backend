package com.example.demo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.List;

public class AdminStaticTokenFilter extends GenericFilter {
    private final String staticToken;
    public AdminStaticTokenFilter(@Value("${app.admin.static-token}") String staticToken) { this.staticToken = staticToken; }

    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        String header = r.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) && header.startsWith("Admin ")) {
            String token = header.substring(6);
            if (token.equals(staticToken)) {
                Authentication a = new UsernamePasswordAuthenticationToken("admin-static", null,
                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
                SecurityContextHolder.getContext().setAuthentication(a);
            }
        }
        chain.doFilter(req, res);
    }
}