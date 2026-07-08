package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.model.DireccionUsuario;

import java.util.List;

public interface PerfilUsuarioService {
    DireccionUsuario addDireccion(Long usuarioId, DireccionUsuario direccion);
    List<DireccionUsuario> getDireccionesByUsuario(Long usuarioId);
    DireccionUsuario getDireccionById(Long id);
    DireccionUsuario updateDireccion(Long id, DireccionUsuario direccion);
    void deleteDireccion(Long id);
    void setDireccionPredeterminada(Long usuarioId, Long direccionId);
}
