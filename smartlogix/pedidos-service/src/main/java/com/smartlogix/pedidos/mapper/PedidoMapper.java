package com.smartlogix.pedidos.mapper;

import com.smartlogix.pedidos.dto.*;
import com.smartlogix.pedidos.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static PedidoResponseDTO toDTO(Pedido p) {

        if (p == null) return null;

        List<DetallePedidoDTO> detalles = (p.getDetalles() == null)
                ? List.of()
                : p.getDetalles()
                    .stream()
                    .map(d -> {
                        DetallePedidoDTO dto = new DetallePedidoDTO();
                        dto.setProductoId(d.getProductoId());
                        dto.setCantidad(d.getCantidad());
                        dto.setPrecioUnitario(d.getPrecio());
                        return dto;
                    })
                    .collect(Collectors.toList());

        return new PedidoResponseDTO(
                p.getId(),
                p.getUsuarioId(),
                detalles,
                p.getTotal(),
                p.getEstado() != null ? p.getEstado().name() : null
        );
    }
}