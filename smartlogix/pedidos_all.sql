-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V1__initial_schema.sql
-- Description: Initial schema for orders and order details
-- ============================================

-- Tabla principal: pedidos
CREATE TABLE pedidos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id VARCHAR(255),
    fecha TIMESTAMP,
    total DOUBLE PRECISION,
    estado VARCHAR(50),
    
    CONSTRAINT chk_total_positivo CHECK (total IS NULL OR total >= 0),
    CONSTRAINT chk_estado_pedido_valido CHECK (
        estado IN ('PENDIENTE', 'PAGADO', 'EN_PREPARACION', 'ENVIADO', 'ENTREGADO', 'CANCELADO')
    )
);

-- Tabla de detalles: detalle_pedido
CREATE TABLE detalle_pedido (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT,
    producto_id BIGINT,
    cantidad INTEGER,
    precio DOUBLE PRECISION,
    
    CONSTRAINT fk_detalle_pedido_pedido 
        FOREIGN KEY (pedido_id) 
        REFERENCES pedidos(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad IS NULL OR cantidad > 0),
    CONSTRAINT chk_precio_positivo CHECK (precio IS NULL OR precio >= 0)
);

-- Índices para optimización de consultas
CREATE INDEX idx_pedidos_usuario_id ON pedidos(usuario_id);
CREATE INDEX idx_pedidos_estado ON pedidos(estado);
CREATE INDEX idx_pedidos_fecha ON pedidos(fecha DESC);
CREATE INDEX idx_detalle_pedido_pedido_id ON detalle_pedido(pedido_id);
CREATE INDEX idx_detalle_pedido_producto_id ON detalle_pedido(producto_id);

-- Comentarios para documentación
COMMENT ON TABLE pedidos IS 'Pedidos de clientes';
COMMENT ON COLUMN pedidos.usuario_id IS 'ID de usuario de Auth0 (string format: auth0|xxx)';
COMMENT ON COLUMN pedidos.estado IS 'Estado del ciclo de vida del pedido';
COMMENT ON TABLE detalle_pedido IS 'Líneas de detalle de cada pedido (productos, cantidades, precios)';
COMMENT ON COLUMN detalle_pedido.precio IS 'Precio del producto al momento de la compra (snapshot)';
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V10__create_analytics_eventos_table.sql
-- Description: Create table for analytics events
-- ============================================

CREATE TABLE pedidos_analytics_eventos (
    id BIGSERIAL PRIMARY KEY,
    tipo_evento VARCHAR(255) NOT NULL,
    payload TEXT,
    fecha_registro TIMESTAMP NOT NULL
);

CREATE INDEX idx_analytics_tipo ON pedidos_analytics_eventos(tipo_evento);
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V11__seed_test_data.sql
-- Description: Datos de prueba para pedidos
-- ============================================

-- Pedido 1: Cliente Maria - ENTREGADO
INSERT INTO pedidos (usuario_id, fecha, total, estado)
VALUES 
('auth0|cliente-test-001', NOW() - INTERVAL '7 days', 449990.00, 'ENTREGADO')
ON CONFLICT DO NOTHING;

-- Detalle Pedido 1
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio)
VALUES 
((SELECT id FROM pedidos WHERE usuario_id = 'auth0|cliente-test-001' AND estado = 'ENTREGADO' LIMIT 1), 1, 1, 299990.00),
((SELECT id FROM pedidos WHERE usuario_id = 'auth0|cliente-test-001' AND estado = 'ENTREGADO' LIMIT 1), 2, 1, 150000.00);

-- Pedido 2: Cliente Maria - EN_PREPARACION
INSERT INTO pedidos (usuario_id, fecha, total, estado)
VALUES 
('auth0|cliente-test-001', NOW() - INTERVAL '2 days', 899990.00, 'EN_PREPARACION')
ON CONFLICT DO NOTHING;

-- Detalle Pedido 2
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio)
VALUES 
((SELECT id FROM pedidos WHERE usuario_id = 'auth0|cliente-test-001' AND estado = 'EN_PREPARACION' LIMIT 1), 5, 1, 899990.00);

-- Pedido 3: Cliente Carlos - PENDIENTE
INSERT INTO pedidos (usuario_id, fecha, total, estado)
VALUES 
('auth0|cliente-test-002', NOW() - INTERVAL '1 day', 189990.00, 'PENDIENTE')
ON CONFLICT DO NOTHING;

-- Detalle Pedido 3
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, nombre_producto)
VALUES 
((SELECT id FROM pedidos WHERE usuario_id = 'auth0|cliente-test-002' AND estado = 'PENDIENTE' LIMIT 1), 3, 1, 89990.00, 'Webcam Full HD'),
((SELECT id FROM pedidos WHERE usuario_id = 'auth0|cliente-test-002' AND estado = 'PENDIENTE' LIMIT 1), 4, 2, 50000.00, 'Cable HDMI 2m');

