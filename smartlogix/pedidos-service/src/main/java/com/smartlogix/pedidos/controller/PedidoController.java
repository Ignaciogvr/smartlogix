package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.dto.PedidoDTO;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.service.PedidoService;
import com.smartlogix.pedidos.exception.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listar() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Lista de pedidos", service.listar())
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> crear(@Valid @RequestBody PedidoDTO dto) {

        Pedido nuevo = service.crearDesdeDTO(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(201, "Pedido creado correctamente", nuevo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Pedido encontrado", service.obtener(id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody PedidoDTO dto) {

        Pedido actualizado = service.actualizarDesdeDTO(id, dto);

        return ResponseEntity.ok(
                new ApiResponse(200, "Pedido actualizado", actualizado)
        );
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<ApiResponse> cerrar(@PathVariable Long id) {

        Pedido pedido = service.cerrar(id);

        return ResponseEntity.ok(
                new ApiResponse(200, "Pedido cerrado y stock actualizado", pedido)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminar(@PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse(200, "Pedido cerrado (eliminación lógica)", null)
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse> porUsuario(@PathVariable String usuarioId) {

        return ResponseEntity.ok(
                new ApiResponse(200, "Pedidos del usuario", service.porUsuario(usuarioId))
        );
    }
}