package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.model.Usuario;
import com.smartlogix.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario crearDesdeToken(String auth0Id, String email, String nombre) {

        Optional<Usuario> existente = repository.findByAuth0Id(auth0Id);
        if (existente.isPresent()) return existente.get();

        Usuario u = new Usuario();
        u.setAuth0Id(auth0Id);
        u.setEmail(email);
        u.setNombre(nombre);
        u.setPassword("PROTEGIDO");
        u.setEstado("ACTIVO");

        return repository.save(u);
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public Usuario obtenerPorUserId(String auth0Id) {
        return repository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario actualizarPorUserId(String auth0Id, Usuario datos) {
        Usuario u = obtenerPorUserId(auth0Id);

        if (datos.getNombre() != null) u.setNombre(datos.getNombre());
        if (datos.getEmail() != null) u.setEmail(datos.getEmail());

        return repository.save(u);
    }

    public void eliminarPorUserId(String auth0Id) {
        Usuario u = obtenerPorUserId(auth0Id);
        u.setEstado("INACTIVO");
        repository.save(u);
    }

    // 🔥 NUEVO FIX CRÍTICO
    public boolean existePorAuth0Id(String auth0Id) {
        return repository.findByAuth0Id(auth0Id).isPresent();
    }
}