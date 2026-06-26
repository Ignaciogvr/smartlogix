-- ============================================
-- SmartLogix - Pedidos Service
-- Migration: V2__add_additional_indexes.sql
-- Description: Additional indexes for performance optimization
-- ============================================

-- Índice compuesto para historial de pedidos del usuario ordenado por fecha
CREATE INDEX idx_pedidos_usuario_id_fecha ON pedidos(usuario_id, fecha DESC);

-- Comentarios para documentación
COMMENT ON INDEX idx_pedidos_usuario_id_fecha IS 'Optimiza consultas de historial de pedidos del usuario ordenado por fecha (UI: Mis Pedidos)';
