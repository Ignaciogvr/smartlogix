package com.smartlogix.usuarios.repository;

import com.smartlogix.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    // 🔥 NUEVO
    Optional<Usuario> findByAuth0Id(String auth0Id);
}