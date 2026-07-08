package com.smartlogix.inventory.security.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Aligns role resolution with bff-service:
 * 1. Reads Auth0 namespaced claim: "https://smartlogix.com/roles"
 * 2. Falls back to "permissions" (Auth0 M2M permissions)
 * 3. Falls back to generic "roles"
 */
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String NAMESPACED_ROLES_CLAIM = "https://smartlogix.com/roles";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        List<String> combined = new ArrayList<>();

        List<String> namespacedRoles = jwt.getClaimAsStringList(NAMESPACED_ROLES_CLAIM);
        if (namespacedRoles != null) {
            combined.addAll(namespacedRoles);
        }

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        if (permissions != null) {
            permissions.stream()
                    .filter(p -> !combined.contains(p))
                    .forEach(combined::add);
        }

        if (combined.isEmpty()) {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null) {
                combined.addAll(roles);
            }
        }

        var authorities = combined.stream()
                .map(String::toUpperCase)
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(jwt, authorities);
    }
}