-- Pedido 4: Cliente Laura - ENVIADO
INSERT INTO pedidos (usuario_id, fecha, total, estado)
VALUES 
('auth0|cliente-test-003', NOW() - INTERVAL '3 days', 1299990.00, 'ENVIADO')
ON CONFLICT DO NOTHING;

-- Detalle Pedido 4
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, nombre_producto)
VALUES 
((SELECT id FROM pedidos WHERE usuario_id = 'auth0|cliente-test-003' AND estado = 'ENVIADO' LIMIT 1), 10, 1, 1299990.00, 'Laptop Gaming 15.6');

-- Pedido 5: Cliente Carlos - CONFIRMADO (reciente)
INSERT INTO pedidos (usuario_id, fecha, total, estado)
VALUES 
('auth0|cliente-test-002', NOW() - INTERVAL '3 hours', 349990.00, 'CONFIRMADO')
ON CONFLICT DO NOTHING;

-- Detalle Pedido 5
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, nombre_producto)
VALUES 
((SELECT id FROM pedidos WHERE usuario_id = 'auth0|cliente-test-002' AND estado = 'CONFIRMADO' LIMIT 1), 6, 1, 199990.00, 'Auriculares Bluetooth'),
((SELECT id FROM pedidos WHERE usuario_id = 'auth0|cliente-test-002' AND estado = 'CONFIRMADO' LIMIT 1), 7, 1, 150000.00, 'Parlante Portatil');

-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V12__add_new_logistica_fields.sql
-- Description: Add new fields for logistics support and create payment/returns tables
-- ============================================

-- Agregar campos nuevos a la tabla pedidos
ALTER TABLE pedidos ADD COLUMN IF NOT EXISTS fecha_completado TIMESTAMP;
ALTER TABLE pedidos ADD COLUMN IF NOT EXISTS requiere_logistica_inversa BOOLEAN DEFAULT FALSE;

-- Crear tabla de pagos (si no existe)
CREATE TABLE IF NOT EXISTS pagos (
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
        estado_pago IN ('PENDIENTE', 'PROCESANDO', 'COMPLETADO', 'FALLIDO', 'RECHAZADO', 'REEMBOLSADO')
    )
);

