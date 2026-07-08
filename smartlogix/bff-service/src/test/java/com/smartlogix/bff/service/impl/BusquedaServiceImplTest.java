package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusquedaServiceImplTest {

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private BusquedaServiceImpl busquedaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarProductos_deberiaRetornarListaDeProductos() {
        // Arrange
        String query = "Laptop";
        ProductoCatalogoDTO p = new ProductoCatalogoDTO();
        p.setNombre("Laptop X");

        ServiceEnvelope<List<ProductoCatalogoDTO>> env = new ServiceEnvelope<>();
        env.setData(List.of(p));

        when(inventoryClient.buscarProductos(query)).thenReturn(env);

        // Act
        List<ProductoCatalogoDTO> result = busquedaService.buscarProductos(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Laptop X", result.get(0).getNombre());
        verify(inventoryClient, times(1)).buscarProductos(query);
    }
}
