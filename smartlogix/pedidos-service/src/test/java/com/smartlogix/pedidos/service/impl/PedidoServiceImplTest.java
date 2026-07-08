package com.smartlogix.pedidos.service.impl;

import com.smartlogix.pedidos.client.InventoryClient;
import com.smartlogix.pedidos.client.UserClient;
import com.smartlogix.pedidos.dto.DetallePedidoDTO;
import com.smartlogix.pedidos.dto.PedidoRequestDTO;
import com.smartlogix.pedidos.event.PedidoEstadoObserver;
import com.smartlogix.pedidos.kafka.producer.KafkaProducerService;
import com.smartlogix.pedidos.model.DetallePedido;
import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.repository.PedidoHistorialRepository;
import com.smartlogix.pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private UserClient userClient;

    @Mock
    private KafkaProducerService producer;

    @Mock
    private PedidoEstadoObserver observer;

    @Mock
    private PedidoHistorialRepository historialRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private PedidoRequestDTO pedidoRequest;
    private Pedido testPedido;
    private DetallePedidoDTO detalleDTO;

    @BeforeEach
    void setUp() {
        detalleDTO = new DetallePedidoDTO();
        detalleDTO.setProductoId(1L);
        detalleDTO.setCantidad(2);
        detalleDTO.setPrecioUnitario(100.0);
        detalleDTO.setVendedorId("vendor1");

        pedidoRequest = new PedidoRequestDTO();
        pedidoRequest.setUsuarioId("user123");
        pedidoRequest.setProductos(List.of(detalleDTO));
        pedidoRequest.setDireccionDestino("Calle 123");

        testPedido = new Pedido();
        // Set private id via reflection for test purposes
        try {
            java.lang.reflect.Field idField = Pedido.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testPedido, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        testPedido.setUsuarioId("user123");
        testPedido.setTotal(200.0);
        testPedido.setEstado(EstadoPedido.PENDIENTE);
        
        DetallePedido detalle = new DetallePedido();
        detalle.setProductoId(1L);
        detalle.setCantidad(2);
        detalle.setPrecio(100.0);
        detalle.setVendedorId("vendor1");
        detalle.setPedido(testPedido);
        
        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(detalle);
        testPedido.setDetalles(detalles);
    }

    @Test
    void testCrearDesdeRequest_Exitoso() {
        // Arrange: Create a saved pedido with ID
        Pedido savedPedido = new Pedido();
        savedPedido.setUsuarioId("user123");
        savedPedido.setTotal(200.0);
        savedPedido.setEstado(EstadoPedido.PENDIENTE);
        savedPedido.setDireccionEnvio("Calle 123");
        
        // Set ID via reflection
        try {
            java.lang.reflect.Field idField = Pedido.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(savedPedido, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        DetallePedido detalle = new DetallePedido();
        detalle.setProductoId(1L);
        detalle.setCantidad(2);
        detalle.setPrecio(100.0);
        detalle.setVendedorId("vendor1");
        detalle.setPedido(savedPedido);
        
        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(detalle);
        savedPedido.setDetalles(detalles);
        
        when(inventoryClient.obtenerStock(anyLong())).thenReturn(10);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(savedPedido);
        when(historialRepository.save(any())).thenReturn(null);

        // Act
        Pedido result = pedidoService.crearDesdeRequest(pedidoRequest, "Bearer token");

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.getUsuarioId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(observer, times(1)).onPedidoCreado(any(), anyString(), anyList());
    }

    @Test
    void testCrearDesdeRequest_UsuarioNulo() {
        pedidoRequest.setUsuarioId(null);

        assertThrows(IllegalArgumentException.class, 
            () -> pedidoService.crearDesdeRequest(pedidoRequest, "Bearer token"));
        
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testCrearDesdeRequest_UsuarioVacio() {
        pedidoRequest.setUsuarioId("");

        assertThrows(IllegalArgumentException.class, 
            () -> pedidoService.crearDesdeRequest(pedidoRequest, "Bearer token"));
    }

    @Test
    void testCrearDesdeRequest_ProductosNulo() {
        pedidoRequest.setProductos(null);

        assertThrows(IllegalArgumentException.class, 
            () -> pedidoService.crearDesdeRequest(pedidoRequest, "Bearer token"));
    }

    @Test
    void testCrearDesdeRequest_ProductosVacio() {
        pedidoRequest.setProductos(List.of());

        assertThrows(IllegalArgumentException.class, 
            () -> pedidoService.crearDesdeRequest(pedidoRequest, "Bearer token"));
    }

    @Test
    void testCrearDesdeRequest_ProductoIdNulo() {
        detalleDTO.setProductoId(null);

        assertThrows(IllegalArgumentException.class, 
            () -> pedidoService.crearDesdeRequest(pedidoRequest, "Bearer token"));
    }

    @Test
    void testCrearDesdeRequest_CantidadInvalida() {
        detalleDTO.setCantidad(0);

        assertThrows(IllegalArgumentException.class, 
            () -> pedidoService.crearDesdeRequest(pedidoRequest, "Bearer token"));
    }

    @Test
    void testPorUsuario() {
        when(pedidoRepository.findByUsuarioId("user123")).thenReturn(List.of(testPedido));

        List<Pedido> result = pedidoService.porUsuario("user123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user123", result.get(0).getUsuarioId());
    }

    @Test
    void testPagar_Exitoso() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(testPedido);
        when(historialRepository.save(any())).thenReturn(null);
        doNothing().when(inventoryClient).descontarStock(anyLong(), anyInt(), anyString());

        Pedido result = pedidoService.pagar(1L);

        assertNotNull(result);
        assertEquals(EstadoPedido.PAGADO, result.getEstado());
        verify(inventoryClient, times(1)).descontarStock(anyLong(), anyInt(), anyString());
        verify(observer, times(1)).onEstadoCambiado(any());
    }

    @Test
    void testPagar_EstadoInvalido() {
        testPedido.setEstado(EstadoPedido.PAGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));

        assertThrows(IllegalStateException.class, () -> pedidoService.pagar(1L));
        
        verify(inventoryClient, never()).descontarStock(anyLong(), anyInt(), anyString());
    }

    @Test
    void testCancelar_DesdePendiente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(testPedido);
        when(historialRepository.save(any())).thenReturn(null);

        Pedido result = pedidoService.cancelar(1L);

        assertNotNull(result);
        assertEquals(EstadoPedido.CANCELADO, result.getEstado());
        verify(inventoryClient, never()).reponerStock(anyLong(), anyInt());
        verify(observer, times(1)).onPedidoCancelado(any());
    }

    @Test
    void testCancelar_DesdePagado_ReponerStock() {
        testPedido.setEstado(EstadoPedido.PAGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(testPedido);
        when(historialRepository.save(any())).thenReturn(null);
        doNothing().when(inventoryClient).reponerStock(anyLong(), anyInt());

        Pedido result = pedidoService.cancelar(1L);

        assertNotNull(result);
        assertEquals(EstadoPedido.CANCELADO, result.getEstado());
        verify(inventoryClient, times(1)).reponerStock(anyLong(), anyInt());
    }

    @Test
    void testCancelar_PedidoEntregado() {
        testPedido.setEstado(EstadoPedido.ENTREGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));

        assertThrows(IllegalStateException.class, () -> pedidoService.cancelar(1L));
        
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testReactivar_Exitoso() {
        testPedido.setEstado(EstadoPedido.CANCELADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(testPedido);
        when(historialRepository.save(any())).thenReturn(null);

        Pedido result = pedidoService.reactivar(1L);

        assertNotNull(result);
        assertEquals(EstadoPedido.PENDIENTE, result.getEstado());
        verify(observer, times(1)).onEstadoCambiado(any());
    }

    @Test
    void testReactivar_EstadoInvalido() {
        testPedido.setEstado(EstadoPedido.PAGADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));

        assertThrows(IllegalStateException.class, () -> pedidoService.reactivar(1L));
        
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testActualizarDesdeRequest_Exitoso() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));
        when(inventoryClient.obtenerStock(anyLong())).thenReturn(10);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(testPedido);

        Pedido result = pedidoService.actualizarDesdeRequest(1L, pedidoRequest);

        assertNotNull(result);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testActualizarDesdeRequest_EstadoNoPermitido() {
        testPedido.setEstado(EstadoPedido.ENVIADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));

        assertThrows(IllegalStateException.class, 
            () -> pedidoService.actualizarDesdeRequest(1L, pedidoRequest));
    }

    @Test
    void testEliminar() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));
        // No need to mock KafkaTemplate here; the service may use producer which is mocked elsewhere if needed

        pedidoService.eliminar(1L);

        verify(pedidoRepository, times(1)).delete(any(Pedido.class));
    }
}
