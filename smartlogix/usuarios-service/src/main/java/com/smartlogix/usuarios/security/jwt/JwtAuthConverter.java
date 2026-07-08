package com.smartlogix.usuarios.security.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String NAMESPACED_ROLES_CLAIM = "https://smartlogix.com/roles";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        List<String> roles = new ArrayList<>();

        List<String> namespacedRoles = jwt.getClaimAsStringList(NAMESPACED_ROLES_CLAIM);
        if (namespacedRoles != null) {
            roles.addAll(namespacedRoles);
        }

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        if (permissions != null) {
            for (String permission : permissions) {
                if (!roles.contains(permission)) {
                    roles.add(permission);
                }
            }
        }

        List<String> genericRoles = jwt.getClaimAsStringList("roles");
        if (genericRoles != null) {
            for (String role : genericRoles) {
                if (!roles.contains(role)) {
                    roles.add(role);
                }
            }
        }

        if (roles.isEmpty()) {
            String scope = jwt.getClaimAsString("scope");
            roles = (scope != null) ? List.of(scope.split(" ")) : List.of();
        }

        var authorities = roles.stream()
                .map(r -> "ROLE_" + r.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new JwtAuthenticationToken(jwt, authorities);
    }
}
