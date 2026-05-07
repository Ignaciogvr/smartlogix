package com.smartlogix.bff.controller;

import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.dto.CompraRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bff/pedidos")
public class PedidoBffController {

    private final PedidoClient pedidoClient;

    public PedidoBffController(PedidoClient pedidoClient) {
        this.pedidoClient = pedidoClient;
    }

    // 🔥 CREAR PEDIDO
    @PostMapping
    public ResponseEntity<Object> crear(
            @RequestBody CompraRequest request
    ) {

        return ResponseEntity.ok(
                pedidoClient.crearPedido(request)
        );
    }

    // 🔥 LISTAR PEDIDOS
    @GetMapping
    public ResponseEntity<Object> listar() {

        return ResponseEntity.ok(
                pedidoClient.listarPedidos()
        );
    }

    // 🔥 OBTENER PEDIDO
    @GetMapping("/{id}")
    public ResponseEntity<Object> obtener(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                pedidoClient.obtenerPedido(id)
        );
    }
}