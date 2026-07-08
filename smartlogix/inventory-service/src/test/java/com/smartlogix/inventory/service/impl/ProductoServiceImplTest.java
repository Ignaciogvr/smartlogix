package com.smartlogix.inventory.service.impl;

import com.smartlogix.inventory.dto.ProductoCreateRequest;
import com.smartlogix.inventory.dto.ProductoResponse;
import com.smartlogix.inventory.dto.ProductoUpdateRequest;
import com.smartlogix.inventory.model.EstadoProducto;
import com.smartlogix.inventory.model.Producto;
import com.smartlogix.inventory.model.ProductoComentario;
import com.smartlogix.inventory.kafka.producer.KafkaProducer;
import com.smartlogix.inventory.repository.ProductoRepository;
import com.smartlogix.inventory.repository.ProductoComentarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository repo;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private ProductoComentarioRepository comentarioRepo;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto testProducto;
    private ProductoCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        testProducto = new Producto();
        testProducto.setNombre("Laptop Test");
        testProducto.setDescripcion("Laptop de prueba");
        testProducto.setPrecio(1000.0);
        testProducto.setStock(10);
        testProducto.setCategoria("Electrónica");
        testProducto.setEstado(EstadoProducto.ACTIVO);

        createRequest = new ProductoCreateRequest();
        createRequest.setNombre("Nuevo Producto");
        createRequest.setDescripcion("Descripción del producto");
        createRequest.setPrecio(500.0);
        createRequest.setStock(20);
        createRequest.setCategoria("Electrónica");
    }

    @Test
    void testCrear_Exitoso() {
        when(repo.findByNombre(anyString())).thenReturn(Optional.empty());
        when(repo.save(any(Producto.class))).thenReturn(testProducto);

        ProductoResponse result = productoService.crear(createRequest);

        assertNotNull(result);
        verify(repo, times(1)).save(any(Producto.class));
    }

    @Test
    void testCrear_ProductoYaExiste() {
        when(repo.findByNombre(anyString())).thenReturn(Optional.of(testProducto));

        assertThrows(RuntimeException.class, () -> productoService.crear(createRequest));
        verify(repo, never()).save(any(Producto.class));
    }

    @Test
    void testCrear_NombreNulo() {
        createRequest.setNombre(null);

        assertThrows(RuntimeException.class, () -> productoService.crear(createRequest));
    }

    @Test
    void testCrear_PrecioInvalido() {
        createRequest.setPrecio(-1.0);

        assertThrows(RuntimeException.class, () -> productoService.crear(createRequest));
    }

    @Test
    void testCrear_StockInvalido() {
        createRequest.setStock(-5);

        assertThrows(RuntimeException.class, () -> productoService.crear(createRequest));
    }

    @Test
    void testObtener_Exitoso() {
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));

        ProductoResponse result = productoService.obtener(1L);

        assertNotNull(result);
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testObtener_NoEncontrado() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productoService.obtener(999L));
    }

    @Test
    void testListar() {
        when(repo.findByEstado(EstadoProducto.ACTIVO)).thenReturn(Arrays.asList(testProducto));

        List<ProductoResponse> result = productoService.listar();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testActualizar_Exitoso() {
        ProductoUpdateRequest updateRequest = new ProductoUpdateRequest();
        updateRequest.setNombre("Laptop Actualizada");
        updateRequest.setPrecio(1200.0);

        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));
        when(repo.save(any(Producto.class))).thenReturn(testProducto);

        ProductoResponse result = productoService.actualizar(1L, updateRequest);

        assertNotNull(result);
        verify(repo, times(1)).save(any(Producto.class));
    }

    @Test
    void testEliminar() {
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));
        when(repo.save(any(Producto.class))).thenReturn(testProducto);

        productoService.eliminar(1L);

        assertEquals(EstadoProducto.INACTIVO, testProducto.getEstado());
        verify(repo, times(1)).save(any(Producto.class));
    }

    @Test
    void testReactivar() {
        testProducto.setEstado(EstadoProducto.INACTIVO);
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));
        when(repo.save(any(Producto.class))).thenReturn(testProducto);

        ProductoResponse result = productoService.reactivar(1L);

        assertNotNull(result);
        assertEquals(EstadoProducto.ACTIVO, testProducto.getEstado());
    }

    @Test
    void testValidarProducto_Exitoso() {
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));

        ProductoResponse result = productoService.validarProducto(1L, 5);

        assertNotNull(result);
    }

    @Test
    void testValidarProducto_ProductoInactivo() {
        testProducto.setEstado(EstadoProducto.INACTIVO);
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));

        assertThrows(RuntimeException.class, () -> productoService.validarProducto(1L, 5));
    }

    @Test
    void testValidarProducto_StockInsuficiente() {
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));

        assertThrows(RuntimeException.class, () -> productoService.validarProducto(1L, 100));
    }

    @Test
    void testValidarProducto_CantidadInvalida() {
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));

        assertThrows(RuntimeException.class, () -> productoService.validarProducto(1L, 0));
    }

    @Test
    void testDescontarStock_Exitoso() {
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));
        when(repo.save(any(Producto.class))).thenReturn(testProducto);

        productoService.descontarStock(1L, 5, "user123");

        assertEquals(5, testProducto.getStock());
        verify(repo, times(1)).save(any(Producto.class));
    }

    @Test
    void testReponerStock_Exitoso() {
        testProducto.setStock(5);
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));
        when(repo.save(any(Producto.class))).thenReturn(testProducto);

        productoService.reponerStock(1L, 10);

        assertEquals(15, testProducto.getStock());
        verify(repo, times(1)).save(any(Producto.class));
    }

    @Test
    void testAgregarRating_Exitoso() {
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));
        when(repo.save(any(Producto.class))).thenReturn(testProducto);

        productoService.agregarRating(1L, 4.5);

        verify(repo, times(1)).save(any(Producto.class));
    }

    @Test
    void testAgregarRating_RatingInvalido() {
        assertThrows(RuntimeException.class, () -> productoService.agregarRating(1L, 6.0));
        assertThrows(RuntimeException.class, () -> productoService.agregarRating(1L, 0.5));
    }

    @Test
    void testBajoStock() {
        when(repo.findByStockLessThan(5)).thenReturn(Arrays.asList(testProducto));

        List<ProductoResponse> result = productoService.bajoStock();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testStock() {
        when(repo.findById(1L)).thenReturn(Optional.of(testProducto));

        Integer stock = productoService.stock(1L);

        assertEquals(10, stock);
    }

    @Test
    void testDestacados() {
        // Simular producto con ventas usando descontarStock
        testProducto.setStock(150);
        testProducto.descontarStock(100); // Esto incrementa cantidadVendidos
        when(repo.findAll()).thenReturn(Arrays.asList(testProducto));

        List<ProductoResponse> result = productoService.destacados();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
