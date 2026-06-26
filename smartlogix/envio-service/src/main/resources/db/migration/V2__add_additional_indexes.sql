-- ============================================
-- SmartLogix - Envio Service
-- Migration: V2__add_additional_indexes.sql
-- Description: Additional indexes for performance optimization
-- ============================================

-- No se requieren índices adicionales en envio-service
-- Las consultas existentes ya están cubiertas por índices en V1:
-- - findByTrackingNumber → idx_envios_trackingNumber
-- - findByPedidoId → idx_envios_pedidoId
-- - findByUsuarioId → idx_envios_usuarioId
-- - findByEstado → idx_envios_estado

-- Este archivo existe solo para consistencia en versionado de Flyway
