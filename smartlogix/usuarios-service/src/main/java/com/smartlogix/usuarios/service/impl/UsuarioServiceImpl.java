package com.smartlogix.usuarios.service.impl;

import com.smartlogix.usuarios.exception.BusinessException;
import com.smartlogix.usuarios.model.Usuario;
import com.smartlogix.usuarios.repository.UsuarioRepository;
import com.smartlogix.usuarios.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.smartlogix.usuarios.kafka.producer.KafkaProducerService;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository repository;
    private final KafkaProducerService producer;

    public UsuarioServiceImpl(UsuarioRepository repository, KafkaProducerService producer) {
        this.repository = repository;
        this.producer = producer;
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

                    producer.enviarUsuarioRegistrado(usuarioGuardado.getId(), auth0Id, email);

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
        if (datos.getTelefono() != null) {
            log.debug("Actualizando telefono - auth0Id: {}", auth0Id);
            u.setTelefono(datos.getTelefono());
            cambiosRealizados = true;
        }

        if (datos.getEstado() != null) {
            log.debug("Actualizando estado - auth0Id: {}, nuevo estado: {}", auth0Id, datos.getEstado());
            u.setEstado(datos.getEstado());
            cambiosRealizados = true;
        }

        if (datos.getRol() != null) {
            log.debug("Actualizando rol - auth0Id: {}, nuevo rol: {}", auth0Id, datos.getRol());
            u.setRol(datos.getRol());
            cambiosRealizados = true;
        }

        if (!cambiosRealizados) {
            log.warn("Actualización solicitada sin cambios - auth0Id: {}", auth0Id);
        }
        if (cambiosRealizados) {
            Usuario guardado = repository.save(u);
            log.info("Usuario actualizado exitosamente - auth0Id: {}", auth0Id);
            producer.enviarPerfilActualizado(guardado.getId(), auth0Id);
            return guardado;
        }

        return u;
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

    @Override
    public Usuario suspenderUsuario(String auth0Id, int dias) {
        log.info("Suspendiendo usuario por {} días - auth0Id: {}", dias, auth0Id);

        Usuario u = obtenerPorUserId(auth0Id);

        java.time.LocalDateTime fechaSuspension = java.time.LocalDateTime.now().plusDays(dias);
        u.setFechaSuspension(fechaSuspension);
        u.setEstado("SUSPENDIDO");

        Usuario usuarioSuspendido = repository.save(u);

        log.info("Usuario suspendido exitosamente - id: {}, auth0Id: {}, hasta: {}", 
                 usuarioSuspendido.getId(), auth0Id, fechaSuspension);

        return usuarioSuspendido;
    }

    @Override
    public Usuario activarUsuario(String auth0Id) {
        log.info("Activando usuario - auth0Id: {}", auth0Id);

        Usuario u = obtenerPorUserId(auth0Id);

        u.setEstado("ACTIVO");
        u.setFechaSuspension(null);

        Usuario usuarioActivado = repository.save(u);

        log.info("Usuario activado exitosamente - id: {}, auth0Id: {}", usuarioActivado.getId(), auth0Id);

        return usuarioActivado;
    }

    @Override
    public Usuario desactivarUsuario(String auth0Id) {
        log.info("Desactivando usuario - auth0Id: {}", auth0Id);

        Usuario u = obtenerPorUserId(auth0Id);

        u.setEstado("INACTIVO");
        u.setFechaSuspension(null);

        Usuario usuarioDesactivado = repository.save(u);

        log.info("Usuario desactivado exitosamente - id: {}, auth0Id: {}", usuarioDesactivado.getId(), auth0Id);

        return usuarioDesactivado;
    }

    @Override
    public Usuario crearVendedorOChofer(String nombre, String email, String rol, String documentoIdentidad) {
        log.info("Creando nuevo {} - email: {}", rol, email);

        // Verificar que el email no exista
        if (repository.findByEmail(email).isPresent()) {
            log.warn("Email ya existe - email: {}", email);
            throw new BusinessException("EMAIL_EXISTS", "El email ya está registrado");
        }

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setEmail(email);
        u.setEstado("ACTIVO");
        u.setAuth0Id("auth0|" + System.currentTimeMillis() + "_" + email);

        // Asignar rol
        if ("CHOFER".equalsIgnoreCase(rol)) {
            u.setRol(com.smartlogix.usuarios.model.Rol.CHOFER);
        } else if ("VENDEDOR".equalsIgnoreCase(rol)) {
            u.setRol(com.smartlogix.usuarios.model.Rol.VENDEDOR);
        } else {
            u.setRol(com.smartlogix.usuarios.model.Rol.CLIENTE);
        }

        Usuario usuarioCreado = repository.save(u);

        log.info("Usuario {} creado exitosamente - id: {}, email: {}", 
                 rol, usuarioCreado.getId(), email);

        return usuarioCreado;
    }

    @Override
    public Usuario cambiarRol(String id, String nuevoRol) {
        log.info("Cambiando rol del usuario {} a {}", id, nuevoRol);
        
        Usuario usuario = obtenerPorUserId(id);
        
        try {
            com.smartlogix.usuarios.model.Rol enumRol = com.smartlogix.usuarios.model.Rol.valueOf(nuevoRol.toUpperCase());
            usuario.setRol(enumRol);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("INVALID_ROLE", "Rol no válido: " + nuevoRol);
        }
        
        return repository.save(usuario);
    }
}