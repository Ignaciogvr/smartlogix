-- ============================================
-- SmartLogix - Envio Service
-- Migration: V1__schema.sql
-- Description: Consolidated initial schema for shipments and logistics
-- ============================================

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
    chofer_id VARCHAR(255),
    chofer_nombre VARCHAR(255),
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_estado_envio_valido CHECK (
        estado IN ('PENDIENTE', 'PREPARANDO', 'ASIGNADO', 'EN_RUTA', 'ENTREGADO', 'CANCELADO')
    ),
    CONSTRAINT chk_tipo_envio_valido CHECK (
        tipo_envio IN ('STANDARD', 'EXPRESS', 'SAME_DAY')
    )
);

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
        estado IN ('PENDIENTE', 'PREPARANDO', 'ASIGNADO', 'EN_RUTA', 'ENTREGADO', 'CANCELADO')
    )
);

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

-- Índices
CREATE INDEX idx_envios_pedido_id ON envios(pedido_id);
CREATE INDEX idx_envios_usuario_id ON envios(usuario_id);
CREATE INDEX idx_envios_tracking_number ON envios(tracking_number);
CREATE INDEX idx_envios_estado ON envios(estado);
CREATE INDEX idx_envios_fecha_creacion ON envios(fecha_creacion DESC);

CREATE INDEX idx_historial_envios_envio_id ON historial_envios(envio_id);
CREATE INDEX idx_historial_envios_fecha ON historial_envios(fecha DESC);

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

-- Comentarios
COMMENT ON TABLE envios IS 'Gestión de envíos y tracking logístico';
COMMENT ON COLUMN envios.pedido_id IS 'Referencia al pedido en pedidos-service (sin FK física)';
COMMENT ON COLUMN envios.tracking_number IS 'Código de seguimiento único del envío';
COMMENT ON COLUMN envios.usuario_id IS 'ID de usuario de Auth0 para consultas directas';

COMMENT ON TABLE historial_envios IS 'Auditoría de cambios de estado de envíos';
COMMENT ON COLUMN historial_envios.fecha IS 'Timestamp del cambio de estado';

COMMENT ON TABLE transportistas IS 'Empresas de transporte/logística contratadas (DHL, FedEx, etc.)';
COMMENT ON TABLE detalle_envio_mejorado IS 'Información del destinatario y detalles mejorados de entrega';
COMMENT ON TABLE eventos_tracking_detallados IS 'Geolocalización y eventos de la ruta en tiempo real';
COMMENT ON TABLE intentos_entrega IS 'Historial de intentos fallidos de entrega';
COMMENT ON TABLE reclamos_logistica IS 'Gestión de problemas durante la logística';
COMMENT ON TABLE asignacion_chofer IS 'Seguimiento del chofer asignado al envío';
COMMENT ON TABLE optimizacion_rutas IS 'Datos para análisis y optimización de rutas logísticas';
