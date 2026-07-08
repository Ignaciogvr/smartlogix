package com.smartlogix.pedidos.service.impl;

import com.smartlogix.pedidos.dto.CarritoItemRequest;
import com.smartlogix.pedidos.dto.CarritoItemResponse;
import com.smartlogix.pedidos.dto.CarritoResponse;
import com.smartlogix.pedidos.model.Carrito;
import com.smartlogix.pedidos.model.CarritoItem;
import com.smartlogix.pedidos.model.EstadoCarrito;
import com.smartlogix.pedidos.repository.CarritoRepository;
import com.smartlogix.pedidos.service.CarritoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;

    public CarritoServiceImpl(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    private Carrito getOrCreateCarrito(String usuarioId) {
        return carritoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseGet(() -> {
                    Carrito c = new Carrito();
                    c.setUsuarioId(usuarioId);
                    c.setEstado(EstadoCarrito.ACTIVO);
                    return carritoRepository.save(c);
                });
    }

    private CarritoResponse mapToResponse(Carrito c) {
        CarritoResponse r = new CarritoResponse();
        r.setId(c.getId());
        r.setUsuarioId(c.getUsuarioId());
        r.setEstado(c.getEstado().name());
        r.setTotal(c.getTotal());
        r.setFechaActualizacion(c.getFechaActualizacion());
        r.setItems(c.getItems().stream().map(i -> new CarritoItemResponse(
                i.getId(), i.getProductoId(), i.getCantidad(), i.getPrecio()
        )).collect(Collectors.toList()));
        return r;
    }

    @Override
    @Transactional
    public CarritoResponse obtenerCarrito(String usuarioId) {
        return mapToResponse(getOrCreateCarrito(usuarioId));
    }

    @Override
    @Transactional
    public CarritoResponse agregarItem(String usuarioId, CarritoItemRequest request) {
        Carrito carrito = getOrCreateCarrito(usuarioId);
        
        Optional<CarritoItem> existingItem = carrito.getItems().stream()
                .filter(i -> i.getProductoId().equals(request.getProductoId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CarritoItem item = existingItem.get();
            item.setCantidad(item.getCantidad() + request.getCantidad());
        } else {
            CarritoItem newItem = new CarritoItem();
            newItem.setCarrito(carrito);
            newItem.setProductoId(request.getProductoId());
            newItem.setCantidad(request.getCantidad());
            newItem.setPrecio(request.getPrecio());
            carrito.getItems().add(newItem);
        }
        
        carrito.recalcularTotal();
        return mapToResponse(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public CarritoResponse actualizarItem(String usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = getOrCreateCarrito(usuarioId);
        
        carrito.getItems().stream()
                .filter(i -> i.getProductoId().equals(productoId))
                .findFirst()
                .ifPresent(item -> {
                    if (cantidad <= 0) {
                        carrito.getItems().remove(item);
                    } else {
                        item.setCantidad(cantidad);
                    }
                });
                
        carrito.recalcularTotal();
        return mapToResponse(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public CarritoResponse removerItem(String usuarioId, Long productoId) {
        Carrito carrito = getOrCreateCarrito(usuarioId);
        carrito.getItems().removeIf(i -> i.getProductoId().equals(productoId));
        carrito.recalcularTotal();
        return mapToResponse(carritoRepository.save(carrito));
    }

    @Override
    @Transactional
    public void vaciarCarrito(String usuarioId) {
        Carrito carrito = getOrCreateCarrito(usuarioId);
        carrito.getItems().clear();
        carrito.recalcularTotal();
        carritoRepository.save(carrito);
    }
}
