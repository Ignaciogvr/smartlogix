package com.smartlogix.usuarios.service.impl;

import com.smartlogix.usuarios.exception.BusinessException;
import com.smartlogix.usuarios.model.Usuario;
import com.smartlogix.usuarios.model.Rol;
import com.smartlogix.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.smartlogix.usuarios.kafka.producer.KafkaProducerService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsuarioServiceImplTest {

    @Mock
    private KafkaProducerService producer;

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario testUsuario;

    @BeforeEach
    void setUp() {
        testUsuario = new Usuario();
        testUsuario.setAuth0Id("auth0|123");
        testUsuario.setEmail("test@test.com");
        testUsuario.setNombre("Test User");
        testUsuario.setEstado("ACTIVO");
        testUsuario.setRol(Rol.CLIENTE);
    }

    @Test
    void testCrearDesdeToken_UsuarioNoExiste() {
        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.empty());
        when(repository.save(any(Usuario.class))).thenReturn(testUsuario);

        Usuario result = usuarioService.crearDesdeToken("auth0|123", "test@test.com", "Test User");

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCrearDesdeToken_UsuarioExiste() {
        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(testUsuario));

        Usuario result = usuarioService.crearDesdeToken("auth0|123", "test@test.com", "Test User");

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        verify(repository, never()).save(any(Usuario.class));
    }

    @Test
    void testListar() {
        List<Usuario> usuarios = Arrays.asList(testUsuario);
        when(repository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listar();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testObtenerPorUserId_Existe() {
        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(testUsuario));

        Usuario result = usuarioService.obtenerPorUserId("auth0|123");

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void testObtenerPorUserId_NoExiste() {
        when(repository.findByAuth0Id("auth0|999")).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> usuarioService.obtenerPorUserId("auth0|999"));
    }

    @Test
    void testActualizarPorUserId() {
        Usuario datosActualizados = new Usuario();
        datosActualizados.setNombre("Nuevo Nombre");
        datosActualizados.setEmail("nuevo@test.com");

        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(testUsuario));
        when(repository.save(any(Usuario.class))).thenReturn(testUsuario);

        Usuario result = usuarioService.actualizarPorUserId("auth0|123", datosActualizados);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testActualizarPorUserId_SinCambios() {
        Usuario datosActualizados = new Usuario();

        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(testUsuario));
        when(repository.save(any(Usuario.class))).thenReturn(testUsuario);

        Usuario result = usuarioService.actualizarPorUserId("auth0|123", datosActualizados);

        assertNotNull(result);
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testActualizarPorUserId_ActualizarEstado() {
        Usuario datosActualizados = new Usuario();
        datosActualizados.setEstado("INACTIVO");

        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(testUsuario));
        when(repository.save(any(Usuario.class))).thenReturn(testUsuario);

        Usuario result = usuarioService.actualizarPorUserId("auth0|123", datosActualizados);

        assertNotNull(result);
        assertEquals("INACTIVO", testUsuario.getEstado());
    }

    @Test
    void testActualizarPorUserId_ActualizarRol() {
        Usuario datosActualizados = new Usuario();
        datosActualizados.setRol(Rol.ADMIN);

        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(testUsuario));
        when(repository.save(any(Usuario.class))).thenReturn(testUsuario);

        Usuario result = usuarioService.actualizarPorUserId("auth0|123", datosActualizados);

        assertNotNull(result);
        assertEquals(Rol.ADMIN, testUsuario.getRol());
    }

    @Test
    void testEliminarPorUserId() {
        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(testUsuario));
        when(repository.save(any(Usuario.class))).thenReturn(testUsuario);

        usuarioService.eliminarPorUserId("auth0|123");

        assertEquals("INACTIVO", testUsuario.getEstado());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testExistePorAuth0Id_Existe() {
        when(repository.findByAuth0Id("auth0|123")).thenReturn(Optional.of(testUsuario));

        boolean result = usuarioService.existePorAuth0Id("auth0|123");

        assertTrue(result);
    }

    @Test
    void testExistePorAuth0Id_NoExiste() {
        when(repository.findByAuth0Id("auth0|999")).thenReturn(Optional.empty());

        boolean result = usuarioService.existePorAuth0Id("auth0|999");

        assertFalse(result);
    }
}
