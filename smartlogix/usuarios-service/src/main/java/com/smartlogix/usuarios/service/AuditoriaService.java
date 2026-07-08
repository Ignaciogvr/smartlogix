package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.model.AuditoriaUsuario;

import java.util.List;

public interface AuditoriaService {
    AuditoriaUsuario registrarAccion(AuditoriaUsuario auditoria);
    List<AuditoriaUsuario> getAuditoriaByUsuario(Long usuarioId);
}
