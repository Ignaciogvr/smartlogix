-- ============================================
-- SmartLogix - Usuarios Service
-- Migration: V1__initial_schema.sql
-- Description: Initial schema for users
-- ============================================

-- Tabla principal: usuarios
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    auth0_id VARCHAR(255) UNIQUE,
    estado VARCHAR(50) DEFAULT 'ACTIVO',
    
    CONSTRAINT chk_estado_usuario_valido CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

-- Índices para optimización de consultas
CREATE INDEX idx_usuarios_auth0_id ON usuarios(auth0_id);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_estado ON usuarios(estado);

-- Comentarios para documentación
COMMENT ON TABLE usuarios IS 'Usuarios del sistema sincronizados con Auth0';
COMMENT ON COLUMN usuarios.auth0_id IS 'Subject claim del JWT de Auth0 (unique identifier)';
COMMENT ON COLUMN usuarios.email IS 'Email del usuario (puede ser null si no está en el token)';
COMMENT ON COLUMN usuarios.estado IS 'Estado lógico del usuario (soft delete)';
