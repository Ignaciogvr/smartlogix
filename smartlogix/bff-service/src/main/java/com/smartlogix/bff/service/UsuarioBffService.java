package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.request.ActualizarPerfilRequest;
import com.smartlogix.bff.dto.response.UsuarioResponse;

public interface UsuarioBffService {

    UsuarioResponse crearDesdeToken();

    UsuarioResponse miPerfil();

    UsuarioResponse actualizarPerfil(
            ActualizarPerfilRequest request
    );

    Boolean existeUsuario(
            String usuarioId
    );

    java.util.List<UsuarioResponse> listarUsuarios();
    
    UsuarioResponse obtenerUsuario(String id);
    
    UsuarioResponse actualizarUsuario(String id, ActualizarPerfilRequest request);
    
    void desactivarUsuario(String id);
    
    void activarUsuario(String id);

    void suspenderUsuario(String id, int dias);

    UsuarioResponse crearVendedorOChofer(String nombre, String email, String rol, String documentoIdentidad);
    
    UsuarioResponse cambiarRol(String id, String rol);
}