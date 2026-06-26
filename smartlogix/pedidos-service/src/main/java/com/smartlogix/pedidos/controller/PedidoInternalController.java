package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.service.PedidoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/pedidos")
public class PedidoInternalController {

    private final PedidoService service;

    public PedidoInternalController(PedidoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Pedido obtenerInterno(@PathVariable Long id) {
        return service.obtener(id);
    }
}