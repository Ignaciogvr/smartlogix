package com.smartlogix.bff.controller;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BFF Controller for Vendedor role.
 * Base path: /vendedor  (matches Angular VendedorService URL: bff.url + '/vendedor/*')
 */
@RestController
@RequestMapping("/vendedor")
@PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
public class VendedorBffController {

    private final EnvioClient envioClient;
    private final InventoryClient inventoryClient;
    private final PedidoClient pedidoClient;

    public VendedorBffController(
            EnvioClient envioClient,
            InventoryClient inventoryClient,
            PedidoClient pedidoClient
    ) {
        this.envioClient = envioClient;
        this.inventoryClient = inventoryClient;
        this.pedidoClient = pedidoClient;
    }

    // ===========================
    // DASHBOARD
    // ===========================

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String vendedorId = jwt.getSubject();

        Map<String, Object> dashboard = new HashMap<>();

        ServiceEnvelope<List<ProductoCatalogoDTO>> productosResp = inventoryClient.productosVendedor(vendedorId);
        dashboard.put("productos", productosResp != null ? productosResp.getData() : List.of());

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProductos", productosResp != null && productosResp.getData() != null ? productosResp.getData().size() : 0);
        dashboard.put("stats", stats);

        return ResponseEntity.ok(dashboard);
    }

    // ===========================
    // PRODUCTOS
    // inventory-service: GET /vendedor/productos, POST /vendedor/productos
    // ===========================

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoCatalogoDTO>> getProductos(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String vendedorId = jwt.getSubject();
        ServiceEnvelope<List<ProductoCatalogoDTO>> resp = inventoryClient.productosVendedor(vendedorId);
        return ResponseEntity.ok(resp != null && resp.getData() != null ? resp.getData() : List.of());
    }

    @PostMapping("/productos")
    public ResponseEntity<ProductoCatalogoDTO> crearProducto(
            @RequestBody Object request,
            Authentication auth
    ) {
        ServiceEnvelope<ProductoCatalogoDTO> resp = inventoryClient.crearProductoVendedor(request);
        return ResponseEntity.ok(resp != null ? resp.getData() : null);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductoCatalogoDTO> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Object request
    ) {
        ServiceEnvelope<ProductoCatalogoDTO> resp = inventoryClient.actualizarProducto(id, request);
        return ResponseEntity.ok(resp != null ? resp.getData() : null);
    }

    @PutMapping("/productos/{id}/estado")
    public ResponseEntity<?> actualizarEstadoProducto(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        // inventory-service exposes PUT /admin/productos/{id}/reactivar (for reactivation)
        // No generic estado endpoint exists — documented as PENDING in microservice
        return ResponseEntity.status(409)
                .body(Map.of("error", "Funcionalidad pendiente en microservicio inventory-service"));
    }

    @PostMapping("/productos/{id}/imagenes")
    public ResponseEntity<?> subirImagenes(
            @PathVariable Long id,
            @RequestBody Object formData
    ) {
        // No existe endpoint en inventory-service para subir imágenes
        return ResponseEntity.status(409)
                .body(Map.of("error", "Funcionalidad pendiente en microservicio inventory-service"));
    }

    // ===========================
    // PEDIDOS
    // pedidos-service: GET /pedidos/usuario/{id}
    // ===========================

    @GetMapping("/pedidos")
    public ResponseEntity<List<com.smartlogix.bff.dto.response.PedidoResponse>> getPedidos(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String vendedorId = jwt.getSubject();
        List<com.smartlogix.bff.dto.response.PedidoResponse> pedidos = pedidoClient.pedidosUsuario(vendedorId);
        return ResponseEntity.ok(pedidos);
    }

    // ===========================
    // ENVÍOS VENDEDOR
    // envio-service: GET /api/envios/usuario/{vendedorId}
    // ===========================

    @GetMapping("/envios")
    public ResponseEntity<List<EnvioResponse>> getEnvios(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String vendedorId = jwt.getSubject();
        List<EnvioResponse> envios = envioClient.enviosUsuario(vendedorId);
        return ResponseEntity.ok(envios != null ? envios : List.of());
    }

    @PutMapping("/envios/{id}/estado")
    public ResponseEntity<EnvioResponse> actualizarEstadoEnvio(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body
    ) {
        com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest request =
                new com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest();
        request.setEstado(body.get("estado"));
        EnvioResponse result = envioClient.actualizarEstado(id, request);
        return ResponseEntity.ok(result);
    }

}
