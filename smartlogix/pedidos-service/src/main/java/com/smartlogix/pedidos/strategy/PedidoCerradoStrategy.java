package com.smartlogix.pedidos.strategy;

import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;

public class PedidoCerradoStrategy {

    public void ejecutar(Pedido pedido) {
        pedido.setEstado(EstadoPedido.CERRADO);
    }
}