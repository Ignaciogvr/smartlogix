package com.smartlogix.bff.security.config;

import com.smartlogix.bff.security.jwt.AudienceValidator;
import com.smartlogix.bff.security.jwt.JwtAuthConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // =========================
    // AUTH0
    // =========================

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${auth0.audience}")
    private String audience;

    // =========================
    // JWT DECODER
    // =========================

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(validator);
        return jwtDecoder;
    }

    // =========================
    // CORS
    // =========================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        return request -> {

            CorsConfiguration config =
                    new CorsConfiguration();

            // FRONTEND - local y producción
            String frontendUrlProd = System.getenv("FRONTEND_URL");
            if (frontendUrlProd != null && !frontendUrlProd.isBlank()) {
                config.addAllowedOrigin(frontendUrlProd);
            }
            
            // Desarrollo local
            config.addAllowedOrigin("http://localhost:3000");
            config.addAllowedOrigin("http://localhost:5173");
            config.addAllowedOrigin("http://127.0.0.1:5173");
            config.addAllowedOrigin("http://localhost:4200");
            config.addAllowedOrigin("http://localhost:4201");

            config.setAllowedMethods(List.of(
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "PATCH",
                    "OPTIONS"
            ));

            config.setAllowedHeaders(List.of(
                    "Authorization",
                    "Content-Type",
                    "X-Request-Id",
                    "Accept"
            ));

            config.setAllowCredentials(true);

            return config;
        };
    }

    // =========================
    // SECURITY FILTER
    // =========================

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http

            // =========================
            // CSRF
            // =========================

            .csrf(csrf -> csrf.disable())

            // =========================
            // CORS
            // =========================

            .cors(cors -> cors.configurationSource(
                    corsConfigurationSource()
            ))

            // =========================
            // ROUTES
            // =========================

            .authorizeHttpRequests(auth -> auth

                    // ====================================
                    // PUBLIC
                    // ====================================

                    .requestMatchers(
                            "/actuator/**"
                    ).permitAll()

                    .requestMatchers(
                            "/swagger-ui/**"
                    ).permitAll()

                    .requestMatchers(
                            "/v3/api-docs/**"
                    ).permitAll()

                    // ====================================
                    // CATÁLOGO PÚBLICO
                    // ====================================

                    .requestMatchers(
                            HttpMethod.GET,
                            "/catalogo/**"
                    ).permitAll()

                    // HOME FRONT
                    .requestMatchers(
                            HttpMethod.GET,
                            "/dashboard/home"
                    ).permitAll()

                    // PRODUCTOS DESTACADOS
                    .requestMatchers(
                            HttpMethod.GET,
                            "/dashboard/destacados"
                    ).permitAll()

                    // TRACKING PÚBLICO
                    .requestMatchers(
                            HttpMethod.GET,
                            "/envios/tracking/**"
                    ).permitAll()

                    // ====================================
                    // CLIENTE / ADMIN
                    // ====================================

                    .requestMatchers(
                            "/usuarios/**"
                    ).authenticated()

                    .requestMatchers(
                            "/pedidos/**"
                    ).authenticated()

                    .requestMatchers(
                            "/checkout/**"
                    ).authenticated()

                    .requestMatchers(
                            "/envios/**"
                    ).authenticated()

                    .requestMatchers(
                            "/dashboard/**"
                    ).authenticated()

                    // ====================================
                    // ADMIN
                    // ====================================

                    .requestMatchers(
                            "/admin/**"
                    ).hasRole("ADMIN")

                    // ====================================
                    // TODO LO DEMÁS
                    // ====================================

                    .anyRequest().authenticated()
            )

            // =========================
            // AUTH0 RESOURCE SERVER
            // =========================

            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(jwt ->
                            jwt.jwtAuthenticationConverter(
                                    new JwtAuthConverter()
                            )
                    )
            );

        return http.build();
    }
}