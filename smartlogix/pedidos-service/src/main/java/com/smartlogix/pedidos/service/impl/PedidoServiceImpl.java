package com.smartlogix.pedidos.service.impl;

import com.smartlogix.pedidos.client.InventoryClient;
import com.smartlogix.pedidos.client.UserClient;
import com.smartlogix.pedidos.dto.DetallePedidoDTO;
import com.smartlogix.pedidos.dto.PedidoRequestDTO;
import com.smartlogix.pedidos.kafka.producer.KafkaProducerService;
import com.smartlogix.pedidos.model.DetallePedido;
import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.repository.PedidoRepository;
import com.smartlogix.pedidos.service.PedidoService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PedidoServiceImpl
        implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final InventoryClient inventoryClient;
    private final UserClient userClient;
    private final KafkaProducerService producer;

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            InventoryClient inventoryClient,
            UserClient userClient,
            KafkaProducerService producer
    ) {
        this.pedidoRepository = pedidoRepository;
        this.inventoryClient = inventoryClient;
        this.userClient = userClient;
        this.producer = producer;
    }

    // ================= CREAR =================

    @Override
    public Pedido crearDesdeRequest(
            PedidoRequestDTO dto
    ) {

        validarUsuario(dto.getUsuarioId());

        if (dto.getProductos() == null
                || dto.getProductos().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debe tener productos"
            );
        }

        Pedido pedido = new Pedido();

        pedido.setUsuarioId(dto.getUsuarioId());

        double total = 0.0;

        for (DetallePedidoDTO d : dto.getProductos()) {

            validarDetalle(d);

            validarStock(
                    d.getProductoId(),
                    d.getCantidad()
            );

            DetallePedido det = new DetallePedido();

            det.setProductoId(d.getProductoId());
            det.setCantidad(d.getCantidad());
            det.setPrecio(d.getPrecioUnitario());
            det.setPedido(pedido);

            pedido.getDetalles().add(det);

            total += d.getCantidad()
                    * d.getPrecioUnitario();
        }

        pedido.setTotal(total);

        pedido.setEstado(
                EstadoPedido.PENDIENTE
        );

        return pedidoRepository.save(pedido);
    }

    // ================= OBTENER =================

    @Override
    public Pedido obtener(Long id) {

        return pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pedido no encontrado"
                        )
                );
    }

    // ================= ACTUALIZAR =================

    @Override
    public Pedido actualizarDesdeRequest(
            Long id,
            PedidoRequestDTO dto
    ) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado()
                == EstadoPedido.ENVIADO
                || pedido.getEstado()
                == EstadoPedido.ENTREGADO
                || pedido.getEstado()
                == EstadoPedido.CANCELADO) {

            throw new IllegalStateException(
                    "No se puede modificar el pedido"
            );
        }

        pedido.getDetalles().clear();

        double total = 0.0;

        for (DetallePedidoDTO d : dto.getProductos()) {

            validarDetalle(d);

            validarStock(
                    d.getProductoId(),
                    d.getCantidad()
            );

            DetallePedido det = new DetallePedido();

            det.setProductoId(d.getProductoId());
            det.setCantidad(d.getCantidad());
            det.setPrecio(d.getPrecioUnitario());
            det.setPedido(pedido);

            pedido.getDetalles().add(det);

            total += d.getCantidad()
                    * d.getPrecioUnitario();
        }

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    // ================= ELIMINAR =================

    @Override
    public void eliminar(Long id) {

        Pedido pedido = obtener(id);

        pedido.setEstado(
                EstadoPedido.CANCELADO
        );

        pedidoRepository.save(pedido);
    }

    // ================= POR USUARIO =================

    @Override
    public List<Pedido> porUsuario(
            String usuarioId
    ) {

        return pedidoRepository
                .findByUsuarioId(usuarioId);
    }

    // ================= PAGAR =================

    @Override
    public Pedido pagar(Long id) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado()
                != EstadoPedido.PENDIENTE) {

            throw new IllegalStateException(
                    "Solo pedidos pendientes pueden pagarse"
            );
        }

        pedido.setEstado(
                EstadoPedido.PAGADO
        );

        Pedido saved =
                pedidoRepository.save(pedido);

        saved.getDetalles().forEach(d ->
                producer.enviarEventoCompra(
                        d.getProductoId(),
                        d.getCantidad(),
                        saved.getUsuarioId()
                )
        );

        return saved;
    }

    // ================= CANCELAR =================

    @Override
    public Pedido cancelar(Long id) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado()
                == EstadoPedido.ENTREGADO) {

            throw new IllegalStateException(
                    "No se puede cancelar un pedido entregado"
            );
        }

        pedido.setEstado(
                EstadoPedido.CANCELADO
        );

        return pedidoRepository.save(pedido);
    }

    // ================= REACTIVAR =================

    @Override
    public Pedido reactivar(Long id) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado()
                != EstadoPedido.CANCELADO) {

            throw new IllegalStateException(
                    "Solo pedidos cancelados pueden reactivarse"
            );
        }

        pedido.setEstado(
                EstadoPedido.PENDIENTE
        );

        return pedidoRepository.save(pedido);
    }

    // ================= VALIDACIONES =================

    private void validarUsuario(
            String usuarioId
    ) {

        Boolean ok = userClient
                .validarUsuario(usuarioId)
                .join();

        if (!Boolean.TRUE.equals(ok)) {

            throw new EntityNotFoundException(
                    "Usuario no existe"
            );
        }
    }

    private void validarStock(
            Long productoId,
            Integer cantidad
    ) {

        Boolean ok = inventoryClient
                .validarStock(
                        productoId,
                        cantidad
                )
                .join();

        if (!Boolean.TRUE.equals(ok)) {

            throw new IllegalStateException(
                    "Stock insuficiente"
            );
        }
    }

    private void validarDetalle(
            DetallePedidoDTO d
    ) {

        if (d.getProductoId() == null) {

            throw new IllegalArgumentException(
                    "productoId requerido"
            );
        }

        if (d.getCantidad() == null
                || d.getCantidad() <= 0) {

            throw new IllegalArgumentException(
                    "cantidad inválida"
            );
        }

        if (d.getPrecioUnitario() == null
                || d.getPrecioUnitario() <= 0) {

            throw new IllegalArgumentException(
                    "precio inválido"
            );
        }
    }
}