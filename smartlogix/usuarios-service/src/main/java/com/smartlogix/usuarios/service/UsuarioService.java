package com.smartlogix.usuarios.service;

import com.smartlogix.usuarios.model.Usuario;
import java.util.List;

public interface UsuarioService {

    Usuario crearDesdeToken(String auth0Id, String email, String nombre);

    List<Usuario> listar();

    Usuario obtenerPorUserId(String auth0Id);

    Usuario actualizarPorUserId(String auth0Id, Usuario datos);

    void eliminarPorUserId(String auth0Id);

    boolean existePorAuth0Id(String auth0Id);
}