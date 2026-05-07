package com.smartlogix.bff.dto;

public class UsuarioResponse {

    private String auth0Id;
    private String nombre;
    private String email;
    private String estado;

    public UsuarioResponse() {}

    public UsuarioResponse(
            String auth0Id,
            String nombre,
            String email,
            String estado
    ) {
        this.auth0Id = auth0Id;
        this.nombre = nombre;
        this.email = email;
        this.estado = estado;
    }

    public String getAuth0Id() {
        return auth0Id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getEstado() {
        return estado;
    }
}