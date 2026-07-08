-- V2__add_missing_columns_to_comentarios.sql
ALTER TABLE producto_comentarios
ADD COLUMN estado VARCHAR(50) DEFAULT 'PENDIENTE_MODERACION',
ADD COLUMN recomendado BOOLEAN DEFAULT FALSE,
ADD COLUMN reportes INTEGER DEFAULT 0,
ADD COLUMN pedido_id BIGINT;
