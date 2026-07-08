package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.exception.ApiResponse;
import com.smartlogix.pedidos.mapper.PedidoMapper;
import com.smartlogix.pedidos.dto.PedidoResponseDTO;
import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.service.AdminPedidoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/pedidos")
public class AdminPedidoController {

    private final AdminPedidoService service;

    public AdminPedidoController(
            AdminPedidoService service
    ) {
        this.service = service;
    }

    // ================= LISTAR TODOS =================

    @GetMapping
    public ResponseEntity<ApiResponse> listar() {

        List<PedidoResponseDTO> pedidos = service.listar()
                .stream()
                .map(PedidoMapper::toDTO)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Lista de pedidos",
                        pedidos
                )
        );
    }

    // ================= FILTRAR POR ESTADO =================

    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse> porEstado(
            @PathVariable EstadoPedido estado
    ) {

        List<PedidoResponseDTO> pedidos = service
                .porEstado(estado)
                .stream()
                .map(PedidoMapper::toDTO)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedidos filtrados por estado",
                        pedidos
                )
        );
    }

    // ================= PREPARAR =================

    @PutMapping("/{id}/preparar")
    public ResponseEntity<ApiResponse> preparar(
            @PathVariable Long id
    ) {

        Pedido pedido = service.preparar(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido en preparación",
                        PedidoMapper.toDTO(pedido)
                )
        );
    }

    // ================= ENVIAR =================

    @PutMapping("/{id}/enviar")
    public ResponseEntity<ApiResponse> enviar(
            @PathVariable Long id
    ) {

        Pedido pedido = service.enviar(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido enviado",
                        PedidoMapper.toDTO(pedido)
                )
        );
    }

    // ================= ENTREGAR =================

    @PutMapping("/{id}/entregar")
    public ResponseEntity<ApiResponse> entregar(
            @PathVariable Long id
    ) {

        Pedido pedido = service.entregar(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido entregado",
                        PedidoMapper.toDTO(pedido)
                )
        );
    }
}