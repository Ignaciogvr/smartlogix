package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.dto.PedidoRequestDTO;
import com.smartlogix.pedidos.dto.PedidoResponseDTO;
import com.smartlogix.pedidos.exception.ApiResponse;
import com.smartlogix.pedidos.mapper.PedidoMapper;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.service.PedidoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // ================= LISTAR =================
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

    // ================= CREAR =================
    @PostMapping
    public ResponseEntity<ApiResponse> crear(
            @Valid @RequestBody PedidoRequestDTO dto
    ) {

        Pedido nuevo = service.crearDesdeRequest(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse(
                                201,
                                "Pedido creado correctamente",
                                PedidoMapper.toDTO(nuevo)
                        )
                );
    }

    // ================= OBTENER =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtener(
            @PathVariable Long id
    ) {

        Pedido pedido = service.obtener(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido encontrado",
                        PedidoMapper.toDTO(pedido)
                )
        );
    }

    // ================= ACTUALIZAR =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO dto
    ) {

        Pedido actualizado = service.actualizarDesdeRequest(id, dto);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido actualizado",
                        PedidoMapper.toDTO(actualizado)
                )
        );
    }

    // ================= CERRAR =================
    @PutMapping("/{id}/cerrar")
    public ResponseEntity<ApiResponse> cerrar(
            @PathVariable Long id
    ) {

        Pedido pedido = service.cerrar(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido cerrado y stock actualizado",
                        PedidoMapper.toDTO(pedido)
                )
        );
    }

    // ================= ELIMINAR =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminar(
            @PathVariable Long id
    ) {

        service.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido cerrado (eliminación lógica)",
                        null
                )
        );
    }

    // ================= POR USUARIO =================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse> porUsuario(
            @PathVariable String usuarioId
    ) {

        List<PedidoResponseDTO> pedidos = service.porUsuario(usuarioId)
                .stream()
                .map(PedidoMapper::toDTO)
                .toList();

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedidos del usuario",
                        pedidos
                )
        );
    }
}