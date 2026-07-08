package com.smartlogix.pedidos.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.pedidos.dto.CheckoutRequest;
import com.smartlogix.pedidos.dto.PedidoResponseDTO;
import com.smartlogix.pedidos.mapper.PedidoMapper;
import com.smartlogix.pedidos.model.*;
import com.smartlogix.pedidos.repository.CarritoRepository;
import com.smartlogix.pedidos.repository.IdempotencyKeyRepository;
import com.smartlogix.pedidos.repository.PagoRepository;
import com.smartlogix.pedidos.repository.PedidoRepository;
import com.smartlogix.pedidos.service.CheckoutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final IdempotencyKeyRepository idempotencyRepository;
    private final CarritoRepository carritoRepository;
    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final ObjectMapper objectMapper;
    private final com.smartlogix.pedidos.client.InventoryClient inventoryClient;

    public CheckoutServiceImpl(
            IdempotencyKeyRepository idempotencyRepository, 
            CarritoRepository carritoRepository, 
            PedidoRepository pedidoRepository,
            PagoRepository pagoRepository,
            ObjectMapper objectMapper,
            com.smartlogix.pedidos.client.InventoryClient inventoryClient) {
        this.idempotencyRepository = idempotencyRepository;
        this.carritoRepository = carritoRepository;
        this.pedidoRepository = pedidoRepository;
        this.pagoRepository = pagoRepository;
        this.objectMapper = objectMapper;
        this.inventoryClient = inventoryClient;
    }

    @Override
    @Transactional
    public PedidoResponseDTO procesarCheckout(String usuarioId, String idempotencyKey, CheckoutRequest request) {
        validarRequest(usuarioId, request);

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            var existingKey = idempotencyRepository.findByKey(idempotencyKey);
            if (existingKey.isPresent()) {
                try {
                    return objectMapper.readValue(existingKey.get().getResponseBody(), PedidoResponseDTO.class);
                } catch (Exception e) {
                    throw new RuntimeException("Error deserializando respuesta en caché");
                }
            }
        }

        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado o vacío"));

        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // VALIDAR STOCK DE TODOS LOS PRODUCTOS ANTES DE CREAR EL PEDIDO
        for (var item : carrito.getItems()) {
            inventoryClient.validarProducto(item.getProductoId(), item.getCantidad());
        }

        final Pedido pedido = new Pedido();

        pedido.setUsuarioId(usuarioId);
        pedido.setTotal(carrito.getTotal());
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setTelefonoContacto(request.getTelefonoContacto());
        pedido.setNotasEntrega(request.getNotasEntrega());
        pedido.setMetodoPago(request.getMetodoPago());
        
        var detalles = carrito.getItems().stream().map(item -> {
            DetallePedido detalle = new DetallePedido();
            detalle.setProductoId(item.getProductoId());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecio(item.getPrecio());
            detalle.setPedido(pedido);
            return detalle;
        }).collect(Collectors.toList());
        
        pedido.setDetalles(detalles);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        
        // ============================================
        // TAREA 2: Crear pago simulado
        // ============================================
        crearPagoSimulado(pedidoGuardado, usuarioId, request.getMetodoPago());
        
        carrito.setEstado(EstadoCarrito.CONVERTIDO_A_PEDIDO);
        carritoRepository.save(carrito);
        
        PedidoResponseDTO response = PedidoMapper.toDTO(pedidoGuardado);
        
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            try {
                IdempotencyKey idempKey = new IdempotencyKey();
                idempKey.setKey(idempotencyKey);
                idempKey.setResponseBody(objectMapper.writeValueAsString(response));
                idempKey.setResponseStatus(200);
                idempotencyRepository.save(idempKey);
            } catch (Exception e) {
                // Ignore serialization errors for idempotency cache
            }
        }
        
        return response;
    }

    private void validarRequest(String usuarioId, CheckoutRequest request) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new IllegalArgumentException("Usuario es requerido");
        }

        if (request == null) {
            throw new IllegalArgumentException("Request no puede ser null");
        }

        if (request.getDireccionEnvio() == null || request.getDireccionEnvio().isBlank()) {
            throw new IllegalArgumentException("Dirección de envío es requerida");
        }

        if (request.getMetodoPago() == null || request.getMetodoPago().isBlank()) {
            throw new IllegalArgumentException("Método de pago es requerido");
        }
    }

    /**
     * TAREA 2: Crear pago simulado
     * 
     * Según ALCANCE_CONGELADO_MVP.md:
     * - Al confirmar checkout, crear registro en tabla pagos con:
     *   - estado = COMPLETADO
     *   - metodo_pago = del request
     *   - monto = total del pedido
     *   - fecha_transaccion = now
     */
    private void crearPagoSimulado(Pedido pedido, String usuarioId, String metodoPagoStr) {
        try {
            Pago pago = new Pago();
            pago.setPedido(pedido);
            pago.setUsuarioId(usuarioId);
            pago.setMonto(pedido.getTotal());
            pago.setMoneda("CLP");
            
            // Convertir String a MetodoPago enum (si existe)
            MetodoPago metodoPago;
            try {
                metodoPago = MetodoPago.valueOf(metodoPagoStr.toUpperCase());
            } catch (Exception e) {
                // Si no existe el enum, usar TRANSFERENCIA como default
                metodoPago = MetodoPago.TRANSFERENCIA;
            }
            pago.setMetodoPago(metodoPago);
            
            // Generar número de referencia único
            String numeroRef = "SIM-" + pedido.getId() + "-" + System.currentTimeMillis();
            pago.setNumeroReferencia(numeroRef);
            
            // Marcar como CONFIRMADO (pago simulado siempre exitoso)
            pago.setEstadoPago(EstadoPago.CONFIRMADO);
            pago.setFechaConfirmacion(LocalDateTime.now());
            
            // Datos de simulación
            pago.setProveedorPago("SIMULADO");
            pago.setIdTransaccionProveedor("SIM-TXN-" + System.currentTimeMillis());
            
            pagoRepository.save(pago);
            
        } catch (Exception e) {
            // Log error pero no fallar el checkout si el pago simulado falla
            System.err.println("[ERROR] No se pudo crear pago simulado: " + e.getMessage());
        }
    }
}
