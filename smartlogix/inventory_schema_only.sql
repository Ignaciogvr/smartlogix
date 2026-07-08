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

CREATE TABLE producto_imagenes (
    producto_id BIGINT NOT NULL,
    imagen_url VARCHAR(500) NOT NULL,
    
    CONSTRAINT fk_producto_imagenes_producto 
        FOREIGN KEY (producto_id) 
        REFERENCES productos(id) 
        ON DELETE CASCADE
);

CREATE INDEX idx_productos_nombre ON productos(nombre);

CREATE INDEX idx_productos_categoria ON productos(categoria);

CREATE INDEX idx_productos_estado ON productos(estado);

CREATE INDEX idx_productos_precio ON productos(precio);

CREATE INDEX idx_producto_imagenes_producto_id ON producto_imagenes(producto_id);

ALTER TABLE productos 
ADD COLUMN precio_anterior DOUBLE PRECISION,
ADD COLUMN descuento_porcentaje INTEGER,
ADD COLUMN marca VARCHAR(100),
ADD COLUMN modelo VARCHAR(100),
ADD COLUMN fabricante VARCHAR(100),
ADD COLUMN sku VARCHAR(50),
ADD COLUMN garantia VARCHAR(100),
ADD COLUMN peso VARCHAR(50),
ADD COLUMN dimensiones VARCHAR(100),
ADD COLUMN material VARCHAR(100),
ADD COLUMN color VARCHAR(50),
ADD COLUMN pais_fabricacion VARCHAR(50),
ADD COLUMN descripcion_corta VARCHAR(255);

