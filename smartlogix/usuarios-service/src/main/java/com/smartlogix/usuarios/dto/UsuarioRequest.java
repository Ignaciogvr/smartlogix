package com.smartlogix.usuarios.dto;


public class UsuarioRequest {


    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String fotoUrl;
    private com.smartlogix.usuarios.model.Rol rol;


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }


    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }


    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public com.smartlogix.usuarios.model.Rol getRol() { return rol; }
    public void setRol(com.smartlogix.usuarios.model.Rol rol) { this.rol = rol; }
}
