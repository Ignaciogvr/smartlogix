package com.smartlogix.pedidos.service.impl;

import com.smartlogix.pedidos.client.InventoryClient;
import com.smartlogix.pedidos.client.UserClient;
import com.smartlogix.pedidos.dto.DetallePedidoDTO;
import com.smartlogix.pedidos.dto.PedidoRequestDTO;
import com.smartlogix.pedidos.event.PedidoEstadoObserver;
import com.smartlogix.pedidos.event.PedidoEventFactory;
import com.smartlogix.pedidos.kafka.producer.KafkaProducerService;
import com.smartlogix.pedidos.model.DetallePedido;
import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.repository.PedidoRepository;
import com.smartlogix.pedidos.service.PedidoService;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern: usa PedidoEstadoObserver para notificar cambios de estado.
 * Factory Pattern: usa PedidoEventFactory para crear eventos (en compra).
 *
 * El KafkaProducerService se mantiene solo para enviarEventoCompra (inventario),
 * ya que ese flujo no es un cambio de estado de pedido.
 */
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    private final PedidoRepository pedidoRepository;
    private final InventoryClient inventoryClient;
    private final UserClient userClient;
    private final KafkaProducerService producer;
    private final PedidoEstadoObserver observer;
    private final com.smartlogix.pedidos.repository.PedidoHistorialRepository historialRepository;

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            InventoryClient inventoryClient,
            UserClient userClient,
            KafkaProducerService producer,
            PedidoEstadoObserver observer,
            com.smartlogix.pedidos.repository.PedidoHistorialRepository historialRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.inventoryClient = inventoryClient;
        this.userClient = userClient;
        this.producer = producer;
        this.observer = observer;
        this.historialRepository = historialRepository;
    }

    @Override
    public Pedido crearDesdeRequest(PedidoRequestDTO dto, String authHeader) {
        
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PedidoServiceImpl.class);
        
        log.info("[COMPRA] Iniciando creación de pedido - Usuario: {}, Productos: {}", 
                dto.getUsuarioId(), dto.getProductos() != null ? dto.getProductos().size() : 0);

        if (dto.getUsuarioId() == null || dto.getUsuarioId().isBlank()) {
            log.error("[COMPRA] Error: Usuario es requerido");
            throw new IllegalArgumentException("Usuario es requerido");
        }

        if (dto.getProductos() == null || dto.getProductos().isEmpty()) {
            log.error("[COMPRA] Error: Debe tener productos");
            throw new IllegalArgumentException("Debe tener productos");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(dto.getUsuarioId());
        pedido.setDetalles(new ArrayList<>());

        double total = 0.0;

        try {
            for (DetallePedidoDTO d : dto.getProductos()) {

                log.info("[COMPRA] Procesando detalle - ProductoID: {}, Cantidad: {}, Precio: {}", 
                        d.getProductoId(), d.getCantidad(), d.getPrecioUnitario());

                validarDetalle(d);
                
                log.info("[COMPRA] Validando stock para productoID: {}", d.getProductoId());
                validarStock(d.getProductoId(), d.getCantidad());
                log.info("[COMPRA] Stock validado correctamente para productoID: {}", d.getProductoId());

                DetallePedido det = new DetallePedido();
                det.setProductoId(d.getProductoId());
                det.setCantidad(d.getCantidad());
                det.setPrecio(d.getPrecioUnitario());
                det.setPedido(pedido);

                pedido.getDetalles().add(det);

                total += d.getCantidad() * d.getPrecioUnitario();
            }

            pedido.setTotal(total);
            pedido.setEstado(EstadoPedido.PENDIENTE);

            log.info("[COMPRA] Guardando pedido en base de datos - Usuario: {}, Total: {}", 
                    dto.getUsuarioId(), total);
            Pedido saved = pedidoRepository.save(pedido);
            log.info("[COMPRA] Pedido guardado con ID: {}", saved.getId());
            
            historialRepository.save(new com.smartlogix.pedidos.model.PedidoHistorial(saved.getId(), null, EstadoPedido.PENDIENTE));

            // Factory Pattern: crear lista de ProductoEvent
            List<com.smartlogix.pedidos.event.ProductoEvent> productosEvent = saved.getDetalles().stream()
                .map(d -> PedidoEventFactory.compra(d.getProductoId(), d.getCantidad(), saved.getUsuarioId()))
                .map(c -> new com.smartlogix.pedidos.event.ProductoEvent(c.getProductoId(), c.getCantidad()))
                .toList();

            // Observer Pattern: notificar pedido creado
            observer.onPedidoCreado(
                    saved,
                    dto.getDireccionDestino() != null ? dto.getDireccionDestino() : "Dirección no especificada",
                    productosEvent
            );

            log.info("[COMPRA] Pedido creado exitosamente - ID: {}, Usuario: {}", saved.getId(), saved.getUsuarioId());
            return saved;
            
        } catch (Exception e) {
            log.error("[COMPRA] Error al crear pedido - Usuario: {}, Error: {}, StackTrace: {}", 
                    dto.getUsuarioId(), e.getMessage(), getStackTraceAsString(e));
            throw e;
        }
    }
    
    private String getStackTraceAsString(Exception e) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    @Override
    public Pedido obtener(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
    }

    @Override
    public Pedido actualizarDesdeRequest(Long id, PedidoRequestDTO dto) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado() == EstadoPedido.ENVIADO ||
            pedido.getEstado() == EstadoPedido.ENTREGADO ||
            pedido.getEstado() == EstadoPedido.CANCELADO) {

            throw new IllegalStateException("No se puede modificar el pedido");
        }

        pedido.getDetalles().clear();

        double total = 0.0;

        for (DetallePedidoDTO d : dto.getProductos()) {

            validarDetalle(d);
            validarStock(d.getProductoId(), d.getCantidad());

            DetallePedido det = new DetallePedido();
            det.setProductoId(d.getProductoId());
            det.setCantidad(d.getCantidad());
            det.setPrecio(d.getPrecioUnitario());
            det.setPedido(pedido);

            pedido.getDetalles().add(det);

            total += d.getCantidad() * d.getPrecioUnitario();
        }

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    @Override
    public void eliminar(Long id) {
        pedidoRepository.delete(obtener(id));
    }

    @Override
    public List<Pedido> porUsuario(String usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Pedido pagar(Long id) {
        
        log.info("[PAGO] Iniciando pago para pedido ID: {}", id);

        try {
            Pedido pedido = obtener(id);

            if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
                log.error("[PAGO] Error: Pedido no está en estado PENDIENTE - Estado actual: {}", pedido.getEstado());
                throw new IllegalStateException("Solo pedidos pendientes pueden pagarse");
            }

            log.info("[PAGO] Cambiando estado a PAGADO para pedido ID: {}", id);
            pedido.setEstado(EstadoPedido.PAGADO);

            Pedido saved = pedidoRepository.save(pedido);
            log.info("[PAGO] Pedido guardado con estado PAGADO - ID: {}", saved.getId());

            historialRepository.save(new com.smartlogix.pedidos.model.PedidoHistorial(saved.getId(), EstadoPedido.PENDIENTE, EstadoPedido.PAGADO));

            // Enviar eventos de compra al inventario (mantiene producer directo)
            log.info("[PAGO] Enviando eventos de descuento de stock para pedido ID: {}", saved.getId());
            saved.getDetalles().forEach(d -> {
                log.info("[PAGO] Descontando stock - ProductoID: {}, Cantidad: {}", d.getProductoId(), d.getCantidad());
                producer.enviarEventoCompra(
                        d.getProductoId(),
                        d.getCantidad(),
                        saved.getUsuarioId()
                );
            });

            // Observer Pattern: notificar cambio de estado
            observer.onEstadoCambiado(saved);

            log.info("[PAGO] Pago completado exitosamente - ID: {}", saved.getId());
            return saved;
            
        } catch (Exception e) {
            log.error("[PAGO] Error al procesar pago - ID: {}, Error: {}, StackTrace: {}", 
                    id, e.getMessage(), getStackTraceAsString(e));
            throw e;
        }
    }

    @Override
    public Pedido cancelar(Long id) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("No se puede cancelar un pedido entregado");
        }

        // Capturar estado antes de cancelar
        EstadoPedido estadoAnterior = pedido.getEstado();

        pedido.setEstado(EstadoPedido.CANCELADO);

        Pedido saved = pedidoRepository.save(pedido);

        historialRepository.save(new com.smartlogix.pedidos.model.PedidoHistorial(saved.getId(), estadoAnterior, EstadoPedido.CANCELADO));

        // Si el pedido ya tenía stock descontado (post-pago), reponer cada línea
        if (estadoAnterior == EstadoPedido.PAGADO
                || estadoAnterior == EstadoPedido.EN_PREPARACION
                || estadoAnterior == EstadoPedido.ENVIADO) {
            saved.getDetalles().forEach(detalle ->
                    inventoryClient.reponerStock(
                            detalle.getProductoId(),
                            detalle.getCantidad()
                    )
            );
        }

        // Observer Pattern: notificar cancelación
        observer.onPedidoCancelado(saved);

        return saved;
    }

    @Override
    public Pedido reactivar(Long id) {

        Pedido pedido = obtener(id);

        if (pedido.getEstado() != EstadoPedido.CANCELADO) {
            throw new IllegalStateException("Solo pedidos cancelados pueden reactivarse");
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);

        Pedido saved = pedidoRepository.save(pedido);

        historialRepository.save(new com.smartlogix.pedidos.model.PedidoHistorial(saved.getId(), EstadoPedido.CANCELADO, EstadoPedido.PENDIENTE));

        // Observer Pattern: notificar cambio de estado
        observer.onEstadoCambiado(saved);

        return saved;
    }

    // =========================
    // 🔥 FIX DEFINITIVO STOCK
    // =========================
    private void validarStock(Long productoId, Integer cantidad) {

        Integer stock = inventoryClient.obtenerStock(productoId);

        if (stock == null) {
            throw new IllegalStateException("No se pudo obtener stock del producto");
        }

        if (stock < cantidad) {
            throw new IllegalStateException(
                    "Stock insuficiente. Disponible: " + stock + ", solicitado: " + cantidad
            );
        }
    }

    private void validarDetalle(DetallePedidoDTO d) {

        if (d.getProductoId() == null)
            throw new IllegalArgumentException("productoId requerido");

        if (d.getCantidad() == null || d.getCantidad() <= 0)
            throw new IllegalArgumentException("cantidad inválida");

        if (d.getPrecioUnitario() == null || d.getPrecioUnitario() <= 0)
            throw new IllegalArgumentException("precio inválido");
    }

    // =========================
    // 🔥 EXPIRACIÓN DE PEDIDOS (CRON)
    // =========================
    @org.springframework.scheduling.annotation.Scheduled(fixedRate = 60000) // Se ejecuta cada 1 minuto
    @Transactional
    public void expirarPedidosPendientes() {
        java.time.LocalDateTime haceUnaHora = java.time.LocalDateTime.now().minusHours(1);
        List<Pedido> expirados = pedidoRepository.findAll().stream()
                .filter(p -> p.getEstado() == EstadoPedido.PENDIENTE)
                .filter(p -> {
                    // Buscar historial de creación (PENDIENTE)
                    return historialRepository.findByPedidoIdOrderByFechaDesc(p.getId()).stream()
                            .filter(h -> h.getEstadoNuevo() == EstadoPedido.PENDIENTE)
                            .findFirst()
                            .map(h -> h.getFecha().isBefore(haceUnaHora))
                            // Si por alguna razón no tiene historial, asumimos expirado si es viejo (simplificación para este PoC, 
                            // asumiendo que al iniciar sin BD todos se crean con historial ahora)
                            .orElse(false);
                })
                .toList();

        for (Pedido p : expirados) {
            org.slf4j.LoggerFactory.getLogger(PedidoServiceImpl.class)
                    .info("Expirando pedido automáticamente: id={}", p.getId());
            p.setEstado(EstadoPedido.EXPIRADO);
            pedidoRepository.save(p);
            historialRepository.save(new com.smartlogix.pedidos.model.PedidoHistorial(p.getId(), EstadoPedido.PENDIENTE, EstadoPedido.EXPIRADO));
            
            // Notificar a Kafka que fue "cancelado" por expiración
            observer.onPedidoCancelado(p);
            observer.onEstadoCambiado(p);
        }
    }
}