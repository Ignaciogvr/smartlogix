package com.smartlogix.usuarios.service.impl;

import com.smartlogix.usuarios.exception.BusinessException;
import com.smartlogix.usuarios.model.Usuario;
import com.smartlogix.usuarios.repository.UsuarioRepository;
import com.smartlogix.usuarios.service.UsuarioService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario crearDesdeToken(
            String auth0Id,
            String email,
            String nombre
    ) {

        return repository.findByAuth0Id(auth0Id)
                .orElseGet(() -> {

                    Usuario u = new Usuario();

                    u.setAuth0Id(auth0Id);
                    u.setEmail(email);
                    u.setNombre(nombre);
                    u.setEstado("ACTIVO");

                    return repository.save(u);
                });
    }

    @Override
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @Override
    public Usuario obtenerPorUserId(String auth0Id) {

        return repository.findByAuth0Id(auth0Id)
                .orElseThrow(() ->
                        new BusinessException(
                                "USER_NOT_FOUND",
                                "Usuario no encontrado"
                        )
                );
    }

    @Override
    public Usuario actualizarPorUserId(
            String auth0Id,
            Usuario datos
    ) {

        Usuario u = obtenerPorUserId(auth0Id);

        // 🔥 actualizar nombre
        if (datos.getNombre() != null) {
            u.setNombre(datos.getNombre());
        }

        // 🔥 actualizar email
        if (datos.getEmail() != null) {
            u.setEmail(datos.getEmail());
        }

        // 🔥 FIX IMPORTANTE:
        // permitir actualizar estado
        if (datos.getEstado() != null) {
            u.setEstado(datos.getEstado());
        }

        return repository.save(u);
    }

    @Override
    public void eliminarPorUserId(String auth0Id) {

        Usuario u = obtenerPorUserId(auth0Id);

        u.setEstado("INACTIVO");

        repository.save(u);
    }

    @Override
    public boolean existePorAuth0Id(String auth0Id) {

        return repository.findByAuth0Id(auth0Id)
                .isPresent();
    }
}