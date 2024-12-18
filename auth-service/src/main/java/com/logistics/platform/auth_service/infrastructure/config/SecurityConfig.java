package com.logistics.platform.auth_service.infrastructure.config;

import com.logistics.platform.auth_service.application.service.CustomUserDetailsService;
import com.logistics.platform.auth_service.infrastructure.config.jwt.filter.JwtAuthorizationFilter;
import com.logistics.platform.auth_service.infrastructure.config.jwt.JwtUtil;
import com.logistics.platform.auth_service.infrastructure.config.jwt.filter.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf((csrf) -> csrf.disable());

        http
            .formLogin((formLogin) -> formLogin.disable());

        http
            .httpBasic((httpBasic) -> httpBasic.disable());

        http
            .addFilterBefore(new JwtAuthorizationFilter(customUserDetailsService), UsernamePasswordAuthenticationFilter.class);

        http
            .authorizeHttpRequests(
                (auth) -> auth.requestMatchers("/swagger/**").permitAll()
                    .requestMatchers( "/api/auth/**").permitAll()
                    .requestMatchers("/api/users/**").permitAll()
                    .anyRequest().authenticated());

        http
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        http
            .sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}