package com.smartlogix.inventory.service.impl;

import com.smartlogix.inventory.dto.ProductoCreateRequest;
import com.smartlogix.inventory.dto.ProductoResponse;
import com.smartlogix.inventory.dto.ProductoUpdateRequest;
import com.smartlogix.inventory.dto.ComentarioCreateRequest;
import com.smartlogix.inventory.dto.ComentarioResponse;
import com.smartlogix.inventory.dto.ComentarioUpdateRequest;
import com.smartlogix.inventory.mapper.ProductoMapper;
import com.smartlogix.inventory.model.EstadoProducto;
import com.smartlogix.inventory.model.Producto;
import com.smartlogix.inventory.model.ProductoImagen;
import com.smartlogix.inventory.model.ProductoComentario;
import com.smartlogix.inventory.repository.ProductoRepository;
import com.smartlogix.inventory.repository.ProductoComentarioRepository;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;
    private final ProductoComentarioRepository comentarioRepo;

    public ProductoServiceImpl(ProductoRepository repo, ProductoComentarioRepository comentarioRepo) {
        this.repo = repo;
        this.comentarioRepo = comentarioRepo;
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

            List<ProductoImagen> productImages = new java.util.ArrayList<>();
            int orden = 0;
            for (String url : req.getImagenes()) {
                ProductoImagen img = new ProductoImagen(url, false, orden);
                img.setProducto(p);
                productImages.add(img);
                orden++;
            }
            if (!productImages.isEmpty()) {
                productImages.get(0).setEsPrincipal(true);
            }
            p.getImagenes().clear();
            p.getImagenes().addAll(productImages);
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

    @Transactional
    @Override
    public void reponerStock(Long id, Integer cantidad) {
        Producto p = getOrThrow(id);
        p.reponerStock(cantidad);
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

    @Override
    public List<ProductoResponse> destacados() {
        return repo.findAll()
                .stream()
                .sorted((a, b) -> b.getCantidadVendidos().compareTo(a.getCantidadVendidos()))
                .limit(5)
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public Integer stock(Long id) {
        return getOrThrow(id).getStock();
    }

    // ===================== PRODUCTOS RELACIONADOS =====================

    @Override
    public List<ProductoResponse> productosRelacionados(Long id) {
        Producto p = getOrThrow(id);
        return repo.findByCategoriaAndEstado(p.getCategoria(), EstadoProducto.ACTIVO)
                .stream()
                .filter(prod -> !prod.getId().equals(id))
                .limit(4)
                .map(ProductoMapper::toResponse)
                .toList();
    }

    // ===================== COMENTARIOS =====================

    private ComentarioResponse mapComentario(ProductoComentario c) {
        return new ComentarioResponse(c.getId(), c.getUsuarioId(), c.getNombreCliente(), c.getFecha(), c.getFechaActualizacion(), c.getCalificacion(), c.getComentario(), c.getCompraVerificada(), c.getRespuestaEmpresa());
    }

    @Override
    public List<ComentarioResponse> obtenerComentarios(Long id) {
        return comentarioRepo.findByProductoIdAndActivoTrueOrderByFechaDesc(id)
                .stream().map(this::mapComentario).toList();
    }

    @Override
    public ComentarioResponse agregarComentario(Long id, ComentarioCreateRequest request, String usuarioId, String nombreCliente) {
        Producto p = getOrThrow(id);
        ProductoComentario c = new ProductoComentario();
        c.setProducto(p);
        c.setUsuarioId(usuarioId);
        c.setNombreCliente(nombreCliente);
        c.setCalificacion(request.getCalificacion());
        c.setComentario(request.getComentario());
        c.setFecha(java.time.LocalDateTime.now());
        c.setActivo(true);
        c = comentarioRepo.save(c);
        
        p.agregarRating((double) request.getCalificacion());
        repo.save(p);
        return mapComentario(c);
    }

    @Override
    public ComentarioResponse actualizarComentario(Long comentarioId, ComentarioUpdateRequest request, String usuarioId) {
        ProductoComentario c = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        if (!c.getUsuarioId().equals(usuarioId)) throw new RuntimeException("No autorizado");
        
        c.setComentario(request.getComentario());
        if (request.getCalificacion() != null) {
            c.setCalificacion(request.getCalificacion());
        }
        c.setFechaActualizacion(java.time.LocalDateTime.now());
        return mapComentario(comentarioRepo.save(c));
    }

    @Override
    public void eliminarComentario(Long comentarioId, String usuarioId) {
        ProductoComentario c = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        if (!c.getUsuarioId().equals(usuarioId)) throw new RuntimeException("No autorizado");
        
        c.setActivo(false);
        comentarioRepo.save(c);
    }
}