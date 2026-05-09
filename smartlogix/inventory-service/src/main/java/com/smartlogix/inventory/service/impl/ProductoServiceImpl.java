package com.smartlogix.inventory.service.impl;

import com.smartlogix.inventory.dto.ProductoCreateRequest;
import com.smartlogix.inventory.dto.ProductoResponse;
import com.smartlogix.inventory.dto.ProductoUpdateRequest;
import com.smartlogix.inventory.mapper.ProductoMapper;
import com.smartlogix.inventory.model.EstadoProducto;
import com.smartlogix.inventory.model.Producto;
import com.smartlogix.inventory.repository.ProductoRepository;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;

    public ProductoServiceImpl(ProductoRepository repo) {
        this.repo = repo;
    }

    private Producto getOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // ===================== READ =====================

    @Override
    public List<ProductoResponse> listar() {
        return repo.findByEstado(EstadoProducto.ACTIVO)
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> activos() {
        return listar();
    }

    @Override
    public List<ProductoResponse> porCategoria(String categoria) {

        if (categoria == null || categoria.isBlank())
            throw new RuntimeException("Categoría inválida");

        return repo.findByCategoriaAndEstado(categoria, EstadoProducto.ACTIVO)
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> bajoStock() {
        return repo.findByStockLessThan(5)
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    // ===================== CREATE =====================

    @Override
    public ProductoResponse crear(ProductoCreateRequest req) {

        if (req.getNombre() == null || req.getNombre().isBlank())
            throw new RuntimeException("Nombre obligatorio");

        if (repo.findByNombre(req.getNombre()).isPresent())
            throw new RuntimeException("Producto ya existe");

        if (req.getPrecio() == null || req.getPrecio() <= 0)
            throw new RuntimeException("Precio inválido");

        if (req.getStock() == null || req.getStock() < 0)
            throw new RuntimeException("Stock inválido");

        if (req.getImagenes() != null && req.getImagenes().size() > 5)
            throw new RuntimeException("Máximo 5 imágenes");

        Producto p = ProductoMapper.toEntity(req);

        return ProductoMapper.toResponse(repo.save(p));
    }

    // ===================== UPDATE =====================

    @Override
    public ProductoResponse actualizar(Long id, ProductoUpdateRequest req) {

        Producto p = getOrThrow(id);

        if (req.getNombre() != null) p.setNombre(req.getNombre());
        if (req.getDescripcion() != null) p.setDescripcion(req.getDescripcion());
        if (req.getPrecio() != null) p.setPrecio(req.getPrecio());
        if (req.getStock() != null) p.setStock(req.getStock());
        if (req.getCategoria() != null) p.setCategoria(req.getCategoria());

        if (req.getImagenes() != null) {
            if (req.getImagenes().size() > 5)
                throw new RuntimeException("Máximo 5 imágenes");

            p.setImagenes(req.getImagenes());
        }

        return ProductoMapper.toResponse(repo.save(p));
    }

    // ===================== GET =====================

    @Override
    public ProductoResponse obtener(Long id) {
        return ProductoMapper.toResponse(getOrThrow(id));
    }

    // ===================== DELETE =====================

    @Override
    public void eliminar(Long id) {
        Producto p = getOrThrow(id);
        p.setEstado(EstadoProducto.INACTIVO);
        repo.save(p);
    }

    @Override
    public ProductoResponse reactivar(Long id) {
        Producto p = getOrThrow(id);
        p.setEstado(EstadoProducto.ACTIVO);
        return ProductoMapper.toResponse(repo.save(p));
    }

    // ===================== VALIDACIÓN =====================

    @Override
    public ProductoResponse validarProducto(Long id, Integer cantidad) {

        Producto p = getOrThrow(id);

        if (p.getEstado() != EstadoProducto.ACTIVO)
            throw new RuntimeException("Producto inactivo");

        if (cantidad == null || cantidad <= 0)
            throw new RuntimeException("Cantidad inválida");

        if (p.getStock() < cantidad)
            throw new RuntimeException("Stock insuficiente");

        return ProductoMapper.toResponse(p);
    }

    // ===================== STOCK =====================

    @Transactional
    @Override
    public void descontarStock(Long id, Integer cantidad, String usuarioId) {

        Producto p = getOrThrow(id);

        if (p.getEstado() != EstadoProducto.ACTIVO)
            throw new RuntimeException("Producto inactivo");

        p.descontarStock(cantidad);

        repo.save(p);
    }

    // ===================== RATING =====================

    @Override
    public void agregarRating(Long id, Double rating) {

        if (rating == null || rating < 1 || rating > 5)
            throw new RuntimeException("Rating inválido");

        Producto p = getOrThrow(id);

        p.agregarRating(rating);

        repo.save(p);
    }

    // ===================== 🔥 NUEVOS MÉTODOS QUE TE FALTABAN =====================

    @Override
    public List<ProductoResponse> destacados() {

        return repo.findAll()
                .stream()
                .sorted((a, b) -> b.getCantidadVendidos()
                        .compareTo(a.getCantidadVendidos()))
                .limit(5)
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public Integer stock(Long id) {
        return getOrThrow(id).getStock();
    }
}