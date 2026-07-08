package com.smartlogix.bff.client;

import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.request.ProductoCreateRequest;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@Component
public class InventoryClient {

    private static final ParameterizedTypeReference<ServiceEnvelope<List<ProductoCatalogoDTO>>> ENVELOPE_LIST =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<ProductoCatalogoDTO>> ENVELOPE_ONE =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<Integer>> ENVELOPE_INT =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<List<com.smartlogix.bff.dto.response.ComentarioDTO>>> ENVELOPE_COMMENTS =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<com.smartlogix.bff.dto.response.ComentarioDTO>> ENVELOPE_COMMENT =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<Void>> ENVELOPE_VOID =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<List<ProductoCatalogoDTO>> LIST_PLAIN =
            new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    public InventoryClient(
            @Qualifier("inventoryWebClient")
            WebClient webClient
    ) {
        this.webClient = webClient;
    }

    // ===================== CATALOGO PUBLICO =====================

    public ServiceEnvelope<List<ProductoCatalogoDTO>> listarProductos() {
        return webClient
                .get()
                .uri("/productos")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<ProductoCatalogoDTO> obtenerProducto(Long id) {
        return webClient
                .get()
                .uri("/productos/{id}", id)
                .retrieve()
                .bodyToMono(ENVELOPE_ONE)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosActivos() {
        return webClient
                .get()
                .uri("/productos/activos")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosPorCategoria(String categoria) {
        return webClient
                .get()
                .uri("/productos/categoria/{categoria}", categoria)
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosDestacados() {
        return webClient
                .get()
                .uri("/productos/destacados")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<Integer> stockProducto(Long id) {
        return webClient
                .get()
                .uri("/productos/stock/{id}", id)
                .retrieve()
                .bodyToMono(ENVELOPE_INT)
                .block();
    }

    public ServiceEnvelope<List<com.smartlogix.bff.dto.response.ComentarioDTO>> obtenerComentarios(Long id) {
        return webClient
                .get()
                .uri("/productos/{id}/comentarios", id)
                .retrieve()
                .bodyToMono(ENVELOPE_COMMENTS)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosRelacionados(Long id) {
        return webClient
                .get()
                .uri("/productos/{id}/relacionados", id)
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<com.smartlogix.bff.dto.response.ComentarioDTO> agregarComentario(Long id, com.smartlogix.bff.dto.request.ComentarioCreateRequestDTO request, String userId, String userName) {
        return webClient
                .post()
                .uri("/productos/{id}/comentarios", id)
                .header("X-User-Id", userId)
                .header("X-User-Name", userName)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ENVELOPE_COMMENT)
                .block();
    }

    public ServiceEnvelope<com.smartlogix.bff.dto.response.ComentarioDTO> actualizarComentario(Long comentarioId, com.smartlogix.bff.dto.request.ComentarioUpdateRequestDTO request, String userId) {
        return webClient
                .put()
                .uri("/comentarios/{id}", comentarioId)
                .header("X-User-Id", userId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ENVELOPE_COMMENT)
                .block();
    }

    public ServiceEnvelope<Void> eliminarComentario(Long comentarioId, String userId) {
        return webClient
                .delete()
                .uri("/comentarios/{id}", comentarioId)
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToMono(ENVELOPE_VOID)
                .block();
    }

    // ===================== BÚSQUEDA Y FILTROS =====================

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosOfertas() {
        return webClient
                .get()
                .uri("/productos/ofertas")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosNuevos() {
        return webClient
                .get()
                .uri("/productos/nuevos")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> buscarProductos(String q) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/productos/buscar").queryParam("q", q).build())
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> filtrarProductos(Double precioMin, Double precioMax, String marca, Double ratingMin) {
        return webClient
                .get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path("/productos/filtrar");
                    if (precioMin != null) b = b.queryParam("precioMin", precioMin);
                    if (precioMax != null) b = b.queryParam("precioMax", precioMax);
                    if (marca != null && !marca.isBlank()) b = b.queryParam("marca", marca);
                    if (ratingMin != null) b = b.queryParam("ratingMin", ratingMin);
                    return b.build();
                })
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<Object>> listarBanners() {
        return webClient
                .get()
                .uri("/banners")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ServiceEnvelope<List<Object>>>() {})
                .block();
    }

    public ServiceEnvelope<ProductoCatalogoDTO> validarStock(Long id, Integer cantidad) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/productos/{id}/validar").queryParam("cantidad", cantidad).build(id))
                .retrieve()
                .bodyToMono(ENVELOPE_ONE)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosMenosVendidos() {
        return webClient
                .get()
                .uri("/productos/menos-vendidos")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosRelacionadosPorMarca(Long id) {
        return webClient
                .get()
                .uri("/productos/{id}/relacionados-marca", id)
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public void registrarVista(Long id, String userId) {
        webClient
                .post()
                .uri("/productos/{id}/vistas", id)
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosVistosRecientemente(String userId) {
        return webClient
                .get()
                .uri("/productos/vistos")
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosRecomendados(String userId) {
        return webClient
                .get()
                .uri("/productos/recomendados")
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    // ===================== VENDEDOR =====================

    /**
     * GET /vendedor/productos
     * inventory-service retorna List<ProductoCatalogoDTO> directamente (sin ServiceEnvelope wrapper).
     */
    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosVendedor(String vendedorId) {
        List<ProductoCatalogoDTO> lista = webClient
                .get()
                .uri("/vendedor/productos")
                .retrieve()
                .bodyToMono(LIST_PLAIN)
                .onErrorReturn(Collections.emptyList())
                .block();
        ServiceEnvelope<List<ProductoCatalogoDTO>> envelope = new ServiceEnvelope<>();
        envelope.setData(lista != null ? lista : Collections.emptyList());
        return envelope;
    }

    /**
     * POST /vendedor/productos
     */
    public ServiceEnvelope<ProductoCatalogoDTO> crearProductoVendedor(Object request) {
        return webClient
                .post()
                .uri("/vendedor/productos")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ENVELOPE_ONE)
                .block();
    }

    // ===================== ADMIN =====================

    /**
     * PUT /admin/productos/{id}
     */
    public ServiceEnvelope<ProductoCatalogoDTO> actualizarProducto(Long id, Object request) {
        return webClient
                .put()
                .uri("/admin/productos/{id}", id)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ENVELOPE_ONE)
                .block();
    }

    /**
     * POST /admin/productos
     */
    public ServiceEnvelope<ProductoCatalogoDTO> crearProducto(ProductoCreateRequest request) {
        return webClient
                .post()
                .uri("/admin/productos")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ENVELOPE_ONE)
                .block();
    }

    /**
     * DELETE /admin/productos/{id}
     */
    public void eliminarProducto(Long id) {
        webClient
                .delete()
                .uri("/admin/productos/{id}", id)
                .retrieve()
                .bodyToMono(ENVELOPE_VOID)
                .block();
    }
}
