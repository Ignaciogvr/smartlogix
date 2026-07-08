package com.smartlogix.usuarios.service.impl;

import com.smartlogix.usuarios.model.DireccionUsuario;
import com.smartlogix.usuarios.model.Usuario;
import com.smartlogix.usuarios.repository.DireccionUsuarioRepository;
import com.smartlogix.usuarios.repository.UsuarioRepository;
import com.smartlogix.usuarios.service.PerfilUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PerfilUsuarioServiceImpl implements PerfilUsuarioService {

    @Autowired
    private DireccionUsuarioRepository direccionUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public DireccionUsuario addDireccion(Long usuarioId, DireccionUsuario direccion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
        direccion.setUsuario(usuario);
        
        if (direccion.getEsPredeterminada() != null && direccion.getEsPredeterminada()) {
            resetDireccionesPredeterminadas(usuarioId);
        }
        
        return direccionUsuarioRepository.save(direccion);
    }

    @Override
    public List<DireccionUsuario> getDireccionesByUsuario(Long usuarioId) {
        return direccionUsuarioRepository.findByUsuarioIdAndActivaTrue(usuarioId);
    }

    @Override
    public DireccionUsuario getDireccionById(Long id) {
        return direccionUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Direccion no encontrada con ID: " + id));
    }

    @Override
    @Transactional
    public DireccionUsuario updateDireccion(Long id, DireccionUsuario direccionData) {
        DireccionUsuario direccion = getDireccionById(id);
        
        if (direccionData.getEsPredeterminada() != null && direccionData.getEsPredeterminada() && !direccion.getEsPredeterminada()) {
            resetDireccionesPredeterminadas(direccion.getUsuario().getId());
        }

        direccion.setTipoDireccion(direccionData.getTipoDireccion());
        direccion.setCalle(direccionData.getCalle());
        direccion.setNumero(direccionData.getNumero());
        direccion.setDepartamento(direccionData.getDepartamento());
        direccion.setComuna(direccionData.getComuna());
        direccion.setCiudad(direccionData.getCiudad());
        direccion.setRegion(direccionData.getRegion());
        direccion.setCodigoPostal(direccionData.getCodigoPostal());
        direccion.setPais(direccionData.getPais());
        direccion.setTelefono(direccionData.getTelefono());
        direccion.setNotas(direccionData.getNotas());
        direccion.setActiva(direccionData.getActiva());
        
        if (direccionData.getEsPredeterminada() != null) {
            direccion.setEsPredeterminada(direccionData.getEsPredeterminada());
        }
        
        return direccionUsuarioRepository.save(direccion);
    }

    @Override
    @Transactional
    public void deleteDireccion(Long id) {
        DireccionUsuario direccion = getDireccionById(id);
        direccion.setActiva(false);
        direccionUsuarioRepository.save(direccion);
    }

    @Override
    @Transactional
    public void setDireccionPredeterminada(Long usuarioId, Long direccionId) {
        resetDireccionesPredeterminadas(usuarioId);
        DireccionUsuario direccion = getDireccionById(direccionId);
        if (!direccion.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("La direccion no pertenece al usuario");
        }
        direccion.setEsPredeterminada(true);
        direccionUsuarioRepository.save(direccion);
    }
    
    private void resetDireccionesPredeterminadas(Long usuarioId) {
        List<DireccionUsuario> direcciones = direccionUsuarioRepository.findByUsuarioId(usuarioId);
        for (DireccionUsuario d : direcciones) {
            if (Boolean.TRUE.equals(d.getEsPredeterminada())) {
                d.setEsPredeterminada(false);
                direccionUsuarioRepository.save(d);
            }
        }
    }
}
