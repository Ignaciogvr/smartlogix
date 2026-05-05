package com.smartlogix.inventory.service;

import com.smartlogix.inventory.model.Producto;
import com.smartlogix.inventory.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    // 📦 LISTAR TODO
    public List<Producto> listar() {
        return repository.findAll();
    }

    // 📦 POR CATEGORÍA
    public List<Producto> porCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    // 📦 PRODUCTOS ACTIVOS
    public List<Producto> activos() {
        return repository.findByEstado("ACTIVO");
    }

    // 📦 BAJO STOCK
    public List<Producto> bajoStock() {
        return repository.findByStockLessThan(5);
    }

    // ➕ CREAR
    public Producto crear(Producto producto) {

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new RuntimeException("Nombre obligatorio");
        }

        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new RuntimeException("Precio inválido");
        }

        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new RuntimeException("Stock inválido");
        }

        producto.setEstado("ACTIVO");
        producto.setCantidadVendidos(0);
        producto.setRatingPromedio(0.0);

        return repository.save(producto);
    }

    // 🔍 OBTENER
    public Producto obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // ✏️ ACTUALIZAR
    public Producto actualizar(Long id, Producto datos) {

        Producto producto = obtener(id);

        if (datos.getNombre() != null) producto.setNombre(datos.getNombre());
        if (datos.getDescripcion() != null) producto.setDescripcion(datos.getDescripcion());
        if (datos.getPrecio() != null) producto.setPrecio(datos.getPrecio());
        if (datos.getStock() != null) producto.setStock(datos.getStock());
        if (datos.getCategoria() != null) producto.setCategoria(datos.getCategoria());
        if (datos.getImagenUrl() != null) producto.setImagenUrl(datos.getImagenUrl());

        return repository.save(producto);
    }

    // 🗑️ ELIMINAR LOGICO
    public void eliminar(Long id) {
        Producto producto = obtener(id);
        producto.setEstado("INACTIVO");
        repository.save(producto);
    }

    // 🔄 REACTIVAR
    public Producto reactivar(Long id) {
        Producto producto = obtener(id);
        producto.setEstado("ACTIVO");
        return repository.save(producto);
    }

    // 📉 VALIDAR STOCK
    public Producto validarProducto(Long id, Integer cantidad) {

        Producto producto = obtener(id);

        if (!"ACTIVO".equals(producto.getEstado())) {
            throw new RuntimeException("Producto inactivo");
        }

        if (cantidad <= 0) {
            throw new RuntimeException("Cantidad inválida");
        }

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        return producto;
    }

    // 💥 DESCONTAR STOCK (KAFKA)
    public void descontarStock(Long productoId, Integer cantidad, String usuarioId) {

        Producto producto = validarProducto(productoId, cantidad);

        producto.setStock(producto.getStock() - cantidad);
        producto.setCantidadVendidos(producto.getCantidadVendidos() + cantidad);

        repository.save(producto);
    }

    // ⭐ RATING
    public void agregarRating(Long id, Double rating) {
        Producto producto = obtener(id);
        producto.agregarRating(rating);
        repository.save(producto);
    }
}