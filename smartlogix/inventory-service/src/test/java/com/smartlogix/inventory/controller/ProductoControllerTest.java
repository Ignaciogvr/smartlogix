package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.dto.ProductoResponse;
import com.smartlogix.inventory.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@TestPropertySource(properties = {
    "AUTH0_DOMAIN=test.auth0.com",
    "AUTH0_AUDIENCE=test-audience",
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://test.auth0.com/"
})
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    private ProductoResponse testProducto;

    @BeforeEach
    void setUp() {
        testProducto = new ProductoResponse(
            1L, "Laptop Test", "Desc", 1000.0, 10, "Electronica",
            "img.jpg", null, 4.5, 100, false, false, true,
            "Corta", 1200.0, 10, "Marca", "Modelo", "Fabricante", "SKU", "1 año",
            "1kg", "10x10", "Plastico", "Negro", "China", 100, "Vendedor1"
        );
    }

    @Test
    void testListar() throws Exception {
        List<ProductoResponse> productos = Arrays.asList(testProducto);
        when(productoService.listar()).thenReturn(productos);

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isArray());

        verify(productoService, times(1)).listar();
    }

    @Test
    void testActivos() throws Exception {
        when(productoService.activos()).thenReturn(Arrays.asList(testProducto));

        mockMvc.perform(get("/productos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).activos();
    }

    @Test
    void testObtener() throws Exception {
        when(productoService.obtener(1L)).thenReturn(testProducto);

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(productoService, times(1)).obtener(1L);
    }

    @Test
    void testPorCategoria() throws Exception {
        when(productoService.porCategoria("Electronica")).thenReturn(Arrays.asList(testProducto));

        mockMvc.perform(get("/productos/categoria/Electronica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).porCategoria("Electronica");
    }

    @Test
    void testOfertas() throws Exception {
        when(productoService.ofertas()).thenReturn(Arrays.asList(testProducto));

        mockMvc.perform(get("/productos/ofertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).ofertas();
    }

    @Test
    void testNuevos() throws Exception {
        when(productoService.nuevos()).thenReturn(Arrays.asList(testProducto));

        mockMvc.perform(get("/productos/nuevos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).nuevos();
    }

    @Test
    void testBuscarPorTexto() throws Exception {
        when(productoService.buscarPorTexto("laptop")).thenReturn(Arrays.asList(testProducto));

        mockMvc.perform(get("/productos/buscar").param("q", "laptop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).buscarPorTexto("laptop");
    }

    @Test
    void testFiltrar() throws Exception {
        when(productoService.filtrar(anyDouble(), anyDouble(), anyString(), anyDouble()))
                .thenReturn(Arrays.asList(testProducto));

        mockMvc.perform(get("/productos/filtrar")
                        .param("precioMin", "100")
                        .param("precioMax", "2000")
                        .param("marca", "HP")
                        .param("ratingMin", "4.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).filtrar(anyDouble(), anyDouble(), anyString(), anyDouble());
    }

    @Test
    void testDestacados() throws Exception {
        when(productoService.destacados()).thenReturn(Arrays.asList(testProducto));

        mockMvc.perform(get("/productos/destacados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).destacados();
    }

    @Test
    void testStock() throws Exception {
        when(productoService.stock(1L)).thenReturn(10);

        mockMvc.perform(get("/productos/stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value(10));

        verify(productoService, times(1)).stock(1L);
    }

    @Test
    void testReponerStock() throws Exception {
        doNothing().when(productoService).reponerStock(1L, 5);

        mockMvc.perform(put("/productos/stock/1/reponer").param("cantidad", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).reponerStock(1L, 5);
    }

    @Test
    void testDescontarStock() throws Exception {
        doNothing().when(productoService).descontarStock(1L, 2, "user123");

        mockMvc.perform(put("/productos/stock/1/descontar")
                        .param("cantidad", "2")
                        .param("usuarioId", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        verify(productoService, times(1)).descontarStock(1L, 2, "user123");
    }
}
