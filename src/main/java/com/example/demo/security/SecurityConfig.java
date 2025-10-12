package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Value("${security.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (req, res, ex) -> {
            res.setStatus(401);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"unauthorized\"}");
        };
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return (req, res, ex) -> {
            res.setStatus(403);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"forbidden\"}");
        };
    }

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter,
            AdminStaticTokenFilter adminStaticTokenFilter
    ) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var c = new CorsConfiguration();
                    c.setAllowedOrigins(Arrays.stream(allowedOrigins.split(",")).map(String::trim).toList());
                    c.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
                    c.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","X-Admin-Token"));
                    c.setAllowCredentials(false);
                    return c;
                }))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/hello").permitAll()
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // Admin alanı: static token VEYA ROLE_ADMIN kabul ediyoruz; filtre admin header geldiyse auth set eder.
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/hotel/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/review/**").permitAll()
                        .anyRequest().authenticated()
                );

        // Sıra: Admin static token → JWT → UsernamePasswordAuthenticationFilter
        http.addFilterBefore(adminStaticTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}