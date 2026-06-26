package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.request.ActualizarPerfilRequest;
import com.smartlogix.bff.dto.response.UsuarioResponse;

public interface UsuarioBffService {

    UsuarioResponse miPerfil();

    UsuarioResponse actualizarPerfil(
            ActualizarPerfilRequest request
    );

    Boolean existeUsuario(
            String usuarioId
    );
}