-- Crear tabla de devoluciones (si no existe)
CREATE TABLE IF NOT EXISTS devoluciones (
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

-- Crear índices para optimización
CREATE INDEX IF NOT EXISTS idx_pedidos_fecha_completado ON pedidos(fecha_completado);
CREATE INDEX IF NOT EXISTS idx_pedidos_requiere_logistica_inversa ON pedidos(requiere_logistica_inversa);
CREATE INDEX IF NOT EXISTS idx_pagos_pedido_id ON pagos(pedido_id);
CREATE INDEX IF NOT EXISTS idx_pagos_estado ON pagos(estado_pago);
CREATE INDEX IF NOT EXISTS idx_pagos_usuario_id ON pagos(usuario_id);
CREATE INDEX IF NOT EXISTS idx_devoluciones_pedido_id ON devoluciones(pedido_id);
CREATE INDEX IF NOT EXISTS idx_devoluciones_estado ON devoluciones(estado_devolucion);
CREATE INDEX IF NOT EXISTS idx_devoluciones_usuario_id ON devoluciones(usuario_id);

-- Comentarios para documentación
COMMENT ON TABLE pagos IS 'Registro de pagos y transacciones para cada pedido';
COMMENT ON TABLE devoluciones IS 'Solicitudes y procesamiento de devoluciones de productos (logística inversa)';
COMMENT ON COLUMN pedidos.fecha_completado IS 'Fecha cuando el pedido fue completado/entregado';
COMMENT ON COLUMN pedidos.requiere_logistica_inversa IS 'Indica si el pedido requiere logística de reversa (devoluciones)';

-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V13__add_checkout_fields.sql
-- Description: Add checkout fields to pedidos table
-- Author: System
-- Date: 2026-07-04
-- ============================================

-- Agregar campos de checkout a la tabla pedidos
ALTER TABLE pedidos ADD COLUMN IF NOT EXISTS direccion_envio VARCHAR(500);
ALTER TABLE pedidos ADD COLUMN IF NOT EXISTS telefono_contacto VARCHAR(20);
ALTER TABLE pedidos ADD COLUMN IF NOT EXISTS notas_entrega TEXT;
ALTER TABLE pedidos ADD COLUMN IF NOT EXISTS metodo_pago VARCHAR(50);

-- Crear índice para método de pago (para reportes y filtros)
CREATE INDEX IF NOT EXISTS idx_pedidos_metodo_pago ON pedidos(metodo_pago);

-- Comentarios para documentación
COMMENT ON COLUMN pedidos.direccion_envio IS 'Dirección completa de entrega del pedido';
COMMENT ON COLUMN pedidos.telefono_contacto IS 'Teléfono de contacto para la entrega';
COMMENT ON COLUMN pedidos.notas_entrega IS 'Notas o instrucciones especiales para la entrega';
COMMENT ON COLUMN pedidos.metodo_pago IS 'Método de pago seleccionado: TARJETA, TRANSFERENCIA, EFECTIVO, etc.';
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V15__add_snapshot_to_detalle.sql
-- Description: Add product snapshot fields to detalle_pedido
--              to freeze name, image and description at purchase time
-- ============================================

ALTER TABLE detalle_pedido ADD COLUMN IF NOT EXISTS nombre_producto VARCHAR(255);
ALTER TABLE detalle_pedido ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(500);
ALTER TABLE detalle_pedido ADD COLUMN IF NOT EXISTS descripcion_snapshot TEXT;
ALTER TABLE detalle_pedido ADD COLUMN IF NOT EXISTS marca_snapshot VARCHAR(100);

COMMENT ON COLUMN detalle_pedido.nombre_producto IS 'Snapshot del nombre del producto al momento de la compra';
COMMENT ON COLUMN detalle_pedido.imagen_url IS 'Snapshot de la imagen principal del producto al momento de la compra';
COMMENT ON COLUMN detalle_pedido.descripcion_snapshot IS 'Snapshot de la descripción del producto al momento de la compra';
COMMENT ON COLUMN detalle_pedido.marca_snapshot IS 'Snapshot de la marca del producto al momento de la compra';
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V16__add_notas_internas.sql
-- Description: Add notasInternas column to pedidos table for internal admin notes
-- Author: System
-- Date: 2026-07-05
-- ============================================

-- Add internal notes column (visible only to admin/vendedor)
ALTER TABLE pedidos ADD COLUMN IF NOT EXISTS notas_internas TEXT;

-- Optional index for search (if needed in future)
-- CREATE INDEX IF NOT EXISTS idx_pedidos_notas_internas ON pedidos(notas_internas);

-- Comment for documentation
COMMENT ON COLUMN pedidos.notas_internas IS 'Notas internas visibles solo para ADMIN/VENDEDOR';
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V2__add_additional_indexes.sql
-- Description: Additional indexes for performance optimization
-- ============================================

-- Índice compuesto para historial de pedidos del usuario ordenado por fecha
CREATE INDEX idx_pedidos_usuario_id_fecha ON pedidos(usuario_id, fecha DESC);

-- Comentarios para documentación
COMMENT ON INDEX idx_pedidos_usuario_id_fecha IS 'Optimiza consultas de historial de pedidos del usuario ordenado por fecha (UI: Mis Pedidos)';
CREATE TABLE pedido_historial (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    estado_anterior VARCHAR(50),
    estado_nuevo VARCHAR(50) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    CONSTRAINT fk_pedido_historial_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
);

CREATE INDEX idx_pedido_historial_pedido_id ON pedido_historial(pedido_id);
CREATE INDEX idx_pedido_historial_fecha ON pedido_historial(fecha);
ALTER TABLE detalle_pedido ADD COLUMN vendedor_id VARCHAR(255) DEFAULT 'default_vendor';
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V5__create_carritos_table.sql
-- Description: Create carritos table
-- ============================================

CREATE TABLE carritos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
    total DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_actualizacion TIMESTAMP NOT NULL
);

CREATE INDEX idx_carritos_usuario ON carritos(usuario_id);
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V6__create_carrito_items_table.sql
-- Description: Create carrito_items table
-- ============================================

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

CREATE INDEX idx_carrito_items_carrito ON carrito_items(carrito_id);
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V7__create_idempotency_keys_table.sql
-- Description: Create idempotency_keys table
-- ============================================

CREATE TABLE idempotency_keys (
    id BIGSERIAL PRIMARY KEY,
    idempotency_key VARCHAR(255) NOT NULL UNIQUE,
    response_body TEXT,
    response_status INTEGER,
    fecha_creacion TIMESTAMP NOT NULL
);

CREATE INDEX idx_idempotency_key ON idempotency_keys(idempotency_key);
-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V9__create_outbox_events_table.sql
-- Description: Create outbox table for reliable messaging
-- ============================================

CREATE TABLE pedidos_outbox_events (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

CREATE INDEX idx_pedidos_outbox_estado ON pedidos_outbox_events(estado);
