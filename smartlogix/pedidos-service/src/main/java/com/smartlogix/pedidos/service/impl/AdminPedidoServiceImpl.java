package com.smartlogix.pedidos.service.impl;

import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.repository.PedidoRepository;
import com.smartlogix.pedidos.service.AdminPedidoService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminPedidoServiceImpl
        implements AdminPedidoService {

    private final PedidoRepository pedidoRepository;

    public AdminPedidoServiceImpl(
            PedidoRepository pedidoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
    }

    // ================= LISTAR TODOS =================

    @Override
    public List<Pedido> listar() {

        return pedidoRepository.findAll();
    }

    // ================= FILTRAR POR ESTADO =================

    @Override
    public List<Pedido> porEstado(
            EstadoPedido estado
    ) {

        return pedidoRepository.findByEstado(estado);
    }

    // ================= PREPARAR PEDIDO =================

    @Override
    public Pedido preparar(Long id) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado()
                != EstadoPedido.PAGADO) {

            throw new IllegalStateException(
                    "El pedido debe estar pagado"
            );
        }

        pedido.setEstado(
                EstadoPedido.EN_PREPARACION
        );

        return pedidoRepository.save(pedido);
    }

    // ================= ENVIAR PEDIDO =================

    @Override
    public Pedido enviar(Long id) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado()
                != EstadoPedido.EN_PREPARACION) {

            throw new IllegalStateException(
                    "El pedido no está preparado"
            );
        }

        pedido.setEstado(
                EstadoPedido.ENVIADO
        );

        return pedidoRepository.save(pedido);
    }

    // ================= ENTREGAR PEDIDO =================

    @Override
    public Pedido entregar(Long id) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado()
                != EstadoPedido.ENVIADO) {

            throw new IllegalStateException(
                    "El pedido no ha sido enviado"
            );
        }

        pedido.setEstado(
                EstadoPedido.ENTREGADO
        );

        return pedidoRepository.save(pedido);
    }

    // ================= BUSCAR PEDIDO =================

    private Pedido obtener(Long id) {

        return pedidoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Pedido no encontrado"
                        )
                );
    }
}