package com.smartlogix.pedidos.security.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import com.smartlogix.pedidos.security.jwt.AudienceValidator;
import com.smartlogix.pedidos.security.jwt.JwtAuthConverter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ISSUER =
            "https://dev-nomnv0fhn3zpzt4t.us.auth0.com/";
    private static final String AUDIENCE =
            "https://smartlogix-api";

    @Bean
    public JwtDecoder jwtDecoder() {

        NimbusJwtDecoder jwtDecoder =
                (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(ISSUER);

        OAuth2TokenValidator<Jwt> withIssuer =
                JwtValidators.createDefaultWithIssuer(ISSUER);

        OAuth2TokenValidator<Jwt> audienceValidator =
                new AudienceValidator(AUDIENCE);

        OAuth2TokenValidator<Jwt> validator =
                new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(request -> {
                var config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:4200"));
                config.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
                config.setAllowedHeaders(List.of("*"));
                return config;
            }))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/pedidos").hasAnyRole("CLIENTE","ADMIN")
                .requestMatchers(HttpMethod.GET, "/pedidos/usuario/**").hasAnyRole("CLIENTE","ADMIN")
                .requestMatchers(HttpMethod.GET, "/pedidos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/pedidos/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )

            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(new JwtAuthConverter())
                )
            );

        return http.build();
    }
}