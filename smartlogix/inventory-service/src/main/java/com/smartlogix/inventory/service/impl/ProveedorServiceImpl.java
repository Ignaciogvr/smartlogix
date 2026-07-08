package com.smartlogix.inventory.service.impl;

import com.smartlogix.inventory.model.Proveedor;
import com.smartlogix.inventory.model.PedidoProveedor;
import com.smartlogix.inventory.model.DetallePedidoProveedor;
import com.smartlogix.inventory.repository.ProveedorRepository;
import com.smartlogix.inventory.repository.PedidoProveedorRepository;
import com.smartlogix.inventory.repository.DetallePedidoProveedorRepository;
import com.smartlogix.inventory.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final PedidoProveedorRepository pedidoProveedorRepository;
    private final DetallePedidoProveedorRepository detallePedidoProveedorRepository;

    @Override
    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor getProveedorById(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Proveedor createProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional
    public Proveedor updateProveedor(Long id, Proveedor proveedor) {
        if (proveedorRepository.existsById(id)) {
            proveedor.setId(id);
            return proveedorRepository.save(proveedor);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    public List<PedidoProveedor> getAllPedidos() {
        return pedidoProveedorRepository.findAll();
    }

    @Override
    public List<PedidoProveedor> getPedidosByProveedor(Long proveedorId) {
        return pedidoProveedorRepository.findByProveedorId(proveedorId);
    }

    @Override
    public PedidoProveedor getPedidoById(Long id) {
        return pedidoProveedorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PedidoProveedor createPedido(PedidoProveedor pedido) {
        return pedidoProveedorRepository.save(pedido);
    }

    @Override
    @Transactional
    public PedidoProveedor updatePedido(Long id, PedidoProveedor pedido) {
        if (pedidoProveedorRepository.existsById(id)) {
            pedido.setId(id);
            return pedidoProveedorRepository.save(pedido);
        }
        return null;
    }

    @Override
    @Transactional
    public void deletePedido(Long id) {
        pedidoProveedorRepository.deleteById(id);
    }

    @Override
    public List<DetallePedidoProveedor> getDetallesByPedido(Long pedidoId) {
        return detallePedidoProveedorRepository.findByPedidoProveedorId(pedidoId);
    }

    @Override
    @Transactional
    public DetallePedidoProveedor addDetalleToPedido(Long pedidoId, DetallePedidoProveedor detalle) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(pedidoId).orElse(null);
        if (pedido != null) {
            detalle.setPedidoProveedor(pedido);
            return detallePedidoProveedorRepository.save(detalle);
        }
        return null;
    }

    @Override
    @Transactional
    public void removeDetalle(Long id) {
        detallePedidoProveedorRepository.deleteById(id);
    }
}
