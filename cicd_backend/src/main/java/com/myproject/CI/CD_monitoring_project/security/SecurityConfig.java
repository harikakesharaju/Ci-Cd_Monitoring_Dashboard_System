package com.myproject.CI.CD_monitoring_project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    	http.csrf(csrf -> csrf.disable())
        .sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth

            // 🔥 ADD THIS LINE
            .requestMatchers("/webhook/**").permitAll()

            // existing rules
            .requestMatchers("/auth/users/**").hasRole("ADMIN")
            .requestMatchers("/auth/**").permitAll()

            .requestMatchers("/projects/**")
                .hasAnyRole("ADMIN", "DEVELOPER", "OPS")

            .requestMatchers("/builds/**", "/metrics/**")
                .hasAnyRole("ADMIN", "DEVELOPER", "VIEWER", "QA", "OPS")

            .requestMatchers("/deployments/**")
                .hasAnyRole("ADMIN", "OPS")

            .requestMatchers("/tests/**")
                .hasAnyRole("QA", "ADMIN")

            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}