package com.smartlogix.pedidos.service.impl;

import com.smartlogix.pedidos.dto.CarritoItemRequest;
import com.smartlogix.pedidos.dto.CarritoResponse;
import com.smartlogix.pedidos.model.Carrito;
import com.smartlogix.pedidos.model.CarritoItem;
import com.smartlogix.pedidos.model.EstadoCarrito;
import com.smartlogix.pedidos.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

    private Carrito testCarrito;
    private CarritoItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        testCarrito = new Carrito();
        testCarrito.setId(1L);
        testCarrito.setUsuarioId("user123");
        testCarrito.setEstado(EstadoCarrito.ACTIVO);
        testCarrito.setTotal(0.0);
        testCarrito.setItems(new ArrayList<>());

        itemRequest = new CarritoItemRequest();
        itemRequest.setProductoId(1L);
        itemRequest.setCantidad(2);
        itemRequest.setPrecio(50.0);
    }

    @Test
    void testObtenerCarrito_CarritoExistente() {
        when(carritoRepository.findByUsuarioIdAndEstado("user123", EstadoCarrito.ACTIVO))
            .thenReturn(Optional.of(testCarrito));

        CarritoResponse result = carritoService.obtenerCarrito("user123");

        assertNotNull(result);
        assertEquals("user123", result.getUsuarioId());
        assertEquals("ACTIVO", result.getEstado());
        verify(carritoRepository, times(1)).findByUsuarioIdAndEstado("user123", EstadoCarrito.ACTIVO);
    }

    @Test
    void testObtenerCarrito_CrearNuevoCarrito() {
        when(carritoRepository.findByUsuarioIdAndEstado("user456", EstadoCarrito.ACTIVO))
            .thenReturn(Optional.empty());
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponse result = carritoService.obtenerCarrito("user456");

        assertNotNull(result);
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void testAgregarItem_NuevoItem() {
        when(carritoRepository.findByUsuarioIdAndEstado(anyString(), eq(EstadoCarrito.ACTIVO)))
            .thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponse result = carritoService.agregarItem("user123", itemRequest);

        assertNotNull(result);
        assertEquals(1, testCarrito.getItems().size());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void testAgregarItem_ItemExistente_IncrementarCantidad() {
        CarritoItem existingItem = new CarritoItem();
        existingItem.setProductoId(1L);
        existingItem.setCantidad(3);
        existingItem.setPrecio(50.0);
        existingItem.setCarrito(testCarrito);
        testCarrito.getItems().add(existingItem);

        when(carritoRepository.findByUsuarioIdAndEstado(anyString(), eq(EstadoCarrito.ACTIVO)))
            .thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponse result = carritoService.agregarItem("user123", itemRequest);

        assertNotNull(result);
        assertEquals(5, existingItem.getCantidad());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void testActualizarItem_Exitoso() {
        CarritoItem item = new CarritoItem();
        item.setProductoId(1L);
        item.setCantidad(2);
        item.setPrecio(50.0);
        item.setCarrito(testCarrito);
        testCarrito.getItems().add(item);

        when(carritoRepository.findByUsuarioIdAndEstado(anyString(), eq(EstadoCarrito.ACTIVO)))
            .thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponse result = carritoService.actualizarItem("user123", 1L, 5);

        assertNotNull(result);
        assertEquals(5, item.getCantidad());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void testActualizarItem_CantidadCero_RemoverItem() {
        CarritoItem item = new CarritoItem();
        item.setProductoId(1L);
        item.setCantidad(2);
        item.setPrecio(50.0);
        item.setCarrito(testCarrito);
        testCarrito.getItems().add(item);

        when(carritoRepository.findByUsuarioIdAndEstado(anyString(), eq(EstadoCarrito.ACTIVO)))
            .thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponse result = carritoService.actualizarItem("user123", 1L, 0);

        assertNotNull(result);
        assertEquals(0, testCarrito.getItems().size());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void testRemoverItem_Exitoso() {
        CarritoItem item = new CarritoItem();
        item.setProductoId(1L);
        item.setCantidad(2);
        item.setPrecio(50.0);
        item.setCarrito(testCarrito);
        testCarrito.getItems().add(item);

        when(carritoRepository.findByUsuarioIdAndEstado(anyString(), eq(EstadoCarrito.ACTIVO)))
            .thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponse result = carritoService.removerItem("user123", 1L);

        assertNotNull(result);
        assertEquals(0, testCarrito.getItems().size());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }

    @Test
    void testVaciarCarrito() {
        CarritoItem item1 = new CarritoItem();
        item1.setProductoId(1L);
        item1.setCantidad(2);
        item1.setPrecio(50.0);
        item1.setCarrito(testCarrito);

        CarritoItem item2 = new CarritoItem();
        item2.setProductoId(2L);
        item2.setCantidad(1);
        item2.setPrecio(100.0);
        item2.setCarrito(testCarrito);

        testCarrito.getItems().add(item1);
        testCarrito.getItems().add(item2);

        when(carritoRepository.findByUsuarioIdAndEstado(anyString(), eq(EstadoCarrito.ACTIVO)))
            .thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        carritoService.vaciarCarrito("user123");

        assertEquals(0, testCarrito.getItems().size());
        assertEquals(0.0, testCarrito.getTotal());
        verify(carritoRepository, times(1)).save(any(Carrito.class));
    }
}
