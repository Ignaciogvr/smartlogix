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
                        dto.setVendedorId(d.getVendedorId());
                        return dto;
                    })
                    .collect(Collectors.toList());

        List<PagoDTO> pagos = (p.getPagos() == null)
                ? List.of()
                : p.getPagos()
                    .stream()
                    .map(PedidoMapper::pagoToDTO)
                    .collect(Collectors.toList());


        PedidoResponseDTO dto = new PedidoResponseDTO(
                p.getId(),
                p.getUsuarioId(),
                detalles,
                p.getTotal(),
                p.getEstado() != null ? p.getEstado().name() : null,
                p.getFecha(),
                p.getFechaCompletado(),
                p.getRequiereLogisticaInversa(),
                pagos
        );
        dto.setNotasInternas(p.getNotasInternas());
        return dto;
    }

    public static PagoDTO pagoToDTO(Pago pago) {
        if (pago == null) return null;

        return new PagoDTO(
                pago.getId(),
                pago.getMonto(),
                pago.getEstadoPago() != null ? pago.getEstadoPago().name() : null,
                pago.getFechaTransaccion(),
                pago.getFechaConfirmacion(),
                pago.getEstadoPago() != null ? pago.getEstadoPago().getDescripcion() : null,
                pago.getMetodoPago() != null ? pago.getMetodoPago().name() : null,
                pago.getNumeroReferencia()
        );
    }

}