package com.smartlogix.bff.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> bearerToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return Optional.of("Bearer " + jwtAuth.getToken().getTokenValue());
        }
        return Optional.empty();
    }

    public static Optional<String> auth0Subject() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            return Optional.ofNullable(jwtAuth.getToken().getSubject());
        }
        return Optional.empty();
    }

    /**
     * Extrae el nombre real del usuario desde claims JWT.
     * Prioridad: claim "name" → claim "nickname" → fallback "Usuario"
     */
    public static String extractUserName(Jwt jwt) {
        if (jwt == null) return "Usuario";
        String name = jwt.getClaimAsString("name");
        if (name != null && !name.isBlank()) return name;
        String nickname = jwt.getClaimAsString("nickname");
        if (nickname != null && !nickname.isBlank()) return nickname;
        return "Usuario";
    }

    /**
     * Verifica si el usuario autenticado tiene el rol indicado.
     * Compatible con roles Auth0 mapeados como ROLE_ADMIN, ROLE_VENDEDOR, etc.
     */
    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        String normalizedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equalsIgnoreCase(normalizedRole));
    }
}
