package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.client.UsuarioClient;
import com.smartlogix.bff.dto.request.ActualizarPerfilRequest;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.service.UsuarioBffService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioBffServiceImpl
        implements UsuarioBffService {

    private final UsuarioClient usuarioClient;

    public UsuarioBffServiceImpl(
            UsuarioClient usuarioClient
    ) {
        this.usuarioClient = usuarioClient;
    }

    @Override
    public UsuarioResponse crearDesdeToken() {
        return usuarioClient.crearDesdeToken();
    }

    @Override
    public UsuarioResponse miPerfil() {

        return usuarioClient.miPerfil();
    }

    @Override
    public UsuarioResponse actualizarPerfil(
            ActualizarPerfilRequest request
    ) {

        return usuarioClient.actualizarPerfil(request);
    }

    @Override
    public Boolean existeUsuario(String usuarioId) {
        return usuarioClient.existeUsuario(usuarioId);
    }

    @Override
    public java.util.List<UsuarioResponse> listarUsuarios() {
        return usuarioClient.listarUsuarios();
    }

    @Override
    public UsuarioResponse obtenerUsuario(String id) {
        return usuarioClient.obtenerUsuario(id);
    }

    @Override
    public UsuarioResponse actualizarUsuario(String id, ActualizarPerfilRequest request) {
        return usuarioClient.actualizarUsuario(id, request);
    }

    @Override
    public void desactivarUsuario(String id) {
        usuarioClient.desactivarUsuario(id);
    }

    @Override
    public void activarUsuario(String id) {
        usuarioClient.activarUsuario(id);
    }

    @Override
    public void suspenderUsuario(String id, int dias) {
        usuarioClient.suspenderUsuario(id, dias);
    }

    @Override
    public UsuarioResponse crearVendedorOChofer(String nombre, String email, String rol, String documentoIdentidad) {
        return usuarioClient.crearVendedorOChofer(nombre, email, rol, documentoIdentidad);
    }

    @Override
    public UsuarioResponse cambiarRol(String id, String rol) {
        return usuarioClient.cambiarRol(id, rol);
    }
}