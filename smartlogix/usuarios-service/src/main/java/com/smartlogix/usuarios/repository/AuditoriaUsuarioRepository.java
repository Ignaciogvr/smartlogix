package com.smartlogix.usuarios.repository;

import com.smartlogix.usuarios.model.AuditoriaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaUsuarioRepository extends JpaRepository<AuditoriaUsuario, Long> {
    List<AuditoriaUsuario> findByUsuarioId(Long usuarioId);
    List<AuditoriaUsuario> findByUsuarioIdOrderByFechaAccionDesc(Long usuarioId);
}
