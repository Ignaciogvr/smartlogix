package com.smartlogix.bff.controller;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.response.ApiResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comparador")
public class ComparadorController {

    private final InventoryClient inventoryClient;

    public ComparadorController(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    /**
     * GET /comparador?ids=1,2,3
     * Retorna la lista de productos para comparación.
     * Stateless: no persiste, solo agrega los datos en paralelo.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> comparar(
            @RequestParam(name = "ids") String ids
    ) {
        List<Long> productoIds = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<ProductoCatalogoDTO> productos = productoIds.stream()
                .map(id -> {
                    try {
                        ServiceEnvelope<ProductoCatalogoDTO> env = inventoryClient.obtenerProducto(id);
                        return env != null ? env.getData() : null;
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>("ok", "Comparación de productos", productos)
        );
    }
}
