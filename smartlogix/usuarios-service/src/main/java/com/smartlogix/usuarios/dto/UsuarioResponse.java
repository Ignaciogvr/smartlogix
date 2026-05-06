package com.smartlogix.usuarios.dto;

public class UsuarioResponse {

    private String auth0Id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String fotoUrl;
    private String estado;

    public UsuarioResponse(String auth0Id, String nombre, String email,
                           String telefono, String direccion,
                           String fotoUrl, String estado) {
        this.auth0Id = auth0Id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fotoUrl = fotoUrl;
        this.estado = estado;
    }

    public String getAuth0Id() { return auth0Id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getFotoUrl() { return fotoUrl; }
    public String getEstado() { return estado; }
}