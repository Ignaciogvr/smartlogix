-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V2__add_additional_indexes.sql
-- Description: Additional indexes for performance optimization
-- ============================================

-- Índice simple para consultas de stock bajo
CREATE INDEX idx_productos_stock ON productos(stock);

-- Índice compuesto para consultas por categoría + estado
CREATE INDEX idx_productos_categoria_estado ON productos(categoria, estado);

-- Comentarios para documentación
COMMENT ON INDEX idx_productos_stock IS 'Optimiza consultas de productos con stock bajo (findByStockLessThan)';
COMMENT ON INDEX idx_productos_categoria_estado IS 'Optimiza consultas de catálogo filtrado por categoría y estado (findByCategoriaAndEstado)';
