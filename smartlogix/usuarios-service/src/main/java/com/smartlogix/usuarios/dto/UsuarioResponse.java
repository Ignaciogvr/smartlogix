package com.smartlogix.usuarios.dto;

public class UsuarioResponse {

    private String auth0Id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String fotoUrl;
    private String estado;
    private com.smartlogix.usuarios.model.Rol rol;

    public UsuarioResponse(String auth0Id, String nombre, String email,
                           String telefono, String direccion,
                           String fotoUrl, String estado, com.smartlogix.usuarios.model.Rol rol) {
        this.auth0Id = auth0Id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fotoUrl = fotoUrl;
        this.estado = estado;
        this.rol = rol;
    }

    public String getAuth0Id() { return auth0Id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getFotoUrl() { return fotoUrl; }
    public String getEstado() { return estado; }
    public com.smartlogix.usuarios.model.Rol getRol() { return rol; }
}