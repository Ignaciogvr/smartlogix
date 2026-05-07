package com.smartlogix.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();

                config.setAllowedOrigins(List.of("*"));
                config.setAllowedMethods(List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE"
                ));
                config.setAllowedHeaders(List.of("*"));

                return config;
            }))

            .authorizeHttpRequests(auth -> auth

                    // 🔓 catálogo público
                    .requestMatchers(HttpMethod.GET, "/catalogo/**")
                    .permitAll()

                    // 🔐 resto autenticado
                    .anyRequest()
                    .authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwt ->
                            jwt.jwtAuthenticationConverter(
                                    new JwtAuthenticationConverter()
                            )
                    )
            );

        return http.build();
    }
}