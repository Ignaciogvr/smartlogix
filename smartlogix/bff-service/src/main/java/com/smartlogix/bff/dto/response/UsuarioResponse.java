package com.smartlogix.bff.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioResponse {

    private Long id;

    private String auth0Id;

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private String direccion;

    private String rol;

    private Boolean activo;

    /**
     * Campo crudo del microservicio usuarios (ACTIVO, etc.).
     */
    private String estado;

    public UsuarioResponse() {
    }

    public UsuarioResponse(
            String auth0Id,
            String nombre,
            String apellido,
            String email,
            String telefono,
            String direccion,
            String rol,
            Boolean activo
    ) {
        this.auth0Id = auth0Id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuth0Id() {
        return auth0Id;
    }

    public void setAuth0Id(String auth0Id) {
        this.auth0Id = auth0Id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        if (this.activo == null && estado != null) {
            this.activo = "ACTIVO".equalsIgnoreCase(estado);
        }
    }
}