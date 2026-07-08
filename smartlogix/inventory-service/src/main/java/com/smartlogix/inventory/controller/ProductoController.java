package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.exception.ApiResponse;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // 🔓 LISTAR PRODUCTOS
    @GetMapping
    public ResponseEntity<ApiResponse> listar() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Lista de productos", service.listar())
        );
    }

    // 🔓 ACTIVOS
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse> activos() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos activos", service.activos())
        );
    }

    // 🔓 DETALLE PRODUCTO
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto encontrado", service.obtener(id))
        );
    }

    // 🔓 POR CATEGORÍA
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponse> porCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos por categoría", service.porCategoria(categoria))
        );
    }

    // 🔓 OFERTAS
    @GetMapping("/ofertas")
    public ResponseEntity<ApiResponse> ofertas() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos en oferta", service.ofertas())
        );
    }

    // 🔓 NUEVOS
    @GetMapping("/nuevos")
    public ResponseEntity<ApiResponse> nuevos() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos nuevos", service.nuevos())
        );
    }

    // 🔓 MENOS VENDIDOS
    @GetMapping("/menos-vendidos")
    public ResponseEntity<ApiResponse> menosVendidos() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos menos vendidos", service.menosVendidos())
        );
    }

    // 🔓 BUSCAR
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse> buscarPorTexto(@RequestParam(name = "q", required = false) String q) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Resultados de búsqueda", service.buscarPorTexto(q))
        );
    }

    // 🔓 FILTRAR
    @GetMapping("/filtrar")
    public ResponseEntity<ApiResponse> filtrar(
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Double ratingMin
    ) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos filtrados", service.filtrar(precioMin, precioMax, marca, ratingMin))
        );
    }

    // 🔥 DESTACADOS (HOME FRONT)
    @GetMapping("/destacados")
    public ResponseEntity<ApiResponse> destacados() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos destacados", service.destacados())
        );
    }

    // 🔥 COMENTARIOS
    @GetMapping("/{id}/comentarios")
    public ResponseEntity<ApiResponse> obtenerComentarios(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentarios del producto", service.obtenerComentarios(id))
        );
    }

    /** 2.14 - Distribución de ratings por estrella (con porcentaje) */
    @GetMapping("/{id}/comentarios/distribucion-ratings")
    public ResponseEntity<ApiResponse> distribucionRatings(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Distribución de ratings", service.distribucionRatings(id))
        );
    }

    /** 2.15 - Comentarios marcados como destacados */
    @GetMapping("/{id}/comentarios/destacados")
    public ResponseEntity<ApiResponse> comentariosDestacados(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentarios destacados", service.comentariosDestacados(id))
        );
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ApiResponse> agregarComentario(
            @PathVariable Long id,
            @RequestBody com.smartlogix.inventory.dto.ComentarioCreateRequest request,
            @RequestHeader("X-User-Id") String usuarioId,
            @RequestHeader(value = "X-User-Name", defaultValue = "Usuario") String nombreCliente) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentario creado", service.agregarComentario(id, request, usuarioId, nombreCliente))
        );
    }

    // 🔥 RELACIONADOS
    @GetMapping("/{id}/relacionados")
    public ResponseEntity<ApiResponse> productosRelacionados(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos relacionados", service.productosRelacionados(id))
        );
    }

    @GetMapping("/{id}/relacionados-marca")
    public ResponseEntity<ApiResponse> productosRelacionadosPorMarca(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos relacionados por marca", service.productosRelacionadosPorMarca(id))
        );
    }

    // 🔥 HISTORIAL Y RECOMENDACIONES
    @PostMapping("/{id}/vistas")
    public ResponseEntity<ApiResponse> registrarVista(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) String usuarioId) {
        service.registrarVista(id, usuarioId);
        return ResponseEntity.ok(new ApiResponse(200, "Vista registrada", null));
    }

    @GetMapping("/vistos")
    public ResponseEntity<ApiResponse> vistosRecientemente(
            @RequestHeader(value = "X-User-Id", required = false) String usuarioId) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos vistos recientemente", service.vistosRecientemente(usuarioId))
        );
    }

    @GetMapping("/recomendados")
    public ResponseEntity<ApiResponse> recomendados(
            @RequestHeader(value = "X-User-Id", required = false) String usuarioId) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos recomendados", service.recomendados(usuarioId))
        );
    }

    // 🔥 STOCK (solo lectura)
    @GetMapping("/stock/{id}")
    public ResponseEntity<ApiResponse> stock(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Stock del producto", service.stock(id))
        );
    }

    @GetMapping("/{id}/validar")
    public ResponseEntity<ApiResponse> validarProducto(
            @PathVariable Long id,
            @RequestParam Integer cantidad
    ) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto válido", service.validarProducto(id, cantidad))
        );
    }

    @PutMapping("/stock/{id}/reponer")

    public ResponseEntity<ApiResponse> reponerStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad
    ) {
        service.reponerStock(id, cantidad);
        return ResponseEntity.ok(
                new ApiResponse(200, "Stock repuesto correctamente", null)
        );
    }

    @PutMapping("/stock/{id}/descontar")
    public ResponseEntity<ApiResponse> descontarStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestParam(required = false, defaultValue = "sistema") String usuarioId
    ) {
        service.descontarStock(id, cantidad, usuarioId);
        return ResponseEntity.ok(
                new ApiResponse(200, "Stock descontado correctamente", null)
        );
    }

    /**
     * Filtros avanzados de catálogo con ordenación.
     * GET /productos/catalogo?sort=rating_desc|ventas_desc|comentarios_desc|precio_asc|precio_desc
     * Combina con los filtros base (precio, marca, rating) ya existentes.
     */
    @GetMapping("/catalogo")
    public ResponseEntity<ApiResponse> catalogo(
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Double ratingMin,
            @RequestParam(required = false, defaultValue = "rating_desc") String sort
    ) {
        java.util.List<com.smartlogix.inventory.dto.ProductoResponse> productos =
                service.filtrar(precioMin, precioMax, marca, ratingMin);

        // Aplicar ordenación en memoria basada en el parámetro sort
        java.util.Comparator<com.smartlogix.inventory.dto.ProductoResponse> comparator = switch (sort) {
            case "ventas_desc"      -> java.util.Comparator.comparingInt(
                    p -> -java.util.Optional.ofNullable(((com.smartlogix.inventory.dto.ProductoResponse)p).getVendidos()).orElse(0));
            case "precio_asc"       -> java.util.Comparator.comparingDouble(
                    p -> java.util.Optional.ofNullable(((com.smartlogix.inventory.dto.ProductoResponse)p).getPrecio()).orElse(0.0));
            case "precio_desc"      -> java.util.Comparator.comparingDouble(
                    p -> -java.util.Optional.ofNullable(((com.smartlogix.inventory.dto.ProductoResponse)p).getPrecio()).orElse(0.0));
            default                 -> java.util.Comparator.comparingDouble(
                    p -> -java.util.Optional.ofNullable(((com.smartlogix.inventory.dto.ProductoResponse)p).getRating()).orElse(0.0));
        };

        productos = productos.stream().sorted(comparator).toList();
        return ResponseEntity.ok(new ApiResponse(200, "Catálogo con filtros avanzados", productos));
    }
}
