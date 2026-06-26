package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.EstadoCompletoResponse;
import com.smartlogix.bff.service.PedidoBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoApiV1Controller {

    private final PedidoBffService pedidoService;

    public PedidoApiV1Controller(PedidoBffService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id}/estado-completo")
    public ResponseEntity<EstadoCompletoResponse> obtenerEstadoCompleto(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pedidoService.obtenerEstadoCompleto(id));
    }
}
