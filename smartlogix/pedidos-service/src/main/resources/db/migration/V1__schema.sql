-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V1__schema.sql
-- Description: Consolidated schema for orders, payments, carts, outbox
-- ============================================

CREATE TABLE pedidos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id VARCHAR(255),
    fecha TIMESTAMP,
    total DOUBLE PRECISION,
    estado VARCHAR(50),
    fecha_completado TIMESTAMP,
    requiere_logistica_inversa BOOLEAN DEFAULT FALSE,
    direccion_envio VARCHAR(500),
    telefono_contacto VARCHAR(20),
    notas_entrega TEXT,
    metodo_pago VARCHAR(50),
    notas_internas TEXT,
    
    CONSTRAINT chk_total_positivo CHECK (total IS NULL OR total >= 0),
    CONSTRAINT chk_estado_pedido_valido CHECK (
        estado IN ('PENDIENTE', 'PAGADO', 'EN_PREPARACION', 'ENVIADO', 'ENTREGADO', 'CANCELADO', 'CONFIRMADO')
    )
);

CREATE TABLE detalle_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT,
    producto_id BIGINT,
    cantidad INTEGER,
    precio DOUBLE PRECISION,
    nombre_producto VARCHAR(255),
    imagen_url VARCHAR(500),
    descripcion_snapshot TEXT,
    marca_snapshot VARCHAR(100),
    vendedor_id VARCHAR(255) DEFAULT 'default_vendor',
    
    CONSTRAINT fk_detalle_pedido_pedido 
        FOREIGN KEY (pedido_id) 
        REFERENCES pedidos(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad IS NULL OR cantidad > 0),
    CONSTRAINT chk_precio_positivo CHECK (precio IS NULL OR precio >= 0)
);

CREATE TABLE pedido_historial (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    estado_anterior VARCHAR(50),
    estado_nuevo VARCHAR(50) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    CONSTRAINT fk_pedido_historial_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
);

CREATE TABLE pagos (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    usuario_id VARCHAR(255) NOT NULL,
    monto DOUBLE PRECISION NOT NULL,
    moneda VARCHAR(10) DEFAULT 'CLP',
    metodo_pago VARCHAR(50),
    numero_referencia VARCHAR(255) UNIQUE,
    estado_pago VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    proveedor_pago VARCHAR(100),
    id_transaccion_proveedor VARCHAR(255),
    fecha_transaccion TIMESTAMP NOT NULL,
    fecha_confirmacion TIMESTAMP,
    motivo_rechazo TEXT,
    intentos_pago INTEGER DEFAULT 0,
    fecha_proximo_intento TIMESTAMP,
    
    CONSTRAINT fk_pagos_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT chk_monto_positivo CHECK (monto > 0),
    CONSTRAINT chk_estado_pago_valido CHECK (
        estado_pago IN ('PENDIENTE', 'PROCESANDO', 'COMPLETADO', 'CONFIRMADO', 'FALLIDO', 'RECHAZADO', 'REEMBOLSADO')
    )
);

CREATE TABLE devoluciones (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    detalle_del_pedido_id BIGINT NOT NULL,
    usuario_id VARCHAR(255) NOT NULL,
    estado_devolucion VARCHAR(50) NOT NULL DEFAULT 'INICIADA',
    motivo_devolucion VARCHAR(50) NOT NULL,
    descripcion_problema TEXT,
    cantidad_devuelta INTEGER DEFAULT 1,
    cantidad_aceptada INTEGER,
    foto_evidencia_url VARCHAR(500),
    fecha_solicitud TIMESTAMP NOT NULL,
    fecha_recepcion TIMESTAMP,
    fecha_inspeccion TIMESTAMP,
    fecha_resolucion TIMESTAMP,
    inspeccionado_por VARCHAR(255),
    observaciones_inspeccion TEXT,
    
    CONSTRAINT fk_devoluciones_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_devoluciones_detalle FOREIGN KEY (detalle_del_pedido_id) REFERENCES detalle_pedido(id) ON DELETE CASCADE,
    CONSTRAINT chk_estado_devolucion_valido CHECK (
        estado_devolucion IN ('INICIADA', 'SOLICITUD_RECIBIDA', 'EVALUANDO', 'APROBADA', 'RECHAZADA', 'EN_TRANSITO', 'RECIBIDA', 'COMPLETADA')
    ),
    CONSTRAINT chk_motivo_devolucion_valido CHECK (
        motivo_devolucion IN ('PRODUCTO_DANADO', 'NO_CORRESPONDE', 'CAMBIO_DE_IDEA', 'OTRO')
    )
);

CREATE TABLE carritos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
    total DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP NOT NULL
);

CREATE TABLE carrito_items (
    id BIGSERIAL PRIMARY KEY,
    carrito_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio DOUBLE PRECISION NOT NULL,
    
    CONSTRAINT fk_carrito_item_carrito
        FOREIGN KEY (carrito_id) 
        REFERENCES carritos(id)
        ON DELETE CASCADE
);

