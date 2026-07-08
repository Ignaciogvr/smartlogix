package com.smartlogix.bff.security.jwt;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public class AudienceValidator
        implements OAuth2TokenValidator<Jwt> {

    private final String audience;

    public AudienceValidator(
            String audience
    ) {
        this.audience = audience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(
            Jwt jwt
    ) {

        List<String> audiences =
                jwt.getAudience();

        // TOKEN SIN AUDIENCE
        if (audiences == null || audiences.isEmpty()) {

            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error(
                            "invalid_token",
                            "Token sin audience",
                            null
                    )
            );
        }

        // AUDIENCE CORRECTO
        if (audiences.contains(audience)) {

            return OAuth2TokenValidatorResult.success();
        }

        // AUDIENCE INVÁLIDO
        return OAuth2TokenValidatorResult.failure(
                new OAuth2Error(
                        "invalid_token",
                        "Audience inválido",
                        null
                )
        );
    }
}