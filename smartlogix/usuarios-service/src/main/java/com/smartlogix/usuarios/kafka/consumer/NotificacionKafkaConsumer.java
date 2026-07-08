package com.smartlogix.usuarios.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumidor Kafka para notificaciones dentro de usuarios-service.
 * No se crea un microservicio separado. Este componente recibe eventos
 * de otros microservicios y simula el envío de notificaciones (logs).
 */
@Component
public class NotificacionKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificacionKafkaConsumer.class);

    /**
     * Escucha eventos del dominio de pedidos y genera notificaciones simples.
     */
    @KafkaListener(topics = {"pedidos-events", "pedido-creado", "pedido-actualizado"}, groupId = "usuarios-service")
    public void onPedidoEvent(String mensaje) {
        try {
            log.info("[NOTIFICACION] Evento de pedido recibido: {}", mensaje);
            if (mensaje.contains("CREATED") || mensaje.contains("PAID")) {
                log.info("[EMAIL SIMULADO] ¡Tu pedido ha sido confirmado! Pronto lo prepararemos.");
            } else if (mensaje.contains("SHIPPED") || mensaje.contains("IN_TRANSIT")) {
                log.info("[EMAIL SIMULADO] ¡Tu pedido está en camino! Puedes seguir el tracking.");
            } else if (mensaje.contains("DELIVERED")) {
                log.info("[EMAIL SIMULADO] ¡Tu pedido ha sido entregado! Esperamos que te haya gustado.");
            } else if (mensaje.contains("CANCELLED")) {
                log.info("[EMAIL SIMULADO] Tu pedido ha sido cancelado. Si tienes dudas, contáctanos.");
            }
        } catch (Exception e) {
            log.error("[NOTIFICACION ERROR] Error procesando evento de pedido: {}", e.getMessage());
        }
    }

    /**
     * Escucha eventos del dominio de envíos.
     */
    @KafkaListener(topics = {"envios-events", "envio-actualizado", "envio-creado", "envio-entregado"}, groupId = "usuarios-service")
    public void onEnvioEvent(String mensaje) {
        try {
            log.info("[NOTIFICACION] Evento de envío recibido: {}", mensaje);
            if (mensaje.contains("TrackingActualizado") || mensaje.contains("IN_TRANSIT") || mensaje.contains("EN_CAMINO")) {
                log.info("[EMAIL SIMULADO] ¡Tu paquete está en reparto! Lo recibirás hoy.");
            } else if (mensaje.contains("DELIVERED") || mensaje.contains("ENTREGADO") || mensaje.contains("EnvioEntregado")) {
                log.info("[EMAIL SIMULADO] ¡Entrega completada! Confirma la recepción en la app.");
            } else if (mensaje.contains("FAILED") || mensaje.contains("FALLIDO")) {
                log.info("[EMAIL SIMULADO] Intento de entrega fallido. Reprogramamos mañana.");
            } else if (mensaje.contains("TransportistaAsignado")) {
                log.info("[EMAIL SIMULADO] Se ha asignado un transportista a tu envío.");
            } else if (mensaje.contains("ETAActualizada")) {
                log.info("[EMAIL SIMULADO] La hora estimada de entrega de tu pedido ha sido actualizada.");
            } else if (mensaje.contains("EvidenciaEntregaSubida")) {
                log.info("[EMAIL SIMULADO] Se ha registrado evidencia fotográfica de tu entrega.");
            }
        } catch (Exception e) {
            log.error("[NOTIFICACION ERROR] Error procesando evento de envío: {}", e.getMessage());
        }
    }

    /**
     * Escucha eventos de pagos.
     */
    @KafkaListener(topics = {"pagos-events", "pago-procesado"}, groupId = "usuarios-service")
    public void onPagoEvent(String mensaje) {
        try {
            log.info("[NOTIFICACION] Evento de pago recibido: {}", mensaje);
            if (mensaje.contains("APPROVED")) {
                log.info("[EMAIL SIMULADO] ¡Pago aprobado! Tu pedido está siendo procesado.");
            } else if (mensaje.contains("REJECTED")) {
                log.info("[EMAIL SIMULADO] Pago rechazado. Por favor verifica tu método de pago.");
            }
        } catch (Exception e) {
            log.error("[NOTIFICACION ERROR] Error procesando evento de pago: {}", e.getMessage());
        }
    }

    /**
     * Escucha eventos del dominio de devoluciones.
     */
    @KafkaListener(topics = {"devolucion-events"}, groupId = "usuarios-service")
    public void onDevolucionEvent(String mensaje) {
        try {
            log.info("[NOTIFICACION] Evento de devolución recibido: {}", mensaje);
            if (mensaje.contains("DevolucionCreada")) {
                log.info("[EMAIL SIMULADO] Hemos recibido tu solicitud de devolución. La revisaremos pronto.");
            } else if (mensaje.contains("DevolucionRecibida")) {
                log.info("[EMAIL SIMULADO] Tu devolución ha sido recibida en nuestras instalaciones.");
            } else if (mensaje.contains("DevolucionEvaluando")) {
                log.info("[EMAIL SIMULADO] Tu devolución está siendo evaluada por nuestro equipo.");
            } else if (mensaje.contains("DevolucionAprobada")) {
                log.info("[EMAIL SIMULADO] ¡Tu devolución fue aprobada! El reembolso se procesará en breve.");
            } else if (mensaje.contains("DevolucionRechazada")) {
                log.info("[EMAIL SIMULADO] Tu devolución fue rechazada. Contacta con soporte para más detalles.");
            } else if (mensaje.contains("DevolucionEstadoActualizado")) {
                log.info("[EMAIL SIMULADO] El estado de tu devolución ha sido actualizado.");
            }
        } catch (Exception e) {
            log.error("[NOTIFICACION ERROR] Error procesando evento de devolución: {}", e.getMessage());
        }
    }

    /**
     * Escucha alertas de stock del inventario (para administradores).
     */
    @KafkaListener(topics = {"stock-alertas", "producto-creado"}, groupId = "usuarios-service-admin")
    public void onInventarioEvent(String mensaje) {
        try {
            log.info("[NOTIFICACION ADMIN] Evento de inventario recibido: {}", mensaje);
            if (mensaje.contains("StockBajo")) {
                log.warn("[ADMIN ALERTA] Stock bajo detectado en un producto. Revisar inventario.");
            } else if (mensaje.contains("ProductoAgotado")) {
                log.warn("[ADMIN ALERTA] ¡Producto agotado! Se requiere reposición urgente.");
            } else if (mensaje.contains("ProductoRepuesto")) {
                log.info("[ADMIN INFO] Stock repuesto para un producto.");
            } else if (mensaje.contains("ProductoCreadoEvent") || mensaje.contains("producto-creado")) {
                log.info("[ADMIN INFO] Nuevo producto publicado en catálogo.");
            }
        } catch (Exception e) {
            log.error("[NOTIFICACION ERROR] Error procesando evento de inventario: {}", e.getMessage());
        }
    }
    /**
     * Escucha eventos de usuarios (registro, actualización de perfil).
     */
    @KafkaListener(topics = {"usuarios-events"}, groupId = "usuarios-service")
    public void onUsuarioEvent(String mensaje) {
        try {
            log.info("[NOTIFICACION] Evento de usuario recibido: {}", mensaje);
            if (mensaje.contains("UsuarioRegistrado")) {
                log.info("[EMAIL SIMULADO] ¡Bienvenido a SmartLogix! Tu cuenta ha sido creada exitosamente.");
            } else if (mensaje.contains("PerfilActualizado")) {
                log.info("[EMAIL SIMULADO] Los datos de tu perfil han sido actualizados.");
            }
        } catch (Exception e) {
            log.error("[NOTIFICACION ERROR] Error procesando evento de usuario: {}", e.getMessage());
        }
    }

    /**
     * Escucha eventos de comentarios (creación y reportes).
     */
    @KafkaListener(topics = {"comentarios-events"}, groupId = "usuarios-service")
    public void onComentarioEvent(String mensaje) {
        try {
            log.info("[NOTIFICACION] Evento de comentario recibido: {}", mensaje);
            if (mensaje.contains("ComentarioCreado")) {
                log.info("[NOTIFICACION INTERNA] Tu comentario ha sido publicado exitosamente.");
            } else if (mensaje.contains("ComentarioReportado")) {
                log.warn("[ADMIN ALERTA] Un comentario ha sido reportado y requiere moderación.");
            }
        } catch (Exception e) {
            log.error("[NOTIFICACION ERROR] Error procesando evento de comentario: {}", e.getMessage());
        }
    }
}