CREATE TABLE idempotency_keys (
    id BIGSERIAL PRIMARY KEY,
    idempotency_key VARCHAR(255) NOT NULL UNIQUE,
    response_body TEXT,
    response_status INTEGER,
    fecha_creacion TIMESTAMP NOT NULL
);

CREATE TABLE pedidos_outbox_events (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE pedidos_analytics_eventos (
    id BIGSERIAL PRIMARY KEY,
    tipo_evento VARCHAR(255) NOT NULL,
    payload TEXT,
    fecha_registro TIMESTAMP NOT NULL
);

-- Indexes
CREATE INDEX idx_pedidos_usuario_id ON pedidos(usuario_id);
CREATE INDEX idx_pedidos_estado ON pedidos(estado);
CREATE INDEX idx_pedidos_fecha ON pedidos(fecha DESC);
CREATE INDEX idx_pedidos_usuario_id_fecha ON pedidos(usuario_id, fecha DESC);
CREATE INDEX idx_pedidos_fecha_completado ON pedidos(fecha_completado);
CREATE INDEX idx_pedidos_requiere_logistica_inversa ON pedidos(requiere_logistica_inversa);
CREATE INDEX idx_pedidos_metodo_pago ON pedidos(metodo_pago);

CREATE INDEX idx_detalle_pedido_pedido_id ON detalle_pedido(pedido_id);
CREATE INDEX idx_detalle_pedido_producto_id ON detalle_pedido(producto_id);

CREATE INDEX idx_pedido_historial_pedido_id ON pedido_historial(pedido_id);
CREATE INDEX idx_pedido_historial_fecha ON pedido_historial(fecha);

CREATE INDEX idx_pagos_pedido_id ON pagos(pedido_id);
CREATE INDEX idx_pagos_estado ON pagos(estado_pago);
CREATE INDEX idx_pagos_usuario_id ON pagos(usuario_id);

CREATE INDEX idx_devoluciones_pedido_id ON devoluciones(pedido_id);
CREATE INDEX idx_devoluciones_estado ON devoluciones(estado_devolucion);
CREATE INDEX idx_devoluciones_usuario_id ON devoluciones(usuario_id);

CREATE INDEX idx_carritos_usuario ON carritos(usuario_id);
CREATE INDEX idx_carrito_items_carrito ON carrito_items(carrito_id);

CREATE INDEX idx_idempotency_key ON idempotency_keys(idempotency_key);
CREATE INDEX idx_pedidos_outbox_estado ON pedidos_outbox_events(estado);
CREATE INDEX idx_analytics_tipo ON pedidos_analytics_eventos(tipo_evento);

-- Comments
COMMENT ON TABLE pedidos IS 'Pedidos de clientes';
COMMENT ON COLUMN pedidos.usuario_id IS 'ID de usuario de Auth0 (string format: auth0|xxx)';
COMMENT ON COLUMN pedidos.estado IS 'Estado del ciclo de vida del pedido';
COMMENT ON COLUMN pedidos.fecha_completado IS 'Fecha cuando el pedido fue completado/entregado';
COMMENT ON COLUMN pedidos.requiere_logistica_inversa IS 'Indica si el pedido requiere logística de reversa (devoluciones)';
COMMENT ON COLUMN pedidos.direccion_envio IS 'Dirección completa de entrega del pedido';
COMMENT ON COLUMN pedidos.telefono_contacto IS 'Teléfono de contacto para la entrega';
COMMENT ON COLUMN pedidos.notas_entrega IS 'Notas o instrucciones especiales para la entrega';
COMMENT ON COLUMN pedidos.metodo_pago IS 'Método de pago seleccionado: TARJETA, TRANSFERENCIA, EFECTIVO, etc.';
COMMENT ON COLUMN pedidos.notas_internas IS 'Notas internas visibles solo para ADMIN/VENDEDOR';

COMMENT ON TABLE detalle_pedido IS 'Líneas de detalle de cada pedido (productos, cantidades, precios)';
COMMENT ON COLUMN detalle_pedido.precio IS 'Precio del producto al momento de la compra (snapshot)';
COMMENT ON COLUMN detalle_pedido.nombre_producto IS 'Snapshot del nombre del producto al momento de la compra';
COMMENT ON COLUMN detalle_pedido.imagen_url IS 'Snapshot de la imagen principal del producto al momento de la compra';
COMMENT ON COLUMN detalle_pedido.descripcion_snapshot IS 'Snapshot de la descripción del producto al momento de la compra';
COMMENT ON COLUMN detalle_pedido.marca_snapshot IS 'Snapshot de la marca del producto al momento de la compra';

COMMENT ON INDEX idx_pedidos_usuario_id_fecha IS 'Optimiza consultas de historial de pedidos del usuario ordenado por fecha (UI: Mis Pedidos)';
COMMENT ON TABLE pagos IS 'Registro de pagos y transacciones para cada pedido';
COMMENT ON TABLE devoluciones IS 'Solicitudes y procesamiento de devoluciones de productos (logística inversa)';
