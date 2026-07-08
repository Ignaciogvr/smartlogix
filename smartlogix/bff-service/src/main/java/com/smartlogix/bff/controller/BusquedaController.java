package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.ApiResponse;
import com.smartlogix.bff.service.BusquedaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/busqueda")
public class BusquedaController {

    private final BusquedaService busquedaService;

    public BusquedaController(BusquedaService busquedaService) {
        this.busquedaService = busquedaService;
    }

    /**
     * GET /busqueda?q=texto
     * Búsqueda de productos por texto libre.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> buscar(@RequestParam(name = "q", required = false) String q) {
        return ResponseEntity.ok(
                new ApiResponse<>("ok", "Resultados de búsqueda", busquedaService.buscarProductos(q))
        );
    }

    /**
     * GET /busqueda/filtrar?precioMin=&precioMax=&marca=&ratingMin=
     * Filtrado avanzado de productos.
     */
    @GetMapping("/filtrar")
    public ResponseEntity<ApiResponse<?>> filtrar(
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Double ratingMin
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>("ok", "Productos filtrados", busquedaService.filtrarProductos(precioMin, precioMax, marca, ratingMin))
        );
    }
}
