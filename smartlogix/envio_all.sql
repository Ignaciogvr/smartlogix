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
ALTER TABLE envios ADD COLUMN vendedor_id VARCHAR(255) DEFAULT 'default_vendor';
ALTER TABLE envios DROP COLUMN vendedor_id;
ALTER TABLE envios ADD COLUMN chofer_id VARCHAR(255);
ALTER TABLE envios ADD COLUMN chofer_nombre VARCHAR(255);
-- Agregar columna fecha_actualizacion a la tabla envios
ALTER TABLE envios ADD COLUMN fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Actualizar registros existentes
UPDATE envios SET fecha_actualizacion = fecha_creacion WHERE fecha_actualizacion IS NULL;
-- ============================================
-- SmartLogix - Envio Service
-- Migration: V6__seed_test_data.sql
-- Description: Datos de prueba para envíos
-- ============================================

-- Envío 1: Pedido 1 - ENTREGADO
INSERT INTO envios (pedido_id, usuario_id, tracking_number, estado, direccion_destino, tipo_envio, fecha_creacion, fecha_estimada_entrega, chofer_id, chofer_nombre)
VALUES 
(1, 'auth0|cliente-test-001', 'SL-ENV-20250625-001', 'ENTREGADO', 'Av. Apoquindo 5678, Las Condes, Santiago', 'STANDARD', NOW() - INTERVAL '6 days', NOW() - INTERVAL '3 days', 'auth0|chofer-test-001', 'Pedro Silva')
ON CONFLICT DO NOTHING;

-- Historial Envío 1
INSERT INTO historial_envios (envio_id, estado, descripcion, fecha)
VALUES 
(1, 'PENDIENTE', 'Envío creado', NOW() - INTERVAL '6 days'),
(1, 'PREPARANDO', 'Pedido empaquetado', NOW() - INTERVAL '5 days'),
(1, 'EN_RUTA', 'Envío despachado', NOW() - INTERVAL '4 days'),
(1, 'ENTREGADO', 'Entregado al cliente', NOW() - INTERVAL '3 days');

-- Envío 2: Pedido 2 - PREPARANDO
INSERT INTO envios (pedido_id, usuario_id, tracking_number, estado, direccion_destino, tipo_envio, fecha_creacion, fecha_estimada_entrega, chofer_id, chofer_nombre)
VALUES 
(2, 'auth0|cliente-test-001', 'SL-ENV-20250629-002', 'PREPARANDO', 'Av. Apoquindo 5678, Las Condes, Santiago', 'EXPRESS', NOW() - INTERVAL '2 days', NOW() + INTERVAL '1 day', NULL, NULL)
ON CONFLICT DO NOTHING;

-- Historial Envío 2
INSERT INTO historial_envios (envio_id, estado, descripcion, fecha)
VALUES 
(2, 'PENDIENTE', 'Envío creado', NOW() - INTERVAL '2 days'),
(2, 'PREPARANDO', 'En proceso de empaquetado', NOW() - INTERVAL '1 day');

-- Envío 3: Pedido 4 - EN_RUTA
INSERT INTO envios (pedido_id, usuario_id, tracking_number, estado, direccion_destino, tipo_envio, fecha_creacion, fecha_estimada_entrega, chofer_id, chofer_nombre)
VALUES 
(4, 'auth0|cliente-test-003', 'SL-ENV-20250628-003', 'EN_RUTA', 'Av. Vicuña Mackenna 1111, La Florida, Santiago', 'STANDARD', NOW() - INTERVAL '3 days', NOW(), 'auth0|chofer-test-001', 'Pedro Silva')
ON CONFLICT DO NOTHING;

-- Historial Envío 3
INSERT INTO historial_envios (envio_id, estado, descripcion, fecha)
VALUES 
(3, 'PENDIENTE', 'Envío creado', NOW() - INTERVAL '3 days'),
(3, 'PREPARANDO', 'Pedido empaquetado', NOW() - INTERVAL '2 days'),
(3, 'EN_RUTA', 'En camino al destino', NOW() - INTERVAL '1 day');

-- Envío 4: Pedido 5 - PENDIENTE (reciente)
INSERT INTO envios (pedido_id, usuario_id, tracking_number, estado, direccion_destino, tipo_envio, fecha_creacion, fecha_estimada_entrega, chofer_id, chofer_nombre)
VALUES 
(5, 'auth0|cliente-test-002', 'SL-ENV-20250701-004', 'PENDIENTE', 'Calle Nueva 321, Ñuñoa, Santiago', 'SAME_DAY', NOW() - INTERVAL '2 hours', NOW() + INTERVAL '6 hours', NULL, NULL)
ON CONFLICT DO NOTHING;

