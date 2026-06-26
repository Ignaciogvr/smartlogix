package com.smartlogix.usuarios.service.impl;

import com.smartlogix.usuarios.exception.BusinessException;
import com.smartlogix.usuarios.model.Usuario;
import com.smartlogix.usuarios.repository.UsuarioRepository;
import com.smartlogix.usuarios.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario crearDesdeToken(String auth0Id, String email, String nombre) {
        log.debug("Buscando usuario existente - auth0Id: {}", auth0Id);

        return repository.findByAuth0Id(auth0Id)
                .map(usuario -> {
                    log.info("Usuario encontrado - auth0Id: {}", auth0Id);
                    return usuario;
                })
                .orElseGet(() -> {
                    log.info("Usuario no existe, creando nuevo - auth0Id: {}, email: {}", auth0Id, email);

                    Usuario u = new Usuario();

                    u.setAuth0Id(auth0Id);
                    u.setEmail(email);
                    u.setNombre(nombre);
                    u.setEstado("ACTIVO");

                    Usuario usuarioGuardado = repository.save(u);

                    log.info("Usuario creado exitosamente - id: {}, auth0Id: {}", usuarioGuardado.getId(), auth0Id);

                    return usuarioGuardado;
                });
    }

    @Override
    public List<Usuario> listar() {
        log.debug("Listando todos los usuarios");

        List<Usuario> usuarios = repository.findAll();

        log.info("Usuarios listados - total: {}", usuarios.size());

        return usuarios;
    }

    @Override
    public Usuario obtenerPorUserId(String auth0Id) {
        log.debug("Buscando usuario - auth0Id: {}", auth0Id);

        return repository.findByAuth0Id(auth0Id)
                .map(usuario -> {
                    log.info("Usuario encontrado - id: {}, auth0Id: {}", usuario.getId(), auth0Id);
                    return usuario;
                })
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado - auth0Id: {}", auth0Id);
                    return new BusinessException(
                            "USER_NOT_FOUND",
                            "Usuario no encontrado"
                    );
                });
    }

    @Override
    public Usuario actualizarPorUserId(String auth0Id, Usuario datos) {
        log.debug("Actualizando usuario - auth0Id: {}", auth0Id);

        Usuario u = obtenerPorUserId(auth0Id);

        boolean cambiosRealizados = false;

        if (datos.getNombre() != null) {
            log.debug("Actualizando nombre - auth0Id: {}", auth0Id);
            u.setNombre(datos.getNombre());
            cambiosRealizados = true;
        }

        if (datos.getEmail() != null) {
            log.debug("Actualizando email - auth0Id: {}", auth0Id);
            u.setEmail(datos.getEmail());
            cambiosRealizados = true;
        }

        if (datos.getEstado() != null) {
            log.debug("Actualizando estado - auth0Id: {}, nuevo estado: {}", auth0Id, datos.getEstado());
            u.setEstado(datos.getEstado());
            cambiosRealizados = true;
        }

        if (!cambiosRealizados) {
            log.warn("Actualización solicitada sin cambios - auth0Id: {}", auth0Id);
        }

        Usuario usuarioActualizado = repository.save(u);

        log.info("Usuario actualizado exitosamente - id: {}, auth0Id: {}", usuarioActualizado.getId(), auth0Id);

        return usuarioActualizado;
    }

    @Override
    public void eliminarPorUserId(String auth0Id) {
        log.info("Eliminando usuario (soft delete) - auth0Id: {}", auth0Id);

        Usuario u = obtenerPorUserId(auth0Id);

        u.setEstado("INACTIVO");

        repository.save(u);

        log.info("Usuario marcado como INACTIVO - id: {}, auth0Id: {}", u.getId(), auth0Id);
    }

    @Override
    public boolean existePorAuth0Id(String auth0Id) {
        log.debug("Verificando existencia de usuario - auth0Id: {}", auth0Id);

        boolean existe = repository.findByAuth0Id(auth0Id).isPresent();

        log.debug("Usuario existe: {} - auth0Id: {}", existe, auth0Id);

        return existe;
    }
}