package com.smartlogix.inventory.service;

import com.smartlogix.inventory.model.Proveedor;
import com.smartlogix.inventory.model.PedidoProveedor;
import com.smartlogix.inventory.model.DetallePedidoProveedor;

import java.util.List;

public interface ProveedorService {
    
    // Proveedor
    List<Proveedor> getAllProveedores();
    Proveedor getProveedorById(Long id);
    Proveedor createProveedor(Proveedor proveedor);
    Proveedor updateProveedor(Long id, Proveedor proveedor);
    void deleteProveedor(Long id);

    // PedidoProveedor
    List<PedidoProveedor> getAllPedidos();
    List<PedidoProveedor> getPedidosByProveedor(Long proveedorId);
    PedidoProveedor getPedidoById(Long id);
    PedidoProveedor createPedido(PedidoProveedor pedido);
    PedidoProveedor updatePedido(Long id, PedidoProveedor pedido);
    void deletePedido(Long id);

    // DetallePedidoProveedor
    List<DetallePedidoProveedor> getDetallesByPedido(Long pedidoId);
    DetallePedidoProveedor addDetalleToPedido(Long pedidoId, DetallePedidoProveedor detalle);
    void removeDetalle(Long id);
}
