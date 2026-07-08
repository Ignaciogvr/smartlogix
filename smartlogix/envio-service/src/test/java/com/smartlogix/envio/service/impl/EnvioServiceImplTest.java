package com.smartlogix.envio.service.impl;

import com.smartlogix.envio.client.PedidoClient;
import com.smartlogix.envio.client.UsuarioClient;
import com.smartlogix.envio.dto.request.CrearEnvioRequest;
import com.smartlogix.envio.dto.response.EnvioResponse;
import com.smartlogix.envio.kafka.producer.KafkaProducerService;
import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;
import com.smartlogix.envio.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceImplTest {

    @Mock
    private EnvioRepository repo;

    @Mock
    private PedidoClient pedidoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private KafkaProducerService kafka;

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Envio testEnvio;

    @BeforeEach
    void setUp() {
        testEnvio = new Envio();
        testEnvio.setId(1L);
        testEnvio.setPedidoId(100L);
        testEnvio.setUsuarioId("user123");
        testEnvio.setTrackingNumber("TRACK123456");
        testEnvio.setEstado(EstadoEnvio.PENDIENTE);
        testEnvio.setDireccionDestino("Calle 123");
    }

    @Test
    void testObtenerPorId_Exitoso() {
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));

        EnvioResponse result = envioService.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TRACK123456", result.getTrackingId());
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.obtenerPorId(999L));
    }

    @Test
    void testObtenerPorTracking_Exitoso() {
        when(repo.findByTrackingNumber("TRACK123456")).thenReturn(Optional.of(testEnvio));

        EnvioResponse result = envioService.obtenerPorTracking("TRACK123456");

        assertNotNull(result);
        assertEquals("TRACK123456", result.getTrackingId());
    }

    @Test
    void testObtenerPorTracking_NoEncontrado() {
        when(repo.findByTrackingNumber("INVALID")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.obtenerPorTracking("INVALID"));
    }

    @Test
    void testObtenerPorPedido() {
        when(repo.findByPedidoId(100L)).thenReturn(Arrays.asList(testEnvio));

        List<EnvioResponse> result = envioService.obtenerPorPedido(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getPedidoId());
    }

    @Test
    void testListarTodos() {
        when(repo.findAll()).thenReturn(Arrays.asList(testEnvio));

        List<EnvioResponse> result = envioService.listarTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListarPorUsuario() {
        when(repo.findByUsuarioId("user123")).thenReturn(Arrays.asList(testEnvio));

        List<EnvioResponse> result = envioService.listarPorUsuario("user123");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testListarPorChofer() {
        testEnvio.setChoferId("chofer456");
        when(repo.findByChoferId("chofer456")).thenReturn(Arrays.asList(testEnvio));

        List<EnvioResponse> result = envioService.listarPorChofer("chofer456");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testActualizarEstado_PendienteAPreparando() {
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));
        when(repo.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponse result = envioService.actualizarEstado(1L, "PREPARANDO");

        assertNotNull(result);
        assertEquals(EstadoEnvio.PREPARANDO, testEnvio.getEstado());
        verify(repo, times(1)).save(any(Envio.class));
    }

    @Test
    void testActualizarEstado_PendienteAAsignado() {
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));
        when(repo.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponse result = envioService.actualizarEstado(1L, "ASIGNADO");

        assertNotNull(result);
        assertEquals(EstadoEnvio.ASIGNADO, testEnvio.getEstado());
    }

    @Test
    void testActualizarEstado_EnvioNoEncontrado() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.actualizarEstado(999L, "PREPARANDO"));
    }

    @Test
    void testActualizarEstado_TransicionInvalida_EntregadoNoSePuedeCambiar() {
        testEnvio.setEstado(EstadoEnvio.ENTREGADO);
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));

        assertThrows(IllegalStateException.class, () -> envioService.actualizarEstado(1L, "CANCELADO"));
    }

    @Test
    void testActualizarEstado_TransicionInvalida_CanceladoNoSePuedeCambiar() {
        testEnvio.setEstado(EstadoEnvio.CANCELADO);
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));

        assertThrows(IllegalStateException.class, () -> envioService.actualizarEstado(1L, "EN_RUTA"));
    }

    @Test
    void testActualizarEstado_CancelarDesdePreparando() {
        testEnvio.setEstado(EstadoEnvio.PREPARANDO);
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));
        when(repo.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponse result = envioService.actualizarEstado(1L, "CANCELADO");

        assertNotNull(result);
        assertEquals(EstadoEnvio.CANCELADO, testEnvio.getEstado());
    }

    @Test
    void testActualizarEstado_NoPuedeCancelarEnRuta() {
        testEnvio.setEstado(EstadoEnvio.EN_RUTA);
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));

        assertThrows(IllegalStateException.class, () -> envioService.actualizarEstado(1L, "CANCELADO"));
    }

    @Test
    void testAsignarChofer_Exitoso() {
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));
        when(repo.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponse result = envioService.asignarChofer(1L, "chofer123", "Juan Perez");

        assertNotNull(result);
        assertEquals("chofer123", testEnvio.getChoferId());
        assertEquals("Juan Perez", testEnvio.getChoferNombre());
        assertEquals(EstadoEnvio.ASIGNADO, testEnvio.getEstado());
        verify(repo, times(1)).save(any(Envio.class));
    }

    @Test
    void testAsignarChofer_EnvioNoEncontrado() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> envioService.asignarChofer(999L, "chofer123", "Juan Perez"));
    }

    @Test
    void testAsignarChofer_EstadoInvalido() {
        testEnvio.setEstado(EstadoEnvio.EN_RUTA);
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));

        assertThrows(IllegalStateException.class, () -> envioService.asignarChofer(1L, "chofer123", "Juan Perez"));
    }

    @Test
    void testAsignarChofer_ChoferIdVacio() {
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));

        assertThrows(IllegalArgumentException.class, () -> envioService.asignarChofer(1L, "", "Juan Perez"));
    }

    @Test
    void testAsignarChofer_ChoferIdNulo() {
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));

        assertThrows(IllegalArgumentException.class, () -> envioService.asignarChofer(1L, null, "Juan Perez"));
    }

    @Test
    void testCancelarPorPedido_Exitoso() {
        Envio envio1 = new Envio();
        envio1.setEstado(EstadoEnvio.PENDIENTE);
        envio1.setTrackingNumber("TRACK001");

        Envio envio2 = new Envio();
        envio2.setEstado(EstadoEnvio.PREPARANDO);
        envio2.setTrackingNumber("TRACK002");

        when(repo.findByPedidoId(100L)).thenReturn(Arrays.asList(envio1, envio2));
        when(repo.save(any(Envio.class))).thenReturn(envio1);

        envioService.cancelarPorPedido(100L);

        assertEquals(EstadoEnvio.CANCELADO, envio1.getEstado());
        assertEquals(EstadoEnvio.CANCELADO, envio2.getEstado());
        verify(repo, times(2)).save(any(Envio.class));
    }

    @Test
    void testCancelarPorPedido_EnvioEnRutaNoSeCancela() {
        Envio envio1 = new Envio();
        envio1.setEstado(EstadoEnvio.EN_RUTA);
        envio1.setTrackingNumber("TRACK001");

        when(repo.findByPedidoId(100L)).thenReturn(Arrays.asList(envio1));

        envioService.cancelarPorPedido(100L);

        assertEquals(EstadoEnvio.EN_RUTA, envio1.getEstado());
        verify(repo, never()).save(any(Envio.class));
    }

    @Test
    void testActualizarEstado_PreparandoAEnRuta() {
        testEnvio.setEstado(EstadoEnvio.PREPARANDO);
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));
        when(repo.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponse result = envioService.actualizarEstado(1L, "EN_RUTA");

        assertNotNull(result);
        assertEquals(EstadoEnvio.EN_RUTA, testEnvio.getEstado());
    }

    @Test
    void testActualizarEstado_AsignadoAEnRuta() {
        testEnvio.setEstado(EstadoEnvio.ASIGNADO);
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));
        when(repo.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponse result = envioService.actualizarEstado(1L, "EN_RUTA");

        assertNotNull(result);
        assertEquals(EstadoEnvio.EN_RUTA, testEnvio.getEstado());
    }

    @Test
    void testActualizarEstado_EnRutaAEntregado() {
        testEnvio.setEstado(EstadoEnvio.EN_RUTA);
        when(repo.findById(1L)).thenReturn(Optional.of(testEnvio));
        when(repo.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponse result = envioService.actualizarEstado(1L, "ENTREGADO");

        assertNotNull(result);
        assertEquals(EstadoEnvio.ENTREGADO, testEnvio.getEstado());
    }
}
