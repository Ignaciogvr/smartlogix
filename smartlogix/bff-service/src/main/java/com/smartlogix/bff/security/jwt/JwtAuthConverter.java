package com.smartlogix.bff.security.jwt;

import org.springframework.core.convert.converter.Converter;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthConverter
        implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(
            Jwt jwt
    ) {

        Collection<SimpleGrantedAuthority> authorities =
                new ArrayList<>();

        // =========================
        // AUTH0 PERMISSIONS
        // =========================

        List<String> permissions =
                jwt.getClaimAsStringList("permissions");

        if (permissions != null) {

            authorities.addAll(

                    permissions.stream()

                            .map(String::toUpperCase)

                            .map(permission ->
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + permission
                                    )
                            )

                            .toList()
            );
        }

        // =========================
        // AUTH0 ROLES
        // =========================

        List<String> roles =
                jwt.getClaimAsStringList(
                        "https://smartlogix.com/roles"
                );

        if (roles != null) {

            authorities.addAll(

                    roles.stream()

                            .map(String::toUpperCase)

                            .map(role ->
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + role
                                    )
                            )

                            .toList()
            );
        }

        // =========================
        // SUBJECT AUTH0
        // =========================

        String principalName =
                jwt.getSubject();

        return new JwtAuthenticationToken(
                jwt,
                authorities,
                principalName
        );
    }
}