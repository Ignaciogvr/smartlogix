-- ============================================
-- SmartLogix - Usuarios Service
-- Migration: V2__add_additional_indexes.sql
-- Description: Additional indexes for performance optimization
-- ============================================

-- No se requieren índices adicionales en usuarios-service
-- Todos los campos consultados ya tienen índices en V1:
-- - auth0Id (UNIQUE + índice)
-- - email (UNIQUE + índice)
-- - estado (índice)

-- Este archivo existe solo para consistencia en versionado de Flyway
