package com.smartlogix.usuarios.service.impl;

import com.smartlogix.usuarios.model.AuditoriaUsuario;
import com.smartlogix.usuarios.repository.AuditoriaUsuarioRepository;
import com.smartlogix.usuarios.service.AuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    @Autowired
    private AuditoriaUsuarioRepository auditoriaUsuarioRepository;

    @Override
    @Transactional
    public AuditoriaUsuario registrarAccion(AuditoriaUsuario auditoria) {
        return auditoriaUsuarioRepository.save(auditoria);
    }

    @Override
    public List<AuditoriaUsuario> getAuditoriaByUsuario(Long usuarioId) {
        return auditoriaUsuarioRepository.findByUsuarioIdOrderByFechaAccionDesc(usuarioId);
    }
}