CREATE TABLE producto_comentarios (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    nombre_cliente VARCHAR(100) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT NOW(),
    calificacion INTEGER NOT NULL CHECK (calificacion >= 1 AND calificacion <= 5),
    comentario TEXT NOT NULL,
    compra_verificada BOOLEAN NOT NULL DEFAULT FALSE,
    respuesta_empresa TEXT,
    CONSTRAINT fk_producto_comentario FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

CREATE INDEX idx_comentarios_producto ON producto_comentarios(producto_id);

ALTER TABLE producto_comentarios ADD COLUMN IF NOT EXISTS usuario_id VARCHAR(255);

ALTER TABLE producto_comentarios ADD COLUMN IF NOT EXISTS fecha_actualizacion TIMESTAMP;

ALTER TABLE producto_comentarios ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT TRUE;

ALTER TABLE productos ADD COLUMN vendedor_id VARCHAR(255) DEFAULT 'default_vendor';

CREATE TABLE banners (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descripcion VARCHAR(500),
    imagen_url VARCHAR(500) NOT NULL,
    ruta_destino VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT true,
    orden INTEGER NOT NULL DEFAULT 0,
    fecha_creacion TIMESTAMP,
    fecha_actualizacion TIMESTAMP
);

CREATE INDEX idx_banners_activo ON banners(activo);

CREATE INDEX idx_banners_orden ON banners(orden);

CREATE TABLE inventory_outbox_events (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(255) NOT NULL,
    aggregate_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

CREATE INDEX idx_inventory_outbox_estado ON inventory_outbox_events(estado);

CREATE TABLE inventory_reservas_stock (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'RESERVADA',
    fecha_reserva TIMESTAMP NOT NULL,
    fecha_expiracion TIMESTAMP NOT NULL,
    
    CONSTRAINT fk_reserva_producto 
        FOREIGN KEY (producto_id) 
        REFERENCES productos(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_inventory_reservas_estado ON inventory_reservas_stock(estado);

CREATE INDEX idx_inventory_reservas_expiracion ON inventory_reservas_stock(fecha_expiracion);

ALTER TABLE productos ADD COLUMN fecha_ultima_venta TIMESTAMP;

CREATE TABLE categorias_producto (
    id BIGSERIAL PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    slug VARCHAR(100) UNIQUE,
    imagen_url VARCHAR(500),
    orden_visualizacion INTEGER DEFAULT 0,
    activa BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subcategorias_producto (
    id BIGSERIAL PRIMARY KEY,
    categoria_id BIGINT NOT NULL,
    nombre_subcategoria VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    slug VARCHAR(100),
    orden_visualizacion INTEGER DEFAULT 0,
    activa BOOLEAN DEFAULT TRUE,
    
    CONSTRAINT fk_subcategorias_categoria 
        FOREIGN KEY (categoria_id) 
        REFERENCES categorias_producto(id) 
        ON DELETE CASCADE,
    
    CONSTRAINT uk_subcategoria_nombre UNIQUE(categoria_id, nombre_subcategoria)
);

CREATE TABLE atributos_producto (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    nombre_atributo VARCHAR(100) NOT NULL,
    valor_atributo VARCHAR(100) NOT NULL,
    
    CONSTRAINT fk_atributos_producto 
        FOREIGN KEY (producto_id) 
        REFERENCES productos(id) 
        ON DELETE CASCADE
);

CREATE TABLE variantes_producto (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    codigo_sku VARCHAR(100) UNIQUE NOT NULL,
    nombre_variante VARCHAR(255),
    precio_variante DOUBLE PRECISION,
    stock_variante INTEGER,
    atributos_json VARCHAR(500),
    imagen_variante_url VARCHAR(500),
    estado VARCHAR(50) DEFAULT 'ACTIVO',
    
    CONSTRAINT fk_variantes_producto 
        FOREIGN KEY (producto_id) 
        REFERENCES productos(id) 
        ON DELETE CASCADE
);

ALTER TABLE productos ADD COLUMN IF NOT EXISTS codigo_barras VARCHAR(50) UNIQUE;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS sku_principal VARCHAR(100) UNIQUE;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS proveedor_id BIGINT;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS peso_kg DOUBLE PRECISION;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS alto_cm DOUBLE PRECISION;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS ancho_cm DOUBLE PRECISION;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS profundidad_cm DOUBLE PRECISION;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS requiere_refrigeracion BOOLEAN DEFAULT FALSE;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS fragil BOOLEAN DEFAULT FALSE;

ALTER TABLE productos ADD COLUMN IF NOT EXISTS marca VARCHAR(100);

ALTER TABLE productos ADD COLUMN IF NOT EXISTS modelo VARCHAR(100);

ALTER TABLE productos ADD COLUMN IF NOT EXISTS color VARCHAR(100);

ALTER TABLE productos ADD COLUMN IF NOT EXISTS garantia_meses INTEGER DEFAULT 12;

CREATE TABLE proveedores_inventario (
    id BIGSERIAL PRIMARY KEY,
    nombre_proveedor VARCHAR(255) NOT NULL UNIQUE,
    rut_proveedor VARCHAR(20) UNIQUE,
    contacto_nombre VARCHAR(255),
    contacto_email VARCHAR(255),
    contacto_telefono VARCHAR(20),
    direccion VARCHAR(500),
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    tiempo_entrega_dias INTEGER DEFAULT 7,
    comision_porcentaje DOUBLE PRECISION DEFAULT 0,
    estado_proveedor VARCHAR(50) DEFAULT 'ACTIVO',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pedidos_proveedor (
    id BIGSERIAL PRIMARY KEY,
    proveedor_id BIGINT NOT NULL,
    numero_pedido VARCHAR(100) UNIQUE,
    cantidad_total INTEGER,
    monto_total DOUBLE PRECISION,
    estado_pedido VARCHAR(50) DEFAULT 'SOLICITADO',
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega_esperada TIMESTAMP,
    fecha_entrega_real TIMESTAMP,
    numero_seguimiento VARCHAR(255),
    
    CONSTRAINT fk_pedidos_proveedor 
        FOREIGN KEY (proveedor_id) 
        REFERENCES proveedores_inventario(id) 
        ON DELETE RESTRICT
);

CREATE TABLE detalles_pedido_proveedor (
    id BIGSERIAL PRIMARY KEY,
    pedido_proveedor_id BIGINT NOT NULL,
    producto_id BIGINT,
    cantidad_solicitada INTEGER,
    cantidad_recibida INTEGER DEFAULT 0,
    precio_unitario DOUBLE PRECISION,
    
    CONSTRAINT fk_detalles_pedido_proveedor 
        FOREIGN KEY (pedido_proveedor_id) 
        REFERENCES pedidos_proveedor(id) 
        ON DELETE CASCADE
);

CREATE INDEX idx_categorias_slug ON categorias_producto(slug);

CREATE INDEX idx_categorias_activa ON categorias_producto(activa);

CREATE INDEX idx_subcategorias_categoria_id ON subcategorias_producto(categoria_id);

CREATE INDEX idx_atributos_producto_id ON atributos_producto(producto_id);

CREATE INDEX idx_variantes_codigo_sku ON variantes_producto(codigo_sku);

CREATE INDEX idx_variantes_producto_id ON variantes_producto(producto_id);

CREATE INDEX idx_productos_codigo_barras ON productos(codigo_barras);

CREATE INDEX idx_productos_sku_principal ON productos(sku_principal);

CREATE INDEX idx_productos_marca ON productos(marca);

CREATE INDEX idx_proveedores_estado ON proveedores_inventario(estado_proveedor);

CREATE INDEX idx_pedidos_proveedor_estado ON pedidos_proveedor(estado_pedido);

CREATE INDEX idx_pedidos_proveedor_fecha ON pedidos_proveedor(fecha_solicitud DESC);

CREATE INDEX idx_productos_stock ON productos(stock);

CREATE INDEX idx_productos_categoria_estado ON productos(categoria, estado);

ALTER TABLE producto_comentarios ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(500);

CREATE TABLE historial_navegacion (
    id SERIAL PRIMARY KEY,
    usuario_id VARCHAR(255) NOT NULL,
    producto_id BIGINT NOT NULL,
    fecha_vista TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_historial_producto FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

CREATE INDEX idx_historial_usuario ON historial_navegacion(usuario_id);

CREATE INDEX idx_historial_fecha ON historial_navegacion(fecha_vista DESC);

ALTER TABLE comentarios_producto ADD COLUMN destacado BOOLEAN DEFAULT FALSE;

ALTER TABLE comentarios_producto ADD COLUMN votos_utilidad INT DEFAULT 0;

CREATE TABLE comentario_reportes (
    id SERIAL PRIMARY KEY,
    comentario_id BIGINT NOT NULL,
    usuario_id VARCHAR(255) NOT NULL,
    motivo VARCHAR(500) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reporte_comentario FOREIGN KEY (comentario_id) REFERENCES comentarios_producto(id) ON DELETE CASCADE
);

CREATE INDEX idx_reporte_comentario ON comentario_reportes(comentario_id);

ALTER TABLE productos 
ADD COLUMN destacado BOOLEAN DEFAULT false,
ADD COLUMN oferta BOOLEAN DEFAULT false,
ADD COLUMN nuevo BOOLEAN DEFAULT false;

ALTER TABLE producto_imagenes 
ADD COLUMN id BIGSERIAL PRIMARY KEY,
ADD COLUMN es_principal BOOLEAN DEFAULT false,
ADD COLUMN orden INTEGER DEFAULT 0,
ADD COLUMN fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE producto_comentario ADD COLUMN destacado BOOLEAN DEFAULT FALSE;

ALTER TABLE producto_comentario ADD COLUMN votos_utilidad INTEGER DEFAULT 0;

