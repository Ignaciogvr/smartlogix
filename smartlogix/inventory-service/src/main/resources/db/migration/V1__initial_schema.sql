-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V1__initial_schema.sql
-- Description: Initial schema for products and images
-- ============================================

-- Tabla principal: productos
CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion VARCHAR(1000),
    precio DOUBLE PRECISION NOT NULL,
    stock INTEGER NOT NULL,
    categoria VARCHAR(255) NOT NULL,
    rating_promedio DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    total_ratings INTEGER NOT NULL DEFAULT 0,
    cantidad_vendidos INTEGER NOT NULL DEFAULT 0,
    estado VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion TIMESTAMP,
    fecha_actualizacion TIMESTAMP,
    
    CONSTRAINT chk_precio_positivo CHECK (precio >= 0),
    CONSTRAINT chk_stock_no_negativo CHECK (stock >= 0),
    CONSTRAINT chk_rating_rango CHECK (rating_promedio >= 0 AND rating_promedio <= 5),
    CONSTRAINT chk_estado_valido CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

-- Tabla secundaria: producto_imagenes (ElementCollection)
CREATE TABLE producto_imagenes (
    producto_id BIGINT NOT NULL,
    imagen_url VARCHAR(500) NOT NULL,
    
    CONSTRAINT fk_producto_imagenes_producto 
        FOREIGN KEY (producto_id) 
        REFERENCES productos(id) 
        ON DELETE CASCADE
);

-- Índices para optimización de consultas
CREATE INDEX idx_productos_nombre ON productos(nombre);
CREATE INDEX idx_productos_categoria ON productos(categoria);
CREATE INDEX idx_productos_estado ON productos(estado);
CREATE INDEX idx_productos_precio ON productos(precio);
CREATE INDEX idx_producto_imagenes_producto_id ON producto_imagenes(producto_id);

-- Comentarios para documentación
COMMENT ON TABLE productos IS 'Catálogo de productos del inventario';
COMMENT ON COLUMN productos.version IS 'Versioning optimista para prevenir conflictos de concurrencia';
COMMENT ON COLUMN productos.cantidad_vendidos IS 'Contador histórico de unidades vendidas';
COMMENT ON TABLE producto_imagenes IS 'URLs de imágenes asociadas a productos (relación 1:N)';
