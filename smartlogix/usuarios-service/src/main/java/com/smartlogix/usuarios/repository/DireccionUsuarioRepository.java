package com.smartlogix.usuarios.repository;

import com.smartlogix.usuarios.model.DireccionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DireccionUsuarioRepository extends JpaRepository<DireccionUsuario, Long> {
    List<DireccionUsuario> findByUsuarioId(Long usuarioId);
    List<DireccionUsuario> findByUsuarioIdAndActivaTrue(Long usuarioId);
}
