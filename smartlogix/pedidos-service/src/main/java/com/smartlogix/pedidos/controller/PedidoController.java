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

    // ================= CREAR PEDIDO =================
    @PostMapping
    public ResponseEntity<ApiResponse> crear(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody PedidoRequestDTO dto
    ) {
        
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PedidoController.class);
        
        log.info("[CONTROLLER] POST /pedidos - Usuario: {}, Productos: {}", 
                dto.getUsuarioId(), dto.getProductos() != null ? dto.getProductos().size() : 0);

        try {
            Pedido nuevo = service.crearDesdeRequest(dto, authHeader);

            log.info("[CONTROLLER] Pedido creado exitosamente - ID: {}", nuevo.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(
                            201,
                            "Pedido creado correctamente",
                            PedidoMapper.toDTO(nuevo)
                    ));
        } catch (Exception e) {
            log.error("[CONTROLLER] Error al crear pedido - Usuario: {}, Error: {}", 
                    dto.getUsuarioId(), e.getMessage(), e);
            throw e;
        }
    }

    // ================= OBTENER PEDIDO =================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtener(@PathVariable Long id) {

        Pedido pedido = service.obtener(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido encontrado",
                        PedidoMapper.toDTO(pedido)
                )
        );
    }

    // ================= ACTUALIZAR PEDIDO =================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequestDTO dto
    ) {

        Pedido actualizado = service.actualizarDesdeRequest(id, dto);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido actualizado correctamente",
                        PedidoMapper.toDTO(actualizado)
                )
        );
    }

    // ================= PAGAR PEDIDO =================
    @PutMapping("/{id}/pagar")
    public ResponseEntity<ApiResponse> pagar(@PathVariable Long id) {

        Pedido pedido = service.pagar(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido pagado correctamente",
                        PedidoMapper.toDTO(pedido)
                )
        );
    }

    // ================= CANCELAR PEDIDO =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> cancelar(@PathVariable Long id) {

        service.cancelar(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido cancelado correctamente",
                        null
                )
        );
    }

    // ================= REACTIVAR PEDIDO =================
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<ApiResponse> reactivar(@PathVariable Long id) {

        Pedido pedido = service.reactivar(id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Pedido reactivado correctamente",
                        PedidoMapper.toDTO(pedido)
                )
        );
    }

    // ================= PEDIDOS POR USUARIO =================
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