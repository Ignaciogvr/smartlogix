package com.smartlogix.pedidos.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 3. Falls back to scope
 */
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthConverter.class);
    private static final String NAMESPACED_ROLES_CLAIM = "https://smartlogix.com/roles";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        log.info("[JWT CONVERTER] Procesando JWT - Subject: {}, Issuer: {}", jwt.getSubject(), jwt.getIssuer());

        List<String> combined = new ArrayList<>();

        // 1. Auth0 namespaced roles (matches BFF)
        List<String> namespacedRoles = jwt.getClaimAsStringList(NAMESPACED_ROLES_CLAIM);
        if (namespacedRoles != null) {
            combined.addAll(namespacedRoles);
            log.info("[JWT CONVERTER] Namespaced roles: {}", namespacedRoles);
        }

        // 2. Auth0 permissions claim
        List<String> permissions = jwt.getClaimAsStringList("permissions");
        if (permissions != null) {
            permissions.stream().filter(p -> !combined.contains(p)).forEach(combined::add);
            log.info("[JWT CONVERTER] Permissions claim: {}", permissions);
        }

        // 3. Scope fallback
        if (combined.isEmpty()) {
            String scope = jwt.getClaimAsString("scope");
            if (scope != null) {
                List.of(scope.split(" ")).forEach(combined::add);
            }
            log.info("[JWT CONVERTER] Fallback scope: {}", scope);
        }

        var authorities = combined.stream()
                .map(r -> "ROLE_" + r.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .toList();

        log.info("[JWT CONVERTER] Authorities generadas: {}", authorities);
        return new JwtAuthenticationToken(jwt, authorities);
    }
}