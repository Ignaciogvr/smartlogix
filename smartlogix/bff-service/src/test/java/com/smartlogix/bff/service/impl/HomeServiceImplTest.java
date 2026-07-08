package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.response.HomeResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HomeServiceImplTest {

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private HomeServiceImpl homeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerHome_deberiaRetornarHomeResponseCompleto() {
        // Arrange
        ProductoCatalogoDTO p1 = new ProductoCatalogoDTO();
        p1.setCategoria("Laptops");
        ProductoCatalogoDTO p2 = new ProductoCatalogoDTO();
        p2.setCategoria("Monitores");

        ServiceEnvelope<List<ProductoCatalogoDTO>> env = new ServiceEnvelope<>();
        env.setData(List.of(p1, p2));

        when(inventoryClient.productosDestacados()).thenReturn(env);
        when(inventoryClient.productosOfertas()).thenReturn(env);
        when(inventoryClient.productosNuevos()).thenReturn(env);
        when(inventoryClient.productosActivos()).thenReturn(env);

        // Act
        HomeResponse response = homeService.obtenerHome();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getDestacados().size());
        assertEquals(2, response.getTotalProductos());
        assertEquals(2, response.getTotalCategorias());
        verify(inventoryClient, times(1)).productosDestacados();
    }
}
