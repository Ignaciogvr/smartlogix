package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.DashboardResponse;
import com.smartlogix.bff.dto.response.HomeResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.service.CatalogoService;
import com.smartlogix.bff.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final CatalogoService catalogoService;

    public DashboardController(
            DashboardService dashboardService,
            CatalogoService catalogoService
    ) {
        this.dashboardService = dashboardService;
        this.catalogoService = catalogoService;
    }

    // =========================
    // PÚBLICO (home / vitrina)
    // =========================

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> home() {

        List<ProductoCatalogoDTO> destacados =
                catalogoService.productosDestacados();

        List<ProductoCatalogoDTO> activos =
                catalogoService.listarProductosActivos();

        HomeResponse body = new HomeResponse();
        body.setDestacados(destacados);
        body.setOfertas(destacados);
        body.setRecientes(
                activos.stream().limit(12).toList()
        );
        body.setTotalProductos(activos.size());
        body.setTotalCategorias(
                (int) activos.stream()
                        .map(ProductoCatalogoDTO::getCategoria)
                        .filter(c -> c != null && !c.isBlank())
                        .distinct()
                        .count()
        );

        return ResponseEntity.ok(body);
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoCatalogoDTO>> destacadosDashboard() {

        return ResponseEntity.ok(
                catalogoService.productosDestacados()
        );
    }

    // =========================
    // ADMIN
    // =========================

    @GetMapping("/admin")
    public ResponseEntity<DashboardResponse> admin() {

        return ResponseEntity.ok(
                dashboardService.dashboardAdmin()
        );
    }

    // =========================
    // USUARIO
    // =========================

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<DashboardResponse> usuario(
            @PathVariable String usuarioId
    ) {

        return ResponseEntity.ok(
                dashboardService.dashboardUsuario(usuarioId)
        );
    }

    // =========================
    // VENTAS
    // =========================

    @GetMapping("/ventas")
    public ResponseEntity<DashboardResponse> ventas() {

        return ResponseEntity.ok(
                dashboardService.dashboardVentas()
        );
    }

    // =========================
    // LOGISTICA
    // =========================

    @GetMapping("/logistica")
    public ResponseEntity<DashboardResponse> logistica() {

        return ResponseEntity.ok(
                dashboardService.dashboardLogistica()
        );
    }
}