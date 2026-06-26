package com.smartlogix.bff.util;

import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    // =========================
    // VALIDAR TOKEN SIMPLE
    // =========================

    public boolean isValid(String token) {

        return token != null && !token.isBlank();
    }

    // =========================
    // EXTRAER USER ID (SIMULADO AHORA)
    // =========================

    public String extractUserId(String token) {

        if (!isValid(token)) {
            return null;
        }

        // FUTURO: parse JWT / Auth0 sub
        return "user-id-from-token";
    }
}