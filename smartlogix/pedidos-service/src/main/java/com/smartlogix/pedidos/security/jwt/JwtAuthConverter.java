package com.smartlogix.pedidos.security.jwt; // (cambia el package según microservicio)

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthConverter.class);

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        log.info("[JWT CONVERTER] Procesando JWT - Subject: {}, Issuer: {}", jwt.getSubject(), jwt.getIssuer());

        // 1️⃣ primero permissions (Auth0 recomendado)
        List<String> roles = jwt.getClaimAsStringList("permissions");
        log.info("[JWT CONVERTER] Permissions claim: {}", roles);

        // 2️⃣ fallback a scope si permissions no existe
        if (roles == null || roles.isEmpty()) {
            String scope = jwt.getClaimAsString("scope");
            log.info("[JWT CONVERTER] Permissions vacío, usando scope: {}", scope);
            roles = scope != null ? List.of(scope.split(" ")) : List.of();
        }

        var authorities = roles.stream()
                .map(r -> "ROLE_" + r.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .toList();

        log.info("[JWT CONVERTER] Authorities generadas: {}", authorities);

        return new JwtAuthenticationToken(jwt, authorities);
    }
}