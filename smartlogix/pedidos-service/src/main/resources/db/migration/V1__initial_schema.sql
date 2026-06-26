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
