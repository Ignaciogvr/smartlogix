package com.smartlogix.inventory.security.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        // 1️⃣ primero permissions (Auth0 recomendado)
        List<String> roles = jwt.getClaimAsStringList("permissions");

        // 2️⃣ fallback a scope si permissions no existe
        if (roles == null || roles.isEmpty()) {
            String scope = jwt.getClaimAsString("scope");
            roles = scope != null ? List.of(scope.split(" ")) : List.of();
        }

        var authorities = roles.stream()
                .map(r -> "ROLE_" + r.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(jwt, authorities);
    }
}