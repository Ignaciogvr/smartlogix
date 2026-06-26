-- ============================================
-- SmartLogix - Envio Service
-- Migration: V1__initial_schema.sql
-- Description: Initial schema for shipments and shipment history
-- ============================================

-- Tabla principal: envios
CREATE TABLE envios (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT,
    usuario_id VARCHAR(255),
    tracking_number VARCHAR(255),
    estado VARCHAR(50),
    direccion_destino VARCHAR(500),
    tipo_envio VARCHAR(50),
    fecha_creacion TIMESTAMP,
    fecha_estimada_entrega TIMESTAMP,
    
    CONSTRAINT chk_estado_envio_valido CHECK (
        estado IN ('PENDIENTE', 'PREPARANDO', 'EN_RUTA', 'ENTREGADO', 'CANCELADO')
    ),
    CONSTRAINT chk_tipo_envio_valido CHECK (
        tipo_envio IN ('STANDARD', 'EXPRESS', 'SAME_DAY')
    )
);

-- Tabla de auditoría: historial_envios
CREATE TABLE historial_envios (
    id BIGSERIAL PRIMARY KEY,
    envio_id BIGINT,
    estado VARCHAR(50),
    descripcion VARCHAR(1000),
    fecha TIMESTAMP,
    
    CONSTRAINT fk_historial_envio 
        FOREIGN KEY (envio_id) 
        REFERENCES envios(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT chk_estado_historial_valido CHECK (
        estado IN ('PENDIENTE', 'PREPARANDO', 'EN_RUTA', 'ENTREGADO', 'CANCELADO')
    )
);

-- Índices para optimización de consultas
CREATE INDEX idx_envios_pedido_id ON envios(pedido_id);
CREATE INDEX idx_envios_usuario_id ON envios(usuario_id);
CREATE INDEX idx_envios_tracking_number ON envios(tracking_number);
CREATE INDEX idx_envios_estado ON envios(estado);
CREATE INDEX idx_envios_fecha_creacion ON envios(fecha_creacion DESC);
CREATE INDEX idx_historial_envios_envio_id ON historial_envios(envio_id);
CREATE INDEX idx_historial_envios_fecha ON historial_envios(fecha DESC);

-- Comentarios para documentación
COMMENT ON TABLE envios IS 'Gestión de envíos y tracking logístico';
COMMENT ON COLUMN envios.pedido_id IS 'Referencia al pedido en pedidos-service (sin FK física)';
COMMENT ON COLUMN envios.tracking_number IS 'Código de seguimiento único del envío';
COMMENT ON COLUMN envios.usuario_id IS 'ID de usuario de Auth0 para consultas directas';
COMMENT ON TABLE historial_envios IS 'Auditoría de cambios de estado de envíos';
COMMENT ON COLUMN historial_envios.fecha IS 'Timestamp del cambio de estado';
