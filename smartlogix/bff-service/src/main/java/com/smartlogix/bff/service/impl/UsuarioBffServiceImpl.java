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
    public Boolean existeUsuario(
            String usuarioId
    ) {

        return usuarioClient.existeUsuario(usuarioId);
    }
}