-- Historial Envío 4
INSERT INTO historial_envios (envio_id, estado, descripcion, fecha)
VALUES 
(4, 'PENDIENTE', 'Envío creado, esperando confirmación', NOW() - INTERVAL '2 hours');

COMMENT ON TABLE envios IS 'Tabla con datos de prueba para desarrollo y testing';
-- ============================================
-- SmartLogix - Envio Service
-- Migration: V7__transportistas_y_tracking_avanzado.sql
-- Description: Transport companies, advanced tracking, and logistics optimization
-- ============================================

-- Tabla: transportistas (empresas de logística)
CREATE TABLE transportistas (
    id BIGSERIAL PRIMARY KEY,
    nombre_transportista VARCHAR(255) NOT NULL UNIQUE,
    rut_transportista VARCHAR(20) UNIQUE,
    codigo_integracion VARCHAR(100),
    email_contacto VARCHAR(255),
    telefono VARCHAR(20),
    sitio_web VARCHAR(500),
    api_endpoint VARCHAR(500),
    api_key VARCHAR(500),
    estado_transportista VARCHAR(50) DEFAULT 'ACTIVO',
    comision_porcentaje DOUBLE PRECISION DEFAULT 0,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla: rutas_transporte (rutas configuradas por transportista)
CREATE TABLE rutas_transporte (
    id BIGSERIAL PRIMARY KEY,
    transportista_id BIGINT NOT NULL,
    zona_origen VARCHAR(100),
    zona_destino VARCHAR(100),
    tiempo_promedio_dias INTEGER,
    costo_base DOUBLE PRECISION,
    costo_por_kg DOUBLE PRECISION DEFAULT 0,
    activa BOOLEAN DEFAULT TRUE,
    
    CONSTRAINT fk_rutas_transportista 
        FOREIGN KEY (transportista_id) 
        REFERENCES transportistas(id) 
        ON DELETE CASCADE
);

-- Tabla: detalle_envio_mejorado (campos adicionales para rastreo)
CREATE TABLE detalle_envio_mejorado (
    id BIGSERIAL PRIMARY KEY,
    envio_id BIGINT NOT NULL,
    destinatario_nombre VARCHAR(255),
    destinatario_telefono VARCHAR(20),
    destinatario_email VARCHAR(255),
    direccion_completa VARCHAR(1000),
    coordenadas_latitude DOUBLE PRECISION,
    coordenadas_longitude DOUBLE PRECISION,
    instrucciones_especiales VARCHAR(500),
    requiere_firma BOOLEAN DEFAULT FALSE,
    intentos_entrega INTEGER DEFAULT 0,
    fotografia_entrega_url VARCHAR(500),
    nombre_receptor VARCHAR(255),
    fecha_entrega_real TIMESTAMP,
    
    CONSTRAINT fk_detalle_envio 
        FOREIGN KEY (envio_id) 
        REFERENCES envios(id) 
        ON DELETE CASCADE
);

-- Tabla: eventos_tracking_detallados (geolocalización y eventos)
CREATE TABLE eventos_tracking_detallados (
    id BIGSERIAL PRIMARY KEY,
    envio_id BIGINT NOT NULL,
    tipo_evento VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    ubicacion VARCHAR(255),
    latitud DOUBLE PRECISION,
    longitud DOUBLE PRECISION,
    timestamp_evento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    registrado_por VARCHAR(255),
    foto_evento_url VARCHAR(500),
    
    CONSTRAINT fk_eventos_tracking_envio 
        FOREIGN KEY (envio_id) 
        REFERENCES envios(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT chk_tipo_evento_valido CHECK (
        tipo_evento IN ('PREPARACION', 'RECOGIDA', 'EN_TRANSITO', 'LLEGADA_HUB', 'SALIDA_HUB', 'EN_RUTA_DELIVERY', 
                        'INTENTO_FALLIDO', 'ENTREGADO', 'DEVOLUCION_INICIADA', 'CANCELADO', 'OTRO')
    )
);

-- Tabla: intentos_entrega (para rastrear intentos fallidos)
CREATE TABLE intentos_entrega (
    id BIGSERIAL PRIMARY KEY,
    envio_id BIGINT NOT NULL,
    numero_intento INTEGER,
    fecha_intento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    motivo_fallo VARCHAR(255),
    foto_domicilio_url VARCHAR(500),
    proxima_fecha_reintento TIMESTAMP,
    observaciones VARCHAR(500),
    
    CONSTRAINT fk_intentos_envio 
        FOREIGN KEY (envio_id) 
        REFERENCES envios(id) 
        ON DELETE CASCADE
);

-- Tabla: reclamos_logistica (para problemas en envío)
CREATE TABLE reclamos_logistica (
    id BIGSERIAL PRIMARY KEY,
    envio_id BIGINT NOT NULL,
    tipo_reclamo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(1000),
    foto_evidencia_url VARCHAR(500),
    estado_reclamo VARCHAR(50) DEFAULT 'REGISTRADO',
    fecha_reclamo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_resolucion TIMESTAMP,
    monto_compensacion DOUBLE PRECISION,
    resuelto_por VARCHAR(255),
    
    CONSTRAINT fk_reclamos_envio 
        FOREIGN KEY (envio_id) 
        REFERENCES envios(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT chk_tipo_reclamo_valido CHECK (
        tipo_reclamo IN ('PERDIDA', 'DANO_EMBALAJE', 'RETRASO', 'MAL_ESTADO', 'NO_ENTREGADO', 'OTRO')
    )
);

-- Tabla: asignacion_chofer (quién es responsable de la entrega)
CREATE TABLE asignacion_chofer (
    id BIGSERIAL PRIMARY KEY,
    envio_id BIGINT NOT NULL,
    chofer_id VARCHAR(255) NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_recogida TIMESTAMP,
    fecha_entrega TIMESTAMP,
    estado_asignacion VARCHAR(50) DEFAULT 'ASIGNADO',
    calificacion_chofer_estrellas INTEGER,
    comentario_cliente VARCHAR(500),
    
    CONSTRAINT fk_asignacion_envio 
        FOREIGN KEY (envio_id) 
        REFERENCES envios(id) 
        ON DELETE CASCADE
);

-- Tabla: optimizacion_rutas (para análisis y mejoras)
CREATE TABLE optimizacion_rutas (
    id BIGSERIAL PRIMARY KEY,
    fecha_ruta TIMESTAMP,
    zona_cobertura VARCHAR(100),
    cantidad_envios INTEGER,
    distancia_total_km DOUBLE PRECISION,
    tiempo_total_minutos INTEGER,
    efficiency_score DOUBLE PRECISION,
    costo_estimado DOUBLE PRECISION,
    observaciones VARCHAR(500)
);

-- Índices para rendimiento
CREATE INDEX idx_transportistas_estado ON transportistas(estado_transportista);
CREATE INDEX idx_rutas_transportista_id ON rutas_transporte(transportista_id);
CREATE INDEX idx_rutas_destino ON rutas_transporte(zona_destino);
CREATE INDEX idx_detalle_envio_envio_id ON detalle_envio_mejorado(envio_id);
CREATE INDEX idx_eventos_tracking_envio_id ON eventos_tracking_detallados(envio_id);
CREATE INDEX idx_eventos_tracking_fecha ON eventos_tracking_detallados(timestamp_evento DESC);
CREATE INDEX idx_intentos_envio_id ON intentos_entrega(envio_id);
CREATE INDEX idx_reclamos_envio_id ON reclamos_logistica(envio_id);
CREATE INDEX idx_reclamos_estado ON reclamos_logistica(estado_reclamo);
CREATE INDEX idx_asignacion_chofer_id ON asignacion_chofer(chofer_id);
CREATE INDEX idx_asignacion_envio_id ON asignacion_chofer(envio_id);

-- Comentarios de documentación
COMMENT ON TABLE transportistas IS 'Empresas de transporte/logística contratadas (DHL, FedEx, etc.)';
COMMENT ON TABLE detalle_envio_mejorado IS 'Información del destinatario y detalles mejorados de entrega';
COMMENT ON TABLE eventos_tracking_detallados IS 'Geolocalización y eventos de la ruta en tiempo real';
COMMENT ON TABLE intentos_entrega IS 'Historial de intentos fallidos de entrega';
COMMENT ON TABLE reclamos_logistica IS 'Gestión de problemas durante la logística';
COMMENT ON TABLE asignacion_chofer IS 'Seguimiento del chofer asignado al envío';
COMMENT ON TABLE optimizacion_rutas IS 'Datos para análisis y optimización de rutas logísticas';
