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

        List<String> roles = jwt.getClaimAsStringList("permissions");

        if (roles == null || roles.isEmpty()) {
            roles = jwt.getClaimAsStringList("roles");
        }

        if (roles == null) {
            roles = List.of();
        }

        var authorities = roles.stream()
                .map(String::toUpperCase)
                .map(role -> "ROLE_" + role)   // 🔥 FIX CRÍTICO
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(jwt, authorities);
    }
}