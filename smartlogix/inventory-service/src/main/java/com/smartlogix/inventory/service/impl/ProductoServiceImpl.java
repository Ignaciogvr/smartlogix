package com.smartlogix.inventory.service.impl;

import com.smartlogix.inventory.client.PedidosClient;
import com.smartlogix.inventory.dto.*;
import com.smartlogix.inventory.kafka.producer.KafkaProducer;
import com.smartlogix.inventory.mapper.ProductoMapper;
import com.smartlogix.inventory.model.*;
import com.smartlogix.inventory.repository.*;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repo;
    private final ProductoComentarioRepository comentarioRepo;
    private final ComentarioReporteRepository reporteRepo;
    private final ComentarioModeracionRepository moderacionRepo;
    private final PedidosClient pedidosClient;
    private final HistorialNavegacionRepository historialRepo;
    private final KafkaProducer kafkaProducer;

    public ProductoServiceImpl(ProductoRepository repo,
                               ProductoComentarioRepository comentarioRepo,
                               ComentarioReporteRepository reporteRepo,
                               PedidosClient pedidosClient,
                               HistorialNavegacionRepository historialRepo,
                               ComentarioModeracionRepository moderacionRepo,
                               KafkaProducer kafkaProducer) {
        this.repo = repo;
        this.comentarioRepo = comentarioRepo;
        this.reporteRepo = reporteRepo;
        this.moderacionRepo = moderacionRepo;
        this.pedidosClient = pedidosClient;
        this.historialRepo = historialRepo;
        this.kafkaProducer = kafkaProducer;
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

    // ===================== BUSQUEDAS Y FILTROS =====================

    @Override
    public List<ProductoResponse> ofertas() {
        return repo.findOfertasActivas(EstadoProducto.ACTIVO).stream().map(ProductoMapper::toResponse).toList();
    }

    @Override
    public List<ProductoResponse> nuevos() {
        return repo.findNuevosActivos(EstadoProducto.ACTIVO).stream().map(ProductoMapper::toResponse).toList();
    }

    @Override
    public List<ProductoResponse> buscarPorTexto(String q) {
        if (q == null || q.isBlank()) return activos();
        return repo.buscarPorTexto(q, EstadoProducto.ACTIVO).stream().map(ProductoMapper::toResponse).toList();
    }

    @Override
    public List<ProductoResponse> filtrar(Double precioMin, Double precioMax, String marca, Double ratingMin) {
        return repo.filtrar(EstadoProducto.ACTIVO, precioMin, precioMax, marca, ratingMin).stream().map(ProductoMapper::toResponse).toList();
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

        int stockRestante = p.getStock();
        if (stockRestante <= 0) {
            kafkaProducer.enviarProductoAgotado(id);
        } else if (stockRestante <= 5) {
            kafkaProducer.enviarStockBajo(id, stockRestante, 5);
        }
    }

    @Transactional
    @Override
    public void reponerStock(Long id, Integer cantidad) {
        Producto p = getOrThrow(id);
        p.reponerStock(cantidad);
        repo.save(p);
        kafkaProducer.enviarProductoRepuesto(id, p.getStock());
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
        return repo.findByCategoria(p.getCategoria())
                .stream()
                .filter(prod -> !prod.getId().equals(id))
                .limit(5)
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> productosRelacionadosPorMarca(Long id) {
        Producto p = getOrThrow(id);
        return repo.findTop5ByMarcaAndIdNot(p.getMarca(), id, org.springframework.data.domain.PageRequest.of(0, 5))
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> menosVendidos() {
        return repo.findTop5ByEstadoOrderByCantidadVendidosAsc(EstadoProducto.ACTIVO, org.springframework.data.domain.PageRequest.of(0, 5))
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public void registrarVista(Long id, String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) return;
        Producto p = getOrThrow(id);
        
        // Opcional: borrar vista anterior del mismo producto para este usuario
        historialRepo.deleteByUsuarioIdAndProductoId(usuarioId, id);
        
        com.smartlogix.inventory.model.HistorialNavegacion vista = new com.smartlogix.inventory.model.HistorialNavegacion();
        vista.setUsuarioId(usuarioId);
        vista.setProducto(p);
        historialRepo.save(vista);
    }

    @Override
    public List<ProductoResponse> vistosRecientemente(String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) return java.util.Collections.emptyList();
        return historialRepo.findByUsuarioIdOrderByFechaVistaDesc(usuarioId, org.springframework.data.domain.PageRequest.of(0, 10))
                .stream()
                .filter(h -> com.smartlogix.inventory.model.EstadoProducto.ACTIVO.equals(h.getProducto().getEstado()))
                .map(h -> ProductoMapper.toResponse(h.getProducto()))
                .toList();
    }

    @Override
    public List<ProductoResponse> recomendados(String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) return destacados();
        
        List<com.smartlogix.inventory.model.HistorialNavegacion> vistos = historialRepo.findByUsuarioIdOrderByFechaVistaDesc(usuarioId, org.springframework.data.domain.PageRequest.of(0, 3));
        if (vistos.isEmpty()) return destacados();
        
        // Recomendación simple: productos de la misma categoría que el último visto
        String categoria = vistos.get(0).getProducto().getCategoria();
        return repo.findByCategoriaAndEstado(categoria, EstadoProducto.ACTIVO)
                .stream()
                .filter(p -> vistos.stream().noneMatch(v -> v.getProducto().getId().equals(p.getId())))
                .limit(5)
                .map(ProductoMapper::toResponse)
                .toList();
    }

    // ===================== COMENTARIOS =====================

    private ComentarioResponse mapComentario(ProductoComentario c) {
        ComentarioResponse r = new ComentarioResponse(
            c.getId(), c.getUsuarioId(), c.getNombreCliente(),
            c.getFecha(), c.getFechaActualizacion(),
            c.getCalificacion(), c.getComentario(),
            c.getCompraVerificada(), c.getRespuestaEmpresa(),
            c.getImagenUrl()
        );
        r.setDestacado(c.getDestacado());
        r.setVotosUtilidad(c.getVotosUtilidad());
        return r;
    }

    @Override
    public List<ComentarioResponse> obtenerComentarios(Long id) {
        return comentarioRepo.findByProductoIdAndActivoTrueOrderByFechaDesc(id)
                .stream()
                .filter(c -> {
                    // Exclude comments that are hidden or blocked via moderation
                    return moderacionRepo.findByComentarioId(c.getId())
                            .map(m -> !(m.getAccion() == com.smartlogix.inventory.model.ModeracionAccion.OCULTADO || m.getAccion() == com.smartlogix.inventory.model.ModeracionAccion.BLOQUEADO))
                            .orElse(true);
                })
                .map(this::mapComentario)
                .toList();
    }

    @Override
    public ComentarioResponse agregarComentario(Long id, ComentarioCreateRequest request, String usuarioId, String nombreCliente) {
        Producto p = getOrThrow(id);

        // 2.2 - Validación: solo compradores verificados pueden comentar
        boolean compraVerificada = pedidosClient.verificarCompra(usuarioId, id);
        if (!compraVerificada) {
            throw new RuntimeException("Solo los compradores que han recibido el producto pueden dejar una reseña");
        }

        // 2.2 - Verificar compra y evitar doble comentario por la misma compra
        if (request.getPedidoId() == null) {
            throw new RuntimeException("pedidoId es requerido para comentar");
        }
        if (comentarioRepo.existsByProductoIdAndUsuarioIdAndPedidoIdAndActivoTrue(id, usuarioId, request.getPedidoId())) {
            throw new RuntimeException("Ya has comentado este producto en esta compra.");
        }

        ProductoComentario c = new ProductoComentario();
        c.setProducto(p);
        c.setUsuarioId(usuarioId);
        c.setNombreCliente(nombreCliente);
        c.setPedidoId(request.getPedidoId());
        c.setCalificacion(request.getCalificacion());
        c.setComentario(request.getComentario());
        c.setImagenUrl(request.getImagenUrl());
        c.setFecha(java.time.LocalDateTime.now());
        c.setActivo(true);
        c.setCompraVerificada(true);
        c.setRecomendado(request.getRecomendado());

        comentarioRepo.save(c);

        p.recalcularRating(comentarioRepo.findByProductoIdAndActivoTrueOrderByFechaDesc(id));
        repo.save(p);

        kafkaProducer.enviarComentarioCreado(c.getId(), p.getId(), usuarioId);

        return ProductoMapper.toComentarioResponse(c);
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

    /**
     * Moderación de reviews por ADMIN.
     * Permite al administrador desactivar cualquier comentario sin importar el autor.
     */
    @Override
    @Transactional
    public ComentarioModeracionResponseDTO moderarComentario(Long comentarioId, com.smartlogix.inventory.dto.ComentarioModeracionRequestDTO request) {
        // Validate existence of comment
        comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        // Find existing moderation record
        com.smartlogix.inventory.model.ComentarioModeracion mod = moderacionRepo.findByComentarioId(comentarioId)
                .orElseGet(() -> {
                    com.smartlogix.inventory.model.ComentarioModeracion newMod = new com.smartlogix.inventory.model.ComentarioModeracion();
                    newMod.setComentarioId(comentarioId);
                    return newMod;
                });
        mod.setAccion(com.smartlogix.inventory.model.ModeracionAccion.valueOf(request.getAccion()));
        mod.setModeradorId(request.getModeradorId());
        mod.setFechaAccion(java.time.LocalDateTime.now());
        moderacionRepo.save(mod);
        return com.smartlogix.inventory.mapper.ComentarioModeracionMapper.toDto(mod);
    }

    @Override
    public List<ProductoResponse> listarPorVendedor(String vendedorId) {
        return repo.findByVendedorId(vendedorId).stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    // ===================== NUEVAS FUNCIONALIDADES COMENTARIOS =====================

    /** 2.7 - Responder un comentario (solo ADMIN o VENDEDOR del producto) */
    @Override
    @Transactional
    public ComentarioResponse responderComentario(Long comentarioId, String respuesta, String rol) {
        if (!"ADMIN".equalsIgnoreCase(rol) && !"VENDEDOR".equalsIgnoreCase(rol)) {
            throw new RuntimeException("Solo administradores y vendedores pueden responder comentarios");
        }
        ProductoComentario c = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        if (!c.getActivo()) throw new RuntimeException("El comentario está inactivo");
        c.setRespuestaEmpresa(respuesta);
        c.setFechaActualizacion(java.time.LocalDateTime.now());
        return mapComentario(comentarioRepo.save(c));
    }

    /** 2.8 - Reportar comentario inapropiado (cualquier usuario autenticado) */
    @Override
    @Transactional
    public ComentarioReporteResponse reportarComentario(Long comentarioId, String usuarioId, String motivo) {
        ProductoComentario c = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        if (reporteRepo.existsByComentarioIdAndUsuarioId(comentarioId, usuarioId)) {
            throw new RuntimeException("Ya has reportado este comentario");
        }
        ComentarioReporte r = new ComentarioReporte();
        r.setComentario(c);
        r.setUsuarioId(usuarioId);
        r.setMotivo(motivo);

        reporteRepo.save(r);

        c.setReportes(c.getReportes() + 1);
        // TODO: Implementar moderación automática cuando se agregue el campo estado a la tabla
        comentarioRepo.save(c);

        kafkaProducer.enviarComentarioReportado(r.getId(), c.getId(), usuarioId);

        return ProductoMapper.toReporteResponse(r);
    }

    /** 2.8 - Listar reportes pendientes (solo ADMIN) */
    @Override
    public List<ComentarioReporteResponse> reportesPendientes() {
        return reporteRepo.findAllPendientes().stream()
                .map(r -> new ComentarioReporteResponse(
                        r.getId(), r.getComentario().getId(),
                        r.getUsuarioId(), r.getMotivo(), r.getFecha(), r.getResuelto()))
                .toList();
    }

    /** 2.14 - Distribución porcentual de ratings por estrella */
    @Override
    public List<DistribucionRatingDTO> distribucionRatings(Long productoId) {
        List<Object[]> raw = comentarioRepo.getDistribucionRatings(productoId);
        long total = raw.stream().mapToLong(row -> (Long) row[1]).sum();
        return raw.stream().map(row -> {
            int cal = ((Number) row[0]).intValue();
            long cnt = (Long) row[1];
            double pct = total > 0 ? Math.round((cnt * 100.0 / total) * 10.0) / 10.0 : 0.0;
            return new DistribucionRatingDTO(cal, cnt, pct);
        }).toList();
    }

    /** 2.15 - Marcar comentario como útil (incrementa votos) */
    @Override
    @Transactional
    public ComentarioResponse marcarUtil(Long comentarioId) {
        comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        comentarioRepo.incrementarVotosUtilidad(comentarioId);
        return mapComentario(comentarioRepo.findById(comentarioId).get());
    }

    /** 2.15 - Destacar comentario (solo ADMIN) */
    @Override
    @Transactional
    public ComentarioResponse marcarDestacado(Long comentarioId, Boolean destacado) {
        ProductoComentario c = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
        c.setDestacado(destacado);
        return mapComentario(comentarioRepo.save(c));
    }

    /** 2.15 - Listar comentarios destacados de un producto */
    @Override
    public List<ComentarioResponse> comentariosDestacados(Long productoId) {
        return comentarioRepo.findDestacadosByProductoId(productoId)
                .stream().map(this::mapComentario).toList();
    }
}