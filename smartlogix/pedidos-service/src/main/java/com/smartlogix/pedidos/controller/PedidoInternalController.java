package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.repository.DetallePedidoRepository;
import com.smartlogix.pedidos.repository.PedidoRepository;
import com.smartlogix.pedidos.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/internal/pedidos")
public class PedidoInternalController {

    private final PedidoService service;
    private final DetallePedidoRepository detalleRepo;
    private final PedidoRepository pedidoRepo;

    public PedidoInternalController(PedidoService service,
                                    DetallePedidoRepository detalleRepo,
                                    PedidoRepository pedidoRepo) {
        this.service = service;
        this.detalleRepo = detalleRepo;
        this.pedidoRepo = pedidoRepo;
    }

    @GetMapping("/{id}")
    public Pedido obtenerInterno(@PathVariable Long id) {
        return service.obtener(id);
    }

    /**
     * Endpoint interno para verificar si un usuario ha comprado (y recibido) un producto.
     * Usado por inventory-service para validar la regla "Solo compradores pueden comentar".
     * GET /internal/pedidos/verificar-compra?usuarioId=XXX&productoId=YYY
     */
    @GetMapping("/verificar-compra")
    public boolean verificarCompra(@RequestParam String usuarioId,
                                   @RequestParam Long productoId) {
        return detalleRepo.verificarCompraEntregada(usuarioId, productoId);
    }

    /**
     * Recomendaciones: "Usuarios que compraron esto también compraron..."
     * GET /internal/pedidos/frecuentemente-comprados?productoId=YYY
     */
    @GetMapping("/frecuentemente-comprados")
    public List<Long> frecuentementeCompradoJuntos(@RequestParam Long productoId) {
        return detalleRepo.findFrequentlyBoughtTogether(productoId);
    }

    // ===================== ANALYTICS =====================

    /**
     * Analytics: Ingresos totales (solo pedidos DELIVERED).
     * GET /internal/pedidos/analytics/ingresos
     */
    @GetMapping("/analytics/ingresos")
    public Map<String, Object> ingresosTotales() {
        Double total = detalleRepo.calcularIngresosTotales();
        return Map.of("ingresos_totales", total != null ? total : 0.0);
    }

    /**
     * Analytics: Conteo de pedidos agrupados por estado.
     * GET /internal/pedidos/analytics/por-estado
     */
    @GetMapping("/analytics/por-estado")
    public Map<String, Long> pedidosPorEstado() {
        Map<String, Long> resultado = new LinkedHashMap<>();
        for (EstadoPedido estado : EstadoPedido.values()) {
            long count = pedidoRepo.countByEstado(estado);
            resultado.put(estado.name(), count);
        }
        return resultado;
    }

    /**
     * Analytics: Top 10 productos más vendidos.
     * GET /internal/pedidos/analytics/top-productos
     */
    @GetMapping("/analytics/top-productos")
    public List<Map<String, Object>> topProductos() {
        List<Object[]> rows = detalleRepo.findTopProductos();
        List<Map<String, Object>> resultado = new ArrayList<>();
        int rank = 1;
        for (Object[] row : rows) {
            if (rank > 10) break;
            resultado.add(Map.of(
                "rank", rank++,
                "productoId", row[0],
                "totalVendido", row[1]
            ));
        }
        return resultado;
    }
}