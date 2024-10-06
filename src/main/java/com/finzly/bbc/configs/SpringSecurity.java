package com.finzly.bbc.configs;

import com.finzly.bbc.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private JwtRequestFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http.
                cors (cors -> cors.configurationSource (request -> {
                    CorsConfiguration config = new CorsConfiguration ();
                    config.setAllowedOrigins (List.of ("*"));
                    config.setAllowedMethods (List.of ("*"));
                    config.setAllowedHeaders (List.of ("*"));
                    return config;
                })).
                authorizeHttpRequests (request -> request
//                        .requestMatchers ("/auth/otp/**", "/api-docs/**").permitAll ()
//                        .requestMatchers ("/api/auth/customers/**").fullyAuthenticated ()
                        .anyRequest ().permitAll ())
                .csrf (AbstractHttpConfigurer::disable)
                .addFilterBefore (jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build ();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager ();
    }
}