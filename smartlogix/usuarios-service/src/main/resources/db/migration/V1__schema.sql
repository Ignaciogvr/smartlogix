-- ============================================
-- SmartLogix - Usuarios Service
-- Migration: V1__initial_schema.sql
-- Description: Consolidated initial schema for users
-- ============================================

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    auth0_id VARCHAR(255) UNIQUE,
    estado VARCHAR(50) DEFAULT 'ACTIVO',
    rol VARCHAR(20) DEFAULT 'CLIENTE',
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    genero VARCHAR(20),
    rut VARCHAR(20) UNIQUE,
    verificado_kyc BOOLEAN DEFAULT FALSE,
    documento_verificacion VARCHAR(500),
    nivel_cliente VARCHAR(50) DEFAULT 'REGULAR',
    puntos_lealtad INTEGER DEFAULT 0,
    fecha_suspension TIMESTAMP,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_estado_usuario_valido CHECK (estado IN ('ACTIVO', 'INACTIVO', 'SUSPENDIDO'))
);

CREATE TABLE direcciones_usuarios (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_direccion VARCHAR(50) NOT NULL DEFAULT 'ENVIO',
    calle VARCHAR(255) NOT NULL,
    numero VARCHAR(50),
    departamento VARCHAR(100),
    comuna VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    region VARCHAR(100),
    codigo_postal VARCHAR(20),
    pais VARCHAR(100) DEFAULT 'Chile',
    telefono VARCHAR(20),
    notas VARCHAR(500),
    es_predeterminada BOOLEAN DEFAULT FALSE,
    activa BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_direcciones_usuarios 
        FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT chk_tipo_direccion_valido CHECK (
        tipo_direccion IN ('ENVIO', 'FACTURACION', 'RETIRO')
    )
);

CREATE TABLE auditoria_usuarios (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    accion VARCHAR(100) NOT NULL,
    tabla_afectada VARCHAR(100),
    valores_anteriores VARCHAR(2000),
    valores_nuevos VARCHAR(2000),
    usuario_realizador VARCHAR(255),
    fecha_accion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_origin VARCHAR(50),
    
    CONSTRAINT fk_auditoria_usuarios 
        FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE
);

CREATE INDEX idx_usuarios_auth0_id ON usuarios(auth0_id);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_estado ON usuarios(estado);
CREATE INDEX idx_usuarios_rut ON usuarios(rut);
CREATE INDEX idx_usuarios_verificado_kyc ON usuarios(verificado_kyc);
CREATE INDEX idx_usuarios_nivel_cliente ON usuarios(nivel_cliente);
CREATE INDEX idx_usuarios_fecha_suspension ON usuarios(fecha_suspension);

CREATE INDEX idx_direcciones_usuario_id ON direcciones_usuarios(usuario_id);
CREATE INDEX idx_direcciones_es_predeterminada ON direcciones_usuarios(usuario_id, es_predeterminada);

CREATE INDEX idx_auditoria_usuario_id ON auditoria_usuarios(usuario_id);
CREATE INDEX idx_auditoria_fecha ON auditoria_usuarios(fecha_accion DESC);

COMMENT ON TABLE usuarios IS 'Usuarios del sistema sincronizados con Auth0';
COMMENT ON COLUMN usuarios.auth0_id IS 'Subject claim del JWT de Auth0 (unique identifier)';
COMMENT ON COLUMN usuarios.email IS 'Email del usuario (puede ser null si no está en el token)';
COMMENT ON COLUMN usuarios.estado IS 'Estado lógico del usuario (soft delete)';
COMMENT ON COLUMN usuarios.verificado_kyc IS 'Know Your Customer - verificación de identidad completada';
COMMENT ON COLUMN usuarios.nivel_cliente IS 'REGULAR, VIP, PREMIUM, INFLUENCER';
COMMENT ON COLUMN usuarios.puntos_lealtad IS 'Puntos acumulados para canjeables en compras';
COMMENT ON COLUMN usuarios.fecha_suspension IS 'Fecha hasta la cual el usuario está suspendido. NULL si no está suspendido.';

COMMENT ON TABLE direcciones_usuarios IS 'Direcciones múltiples por usuario para envío, facturación y retiro';
COMMENT ON COLUMN direcciones_usuarios.es_predeterminada IS 'Dirección por defecto para envíos rápidos';

COMMENT ON TABLE auditoria_usuarios IS 'Registro de auditoría de cambios en usuarios para compliance';
