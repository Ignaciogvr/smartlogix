-- Agregar campos a producto_comentarios
ALTER TABLE producto_comentarios ADD COLUMN IF NOT EXISTS usuario_id VARCHAR(255);
ALTER TABLE producto_comentarios ADD COLUMN IF NOT EXISTS fecha_actualizacion TIMESTAMP;
ALTER TABLE producto_comentarios ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT TRUE;

-- Actualizar comentarios existentes (para los dummies creados en V10)
UPDATE producto_comentarios SET activo = TRUE WHERE activo IS NULL;

-- Asegurar restricción: 1 comentario por usuario por producto (solo para los activos)
-- PostgreSQL soporta índices únicos parciales. Esto permite tener múltiples comentarios 'inactivos' del mismo usuario, pero solo uno 'activo'.
CREATE UNIQUE INDEX IF NOT EXISTS idx_comentarios_producto_usuario 
ON producto_comentarios (producto_id, usuario_id) 
WHERE activo = TRUE;
