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
-- V10__add_rich_product_fields_and_reviews.sql

-- 1. Agregar nuevos campos a la tabla productos
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

-- 2. Crear tabla de comentarios
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

-- 3. Actualizar productos existentes con datos realistas

-- Producto 1 (Periféricos - Oferta)
UPDATE productos SET 
    marca = 'Logitech', modelo = 'G Pro X Superlight', fabricante = 'Logitech G', sku = 'LOG-PROX-01',
    garantia = '2 años de garantía del fabricante', peso = '63g', dimensiones = '125.0 x 63.5 x 40.0 mm',
    material = 'Plástico premium y teflón PTFE', color = 'Negro', pais_fabricacion = 'China',
    descripcion_corta = 'Mouse gaming ultraligero profesional.',
    descripcion = 'Diseñado con profesionales de eSports, el PRO X SUPERLIGHT pesa menos de 63 gramos y desliza casi sin fricción. Cuenta con el sensor HERO 25K que proporciona precisión y control inigualables, siendo el mouse perfecto para competir al más alto nivel.',
    precio_anterior = 150000, descuento_porcentaje = 15, precio = 127500, oferta = true,
    rating_promedio = 4.8, total_ratings = 127
WHERE id = 1;

-- Producto 2 (Periféricos)
UPDATE productos SET 
    marca = 'Razer', modelo = 'BlackWidow V3 Pro', fabricante = 'Razer Inc.', sku = 'RAZ-BWV3-02',
    garantia = '2 años de garantía', peso = '1423g', dimensiones = '450.7 x 248.4 x 42.3 mm',
    material = 'Estructura de aluminio y teclas ABS', color = 'Negro', pais_fabricacion = 'China',
    descripcion_corta = 'Teclado mecánico inalámbrico premium.',
    descripcion = 'El primer teclado mecánico inalámbrico para juegos de Razer. Con interruptores mecánicos Razer Green para una ejecución táctil y sonora, conectividad Razer HyperSpeed Wireless y soporte reposamuñecas ergonómico de piel sintética.',
    rating_promedio = 4.6, total_ratings = 85
WHERE id = 2;

-- Producto 3 (Periféricos - Oferta)
UPDATE productos SET 
    marca = 'HyperX', modelo = 'Cloud II Wireless', fabricante = 'HP Inc.', sku = 'HYP-C2W-03',
    garantia = '1 año de garantía', peso = '300g', dimensiones = '190 x 136 x 87 mm',
    material = 'Aluminio y espuma viscoelástica', color = 'Rojo/Negro', pais_fabricacion = 'Taiwán',
    descripcion_corta = 'Audífonos gaming inalámbricos con sonido 7.1.',
    descripcion = 'Los legendarios auriculares Cloud II ahora sin cables. Disfruta de la comodidad de la espuma viscoelástica, micrófono con cancelación de ruido extraíble y conexión de 2,4 GHz que ofrece hasta 30 horas de batería y un radio de 20 metros.',
    precio_anterior = 130000, descuento_porcentaje = 20, precio = 104000, oferta = true,
    rating_promedio = 4.7, total_ratings = 210
WHERE id = 3;

-- Producto 4 (Monitores - Oferta)
UPDATE productos SET 
    marca = 'LG', modelo = 'UltraGear 27GP850-B', fabricante = 'LG Electronics', sku = 'LG-27GP850',
    garantia = '3 años de garantía panel', peso = '6.3 kg', dimensiones = '614.2 x 465.9 x 291.2 mm',
    material = 'Plástico mate y base metálica', color = 'Negro con detalles rojos', pais_fabricacion = 'Corea del Sur',
    descripcion_corta = 'Monitor Gaming Nano IPS 1ms a 165Hz.',
    descripcion = 'Monitor UltraGear de 27" QHD (2560x1440) Nano IPS con 1ms de tiempo de respuesta y frecuencia de actualización de 165Hz (Overclock 180Hz). Compatible con NVIDIA G-SYNC y AMD FreeSync Premium. HDR10 proporciona colores precisos.',
    precio_anterior = 450000, descuento_porcentaje = 10, precio = 405000, oferta = true,
    rating_promedio = 4.9, total_ratings = 340
WHERE id = 4;

-- Producto 5 (Monitores)
UPDATE productos SET 
    marca = 'Dell', modelo = 'UltraSharp U2723QE', fabricante = 'Dell Technologies', sku = 'DELL-U2723QE',
    garantia = '3 años de garantía Advanced Exchange', peso = '6.64 kg', dimensiones = '611.4 x 385.1 x 185.0 mm',
    material = 'Plástico reciclado y metal', color = 'Plata/Negro', pais_fabricacion = 'China',
    descripcion_corta = 'Monitor 4K USB-C Hub enfocado en productividad.',
    descripcion = 'Experimenta un color extraordinario con la tecnología IPS Black que ofrece un ratio de contraste de 2000:1. Resolución 4K (3840x2160) a 60Hz. Conectividad Hub con USB-C (entrega hasta 90W) y RJ45 integrado.',
    rating_promedio = 4.8, total_ratings = 120
WHERE id = 5;

-- Producto 6 (Monitores)
UPDATE productos SET 
    marca = 'Samsung', modelo = 'Odyssey G7', fabricante = 'Samsung Electronics', sku = 'SAM-G7-27',
    garantia = '1 año de garantía', peso = '7.2 kg', dimensiones = '614.6 x 576.0 x 305.9 mm',
    material = 'Plástico', color = 'Negro', pais_fabricacion = 'Vietnam',
    descripcion_corta = 'Monitor Curvo Gaming 240Hz 1000R.',
    descripcion = 'Monitor de 27 pulgadas curvo (1000R) con panel VA y resolución WQHD (2560x1440). Tiempo de respuesta de 1ms y asombrosos 240Hz para una fluidez incomparable. Compatible con G-Sync y FreeSync Premium Pro.',
    rating_promedio = 4.5, total_ratings = 80
WHERE id = 6;

-- Producto 7 (Muebles - Oferta)
UPDATE productos SET 
    marca = 'IKEA', modelo = 'Alex/Lagkapten', fabricante = 'IKEA of Sweden', sku = 'IKEA-ALEX-01',
    garantia = '5 años de garantía', peso = '28 kg', dimensiones = '140 x 60 x 73 cm',
    material = 'Madera MDF y tableros de partículas', color = 'Blanco Mate', pais_fabricacion = 'Suecia',
    descripcion_corta = 'Escritorio con cajonera modular.',
    descripcion = 'Una combinación clásica que te permite organizar tu espacio de trabajo. La cajonera ALEX cuenta con topes para que los cajones no se extraigan por completo. Tablero amplio de 140cm ideal para configuraciones de dos monitores.',
    precio_anterior = 90000, descuento_porcentaje = 15, precio = 76500, oferta = true,
    rating_promedio = 4.7, total_ratings = 450
WHERE id = 7;

-- Producto 8 (Muebles)
UPDATE productos SET 
    marca = 'Secretlab', modelo = 'Titan Evo 2022', fabricante = 'Secretlab', sku = 'SEC-TITAN-EVO',
    garantia = '5 años de garantía extendida', peso = '34 kg', dimensiones = 'Regular (170-189cm)',
    material = 'Tejido SoftWeave Plus / Espuma curada en frío', color = 'Cookies & Cream', pais_fabricacion = 'Singapur',
    descripcion_corta = 'Silla gaming ergonómica de nivel superior.',
    descripcion = 'La galardonada Secretlab TITAN Evo integra nuestro sistema de soporte lumbar de 4 vías L-ADAPT, reposabrazos 4D metálicos y una almohada magnética de espuma viscoelástica para la cabeza.',
    rating_promedio = 4.9, total_ratings = 890
WHERE id = 8;

-- Producto 9 (Muebles - Oferta)
UPDATE productos SET 
    marca = 'Herman Miller', modelo = 'Aeron Remastered', fabricante = 'Herman Miller Inc.', sku = 'HM-AERON-C',
    garantia = '12 años de garantía total', peso = '18.6 kg', dimensiones = 'Talla C (Grande)',
    material = 'Pellicle 8Z (Malla elástica) e Inyección de aluminio', color = 'Onyx', pais_fabricacion = 'Estados Unidos',
    descripcion_corta = 'La silla ergonómica de oficina por excelencia.',
    descripcion = 'La silla de trabajo ergonómica más reconocida del mundo. La malla Pellicle 8Z proporciona ocho zonas de tensión variada para suspensión inteligente. Soporte PostureFit SL para mantener la columna alineada.',
    precio_anterior = 1500000, descuento_porcentaje = 10, precio = 1350000, oferta = true,
    rating_promedio = 5.0, total_ratings = 1200
WHERE id = 9;

-- Producto 10 (Audio - Oferta)
UPDATE productos SET 
    marca = 'Sony', modelo = 'WH-1000XM5', fabricante = 'Sony Corporation', sku = 'SONY-WHXM5',
    garantia = '1 año de garantía', peso = '250g', dimensiones = '25.3 x 7.6 x 19.3 cm',
    material = 'Cuero sintético suave y plásticos reciclados', color = 'Plata', pais_fabricacion = 'Malasia',
    descripcion_corta = 'Audífonos over-ear con cancelación de ruido líder.',
    descripcion = 'Los auriculares WH-1000XM5 reescriben las reglas de escucha sin distracciones. 2 procesadores controlan 8 micrófonos para una cancelación de ruido sin precedentes y una calidad de llamada excepcional.',
    precio_anterior = 350000, descuento_porcentaje = 20, precio = 280000, oferta = true,
    rating_promedio = 4.8, total_ratings = 600
WHERE id = 10;

-- (Rellenando el resto sin hacer el query enorme, con descripciones similares pero reales)
UPDATE productos SET 
    marca = 'Bose', modelo = 'QuietComfort Earbuds II', fabricante = 'Bose', sku = 'BOSE-QCII',
    garantia = '1 año de garantía', peso = '6g (cada auricular)', dimensiones = '1.7 x 3.0 x 2.2 cm',
    material = 'Plástico y silicona', color = 'Triple Black', pais_fabricacion = 'China',
    descripcion_corta = 'Audífonos in-ear inalámbricos TWS.',
    descripcion = 'Audífonos true wireless de Bose con la mejor cancelación de ruido del mundo gracias a la tecnología CustomTune que personaliza el sonido a la forma de tus oídos. Hasta 6 horas de autonomía.',
    rating_promedio = 4.6, total_ratings = 320
WHERE id = 11;

UPDATE productos SET 
    marca = 'Shure', modelo = 'SM7B', fabricante = 'Shure Inc.', sku = 'SHURE-SM7B',
    garantia = '2 años de garantía', peso = '765g', dimensiones = '198.7 x 63.5 x 96 mm',
    material = 'Chasis de aluminio esmaltado', color = 'Negro grafito', pais_fabricacion = 'México',
    descripcion_corta = 'Micrófono dinámico vocal para podcast y streaming.',
    descripcion = 'El SM7B es un micrófono dinámico de patrón polar cardioide suave y plano, ideal para la voz, que cuenta con excelente blindaje contra la interferencia electromagnética generada por monitores de computadora.',
    rating_promedio = 4.9, total_ratings = 450
WHERE id = 12;

UPDATE productos SET 
    marca = 'Apple', modelo = 'MacBook Pro 14" M3 Pro', fabricante = 'Apple Inc.', sku = 'MAC-14M3PRO',
    garantia = '1 año de garantía limitada', peso = '1.61 kg', dimensiones = '31.26 x 22.12 x 1.55 cm',
    material = 'Aluminio 100% reciclado', color = 'Space Black', pais_fabricacion = 'China',
    descripcion_corta = 'Notebook profesional con chip M3 Pro, 18GB RAM, 512GB SSD.',
    descripcion = 'La notebook más avanzada de Apple. La pantalla Liquid Retina XDR es la mejor del mundo en una laptop. El chip M3 Pro ofrece un rendimiento revolucionario para tareas profesionales de edición y desarrollo. Hasta 18 horas de batería.',
    rating_promedio = 4.9, total_ratings = 150
WHERE id = 13;

UPDATE productos SET 
    marca = 'ASUS', modelo = 'ROG Zephyrus G14', fabricante = 'ASUS', sku = 'ASUS-G14',
    garantia = '2 años de garantía', peso = '1.65 kg', dimensiones = '31.2 x 22.7 x 1.85 cm',
    material = 'Aleación de magnesio', color = 'Moonlight White', pais_fabricacion = 'Taiwán',
    descripcion_corta = 'Notebook Gamer compacta 14" Ryzen 9 + RTX 4060.',
    descripcion = 'Rendimiento bestial en un formato ultracompacto. Equipada con un procesador AMD Ryzen 9, tarjeta de video NVIDIA GeForce RTX 4060 y una brillante pantalla ROG Nebula Display a 165Hz.',
    rating_promedio = 4.7, total_ratings = 180
WHERE id = 14;

UPDATE productos SET 
    marca = 'Dell', modelo = 'XPS 15 9530', fabricante = 'Dell', sku = 'DELL-XPS15',
    garantia = '1 año de garantía Premium Support', peso = '1.92 kg', dimensiones = '34.4 x 23.0 x 1.80 cm',
    material = 'Aluminio mecanizado CNC y fibra de carbono', color = 'Platinum Silver', pais_fabricacion = 'China',
    descripcion_corta = 'Laptop premium para creadores.',
    descripcion = 'Una visión perfecta. La XPS 15 combina potencia y belleza. Procesador Intel Core i7 de 13ª generación, pantalla OLED 3.5K sin bordes InfinityEdge, y gráficos RTX 4050.',
    rating_promedio = 4.5, total_ratings = 90
WHERE id = 15;

UPDATE productos SET 
    marca = 'Anker', modelo = 'PowerExpand 8-in-1', fabricante = 'Anker Innovations', sku = 'ANK-HUB81',
    garantia = '18 meses de garantía', peso = '118g', dimensiones = '12.1 x 5.5 x 1.5 cm',
    material = 'Aluminio', color = 'Space Gray', pais_fabricacion = 'China',
    descripcion_corta = 'Hub USB-C multipuerto 10Gbps.',
    descripcion = 'Expande la conectividad de tu laptop con 1 puerto HDMI 4K@60Hz, 1 puerto USB-C PD 100W, 1 puerto USB-C de datos, 2 puertos USB-A de 10Gbps, lector de tarjetas SD y microSD.',
    rating_promedio = 4.6, total_ratings = 310
WHERE id = 16;

UPDATE productos SET 
    marca = 'VIVO', modelo = 'STAND-V002', fabricante = 'VIVO', sku = 'VIVO-ST2',
    garantia = '3 años de garantía', peso = '4.5 kg', dimensiones = 'Admite monitores de 13 a 27 pulgadas',
    material = 'Acero de alta resistencia y aluminio', color = 'Negro mate', pais_fabricacion = 'China',
    descripcion_corta = 'Soporte articulado de escritorio para dos monitores.',
    descripcion = 'Soporte doble para monitor que ahorra espacio en tu escritorio. Los brazos totalmente articulados ofrecen inclinación de +90° a -90°, giro de 180° y rotación de 360°. Instalación mediante abrazadera C.',
    rating_promedio = 4.7, total_ratings = 580
WHERE id = 17;

UPDATE productos SET 
    marca = 'Incase', modelo = 'Icon Sleeve with Woolenex', fabricante = 'Incase Designs', sku = 'INCASE-SLV14',
    garantia = '1 año de garantía', peso = '180g', dimensiones = '32.5 x 23.5 x 1.5 cm',
    material = 'Woolenex 300D y 600D repelente al agua', color = 'Asphalt', pais_fabricacion = 'Vietnam',
    descripcion_corta = 'Funda protectora premium para MacBook Pro 14".',
    descripcion = 'Protección aerodinámica gracias al parachoques de EVA que absorbe impactos ligero cosido y moldeado en cada lado. El exterior está hecho de Woolenex, un poliéster altamente duradero que repele la humedad.',
    rating_promedio = 4.8, total_ratings = 140
WHERE id = 18;


-- 4. Actualizar imágenes adicionales para la Galería (Múltiples imágenes por producto)

-- Logitech Mouse
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden) VALUES
(1, 'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=800&q=80', false, 1),
(1, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&q=80', false, 2);

-- Razer Keyboard
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden) VALUES
(2, 'https://images.unsplash.com/photo-1595225476474-87563907a212?w=800&q=80', false, 1),
(2, 'https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=800&q=80', false, 2);

-- MacBok Pro
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden) VALUES
(13, 'https://images.unsplash.com/photo-1531297484001-80022131f5a1?w=800&q=80', false, 1),
(13, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=800&q=80', false, 2),
(13, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80', false, 3);

-- Secretlab Chair
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden) VALUES
(8, 'https://images.unsplash.com/photo-1581553680321-4cb53b508f7b?w=800&q=80', false, 1),
(8, 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=800&q=80', false, 2);

-- 5. Insertar Comentarios Realistas
INSERT INTO producto_comentarios (producto_id, nombre_cliente, calificacion, comentario, compra_verificada, respuesta_empresa, fecha) VALUES
(1, 'Carlos Muñoz', 5, 'Excelente producto. Llegó antes de lo esperado y la calidad es muy buena. Super ligero para jugar Valorant.', true, '¡Gracias por tu compra Carlos! Nos alegra que disfrutes de tu nuevo equipo.', NOW() - INTERVAL '2 months'),
(1, 'Andrea R.', 4, 'Muy buen mouse, aunque el precio es algo elevado. La batería dura muchísimo.', true, null, NOW() - INTERVAL '1 month'),
(1, 'Felipe Gomez', 5, 'El mejor mouse que he tenido, cero delay inalámbrico.', true, null, NOW() - INTERVAL '15 days'),

(4, 'Pedro Soto', 5, 'Excelente monitor. Colores vibrantes y los 165Hz se notan de inmediato al pasar desde 60Hz. Ningún pixel muerto.', true, '¡Hola Pedro! Qué bueno saber que el producto llegó en perfectas condiciones y estás disfrutando de los 165Hz.', NOW() - INTERVAL '3 months'),
(4, 'María Paz', 4, 'Buen monitor para trabajar y jugar, el único defecto es que el contraste no es tan bueno al ser IPS, pero los colores son precisos.', true, null, NOW() - INTERVAL '20 days'),

(13, 'Luis Fernández', 5, 'Simplemente la mejor herramienta de trabajo para desarrollo de software. Batería que dura todo el día compilando.', true, 'Totalmente de acuerdo Luis, es una máquina excepcional para desarrolladores. ¡Gracias por tu reseña!', NOW() - INTERVAL '5 days'),
(13, 'Camila V.', 5, 'La pantalla es hermosa y los parlantes suenan increíble. Vale cada peso.', false, null, NOW() - INTERVAL '1 day'),

(9, 'Roberto C.', 5, 'Mi espalda me lo agradece. Pasé de tener dolores lumbares diarios a nada. La malla es muy fresca en verano.', true, 'Hola Roberto, la salud postural es nuestra prioridad con Herman Miller. ¡Disfrútala!', NOW() - INTERVAL '6 months');
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
ALTER TABLE productos ADD COLUMN vendedor_id VARCHAR(255) DEFAULT 'default_vendor';
-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V13__create_banners_table.sql
-- Description: Create banners table
-- ============================================

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
-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V14__create_outbox_events_table.sql
-- Description: Create outbox table for reliable messaging
-- ============================================

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
-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V15__create_reservas_stock_table.sql
-- Description: Create table for stock reservations
-- ============================================

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
-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V16__add_liquidacion_fields.sql
-- Description: Add fields for automatic liquidations
-- ============================================

ALTER TABLE productos ADD COLUMN fecha_ultima_venta TIMESTAMP;
-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V17__add_more_products.sql
-- Description: Add more real products to catalog
-- ============================================

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion, version) VALUES 
-- Periféricos (1 already exists: Teclado Mecánico RGB Keychron, add 2 more)
('Mouse Logitech G Pro X Superlight', 'Mouse inalámbrico ultraligero para eSports.', 130000, 30, 'Periféricos', 4.9, 320, 1500, 'ACTIVO', NOW(), NOW(), 0),
('Webcam Logitech Brio 4K', 'Cámara web Ultra HD 4K con HDR y compatibilidad con Windows Hello.', 199990, 20, 'Periféricos', 4.7, 100, 500, 'ACTIVO', NOW(), NOW(), 0),

-- Monitores (1 already exists: Monitor LG UltraGear 27, add 2 more)
('Monitor Samsung Odyssey G7', 'Monitor curvo gaming 1440p, 240Hz, 1ms.', 550000, 5, 'Monitores', 4.8, 80, 200, 'ACTIVO', NOW(), NOW(), 0),
('Monitor Dell UltraSharp 27', 'Monitor 4K USB-C ideal para productividad y diseño gráfico.', 450000, 12, 'Monitores', 4.6, 95, 300, 'ACTIVO', NOW(), NOW(), 0),

-- Muebles (1 already exists: Silla Ergonómica Pro Office, add 2 more)
('Escritorio Standing Desk Motorizado', 'Escritorio con ajuste de altura eléctrico y memoria de posiciones.', 299990, 8, 'Muebles', 4.8, 60, 120, 'ACTIVO', NOW(), NOW(), 0),
('Silla Gamer Secretlab Titan', 'Silla de cuero sintético premium para largas jornadas de juego.', 350000, 10, 'Muebles', 4.9, 150, 400, 'ACTIVO', NOW(), NOW(), 0),

-- Audio (add 3)
('Audífonos Sony WH-1000XM5', 'Auriculares inalámbricos con cancelación de ruido líder en la industria.', 300000, 25, 'Audio', 4.8, 500, 2000, 'ACTIVO', NOW(), NOW(), 0),
('Micrófono Blue Yeti USB', 'Micrófono condensador para streaming, podcast y grabaciones.', 120000, 40, 'Audio', 4.6, 250, 1000, 'ACTIVO', NOW(), NOW(), 0),
('Altavoces Bose Companion 2', 'Sistema de altavoces multimedia Series III.', 110000, 15, 'Audio', 4.5, 120, 350, 'ACTIVO', NOW(), NOW(), 0),

-- Laptops (add 3)
('MacBook Pro M3 Pro 14"', 'Laptop de 14 pulgadas con chip M3 Pro, 18GB RAM, 512GB SSD.', 1850000, 8, 'Laptops', 4.9, 300, 800, 'ACTIVO', NOW(), NOW(), 0),
('ASUS ROG Zephyrus G14', 'Laptop gaming ultradelgada, Ryzen 9, RTX 4060.', 1500000, 6, 'Laptops', 4.7, 180, 400, 'ACTIVO', NOW(), NOW(), 0),
('Dell XPS 15', 'Laptop premium de 15.6 pulgadas, Core i7, 16GB RAM, RTX 4050.', 1650000, 7, 'Laptops', 4.6, 110, 250, 'ACTIVO', NOW(), NOW(), 0),

-- Accesorios (add 3)
('Hub USB-C Anker 8-in-1', 'Adaptador multipuerto con HDMI 4K, lector SD, Ethernet.', 45000, 50, 'Accesorios', 4.7, 400, 3000, 'ACTIVO', NOW(), NOW(), 0),
('Soporte para Monitor Doble VIVO', 'Soporte articulado de escritorio para dos monitores.', 35000, 35, 'Accesorios', 4.8, 280, 1200, 'ACTIVO', NOW(), NOW(), 0),
('Funda para Laptop Incase', 'Funda protectora acolchada para laptops de 13 y 14 pulgadas.', 25000, 100, 'Accesorios', 4.5, 90, 800, 'ACTIVO', NOW(), NOW(), 0)
ON CONFLICT (nombre) DO NOTHING;

-- INSERT IMAGES

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1593640408182-31c70c8268f5?w=800&q=80' FROM productos WHERE nombre = 'Mouse Logitech G Pro X Superlight' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1588610531238-d621b1f9b369?w=800&q=80' FROM productos WHERE nombre = 'Webcam Logitech Brio 4K' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1527443195645-1133f7f28990?w=800&q=80' FROM productos WHERE nombre = 'Monitor Samsung Odyssey G7' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1517055748807-68b3f46f481e?w=800&q=80' FROM productos WHERE nombre = 'Monitor Dell UltraSharp 27' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1595514535316-8a0328b9fb6c?w=800&q=80' FROM productos WHERE nombre = 'Escritorio Standing Desk Motorizado' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=800&q=80' FROM productos WHERE nombre = 'Silla Gamer Secretlab Titan' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=800&q=80' FROM productos WHERE nombre = 'Audífonos Sony WH-1000XM5' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=800&q=80' FROM productos WHERE nombre = 'Micrófono Blue Yeti USB' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=800&q=80' FROM productos WHERE nombre = 'Altavoces Bose Companion 2' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80' FROM productos WHERE nombre = 'MacBook Pro M3 Pro 14"' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=800&q=80' FROM productos WHERE nombre = 'ASUS ROG Zephyrus G14' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1593642702821-c823b281625c?w=800&q=80' FROM productos WHERE nombre = 'Dell XPS 15' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1605655760927-4a81baec4cc5?w=800&q=80' FROM productos WHERE nombre = 'Hub USB-C Anker 8-in-1' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1593642532744-d377ab507dc8?w=800&q=80' FROM productos WHERE nombre = 'Soporte para Monitor Doble VIVO' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1563810842013-3392ea180a3a?w=800&q=80' FROM productos WHERE nombre = 'Funda para Laptop Incase' ON CONFLICT DO NOTHING;

-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V19__categorias_y_logistica.sql
-- Description: Product categories as entities and advanced logistics fields
-- ============================================

-- Tabla: categorias_producto (en lugar de solo VARCHAR)
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

-- Tabla: subcategorias_producto
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

-- Tabla: atributos_producto (tallas, colores, etc.)
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

-- Tabla: variantes_producto (SKUs con diferentes atributos)
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

-- Agregar campos de logística a tabla productos
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

-- Tabla: proveedores_inventario
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

-- Tabla: pedidos_proveedor (para reordenamiento)
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

-- Tabla: detalles_pedido_proveedor
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

-- Índices para rendimiento
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

-- Comentarios de documentación
COMMENT ON TABLE categorias_producto IS 'Categorías de productos (Laptops, Periféricos, etc.)';
COMMENT ON TABLE variantes_producto IS 'Variantes del mismo producto con diferentes SKUs (ej: tallas, colores)';
COMMENT ON TABLE atributos_producto IS 'Atributos específicos de productos (talla, color, capacidad)';
COMMENT ON TABLE proveedores_inventario IS 'Proveedores de stock para el negocio';
COMMENT ON TABLE pedidos_proveedor IS 'Pedidos de reordenamiento a proveedores para mantener stock';
COMMENT ON COLUMN productos.peso_kg IS 'Para cálculos de flete y empaque';
COMMENT ON COLUMN productos.codigo_barras IS 'EAN/UPC para identificación en almacén y logística';
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
-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V21__add_imagen_url_to_comentarios.sql
-- Description: Add imagen_url to producto_comentarios for review images
-- ============================================

ALTER TABLE producto_comentarios ADD COLUMN IF NOT EXISTS imagen_url VARCHAR(500);

COMMENT ON COLUMN producto_comentarios.imagen_url IS 'URL de imagen opcional adjunta al comentario del producto';
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
-- Limpiar los productos para cargar todo de nuevo
DELETE FROM producto_imagenes;
DELETE FROM productos;

INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion, version) VALUES 
-- Periféricos
(1, 'Teclado Mecánico RGB Keychron', 'Teclado mecánico con switches Gateron Red, retroiluminación RGB, layout 75%.', 85000, 48, 'Periféricos', 4.8, 120, 300, 'ACTIVO', NOW(), NOW(), 0),
(2, 'Mouse Logitech G Pro X Superlight', 'Mouse inalámbrico ultraligero para eSports con sensor HERO 25K.', 130000, 30, 'Periféricos', 4.9, 320, 1500, 'ACTIVO', NOW(), NOW(), 0),
(3, 'Webcam Logitech Brio 4K', 'Cámara web Ultra HD 4K con HDR y compatibilidad con Windows Hello.', 199990, 20, 'Periféricos', 4.7, 100, 500, 'ACTIVO', NOW(), NOW(), 0),

-- Monitores
(4, 'Monitor LG UltraGear 27', 'Monitor gaming IPS de 27 pulgadas, 144Hz, 1ms de tiempo de respuesta.', 250000, 10, 'Monitores', 4.9, 200, 450, 'ACTIVO', NOW(), NOW(), 0),
(5, 'Monitor Samsung Odyssey G7', 'Monitor curvo gaming 1440p, 240Hz, 1ms.', 550000, 5, 'Monitores', 4.8, 80, 200, 'ACTIVO', NOW(), NOW(), 0),
(6, 'Monitor Dell UltraSharp 27', 'Monitor 4K USB-C ideal para productividad y diseño gráfico.', 450000, 12, 'Monitores', 4.6, 95, 300, 'ACTIVO', NOW(), NOW(), 0),

-- Muebles
(7, 'Silla Ergonómica Pro Office', 'Silla de oficina con soporte lumbar ajustable, malla transpirable.', 150000, 15, 'Muebles', 4.5, 45, 80, 'ACTIVO', NOW(), NOW(), 0),
(8, 'Escritorio Standing Desk Motorizado', 'Escritorio con ajuste de altura eléctrico y memoria de posiciones.', 299990, 8, 'Muebles', 4.8, 60, 120, 'ACTIVO', NOW(), NOW(), 0),
(9, 'Silla Gamer Secretlab Titan', 'Silla de cuero sintético premium para largas jornadas de juego.', 350000, 10, 'Muebles', 4.9, 150, 400, 'ACTIVO', NOW(), NOW(), 0),

-- Audio
(10, 'Audífonos Sony WH-1000XM5', 'Auriculares inalámbricos con cancelación de ruido.', 300000, 25, 'Audio', 4.8, 500, 2000, 'ACTIVO', NOW(), NOW(), 0),
(11, 'Micrófono Blue Yeti USB', 'Micrófono condensador para streaming, podcast y grabaciones.', 120000, 40, 'Audio', 4.6, 250, 1000, 'ACTIVO', NOW(), NOW(), 0),
(12, 'Altavoces Bose Companion 2', 'Sistema de altavoces multimedia Series III.', 110000, 15, 'Audio', 4.5, 120, 350, 'ACTIVO', NOW(), NOW(), 0),

-- Laptops
(13, 'MacBook Pro M3 Pro 14', 'Laptop de 14 pulgadas con chip M3 Pro, 18GB RAM, 512GB SSD.', 1850000, 8, 'Laptops', 4.9, 300, 800, 'ACTIVO', NOW(), NOW(), 0),
(14, 'ASUS ROG Zephyrus G14', 'Laptop gaming ultradelgada, Ryzen 9, RTX 4060.', 1500000, 6, 'Laptops', 4.7, 180, 400, 'ACTIVO', NOW(), NOW(), 0),
(15, 'Dell XPS 15', 'Laptop premium de 15.6 pulgadas, Core i7, 16GB RAM, RTX 4050.', 1650000, 7, 'Laptops', 4.6, 110, 250, 'ACTIVO', NOW(), NOW(), 0),

-- Accesorios
(16, 'Hub USB-C Anker 8-in-1', 'Adaptador multipuerto con HDMI 4K, lector SD, Ethernet.', 45000, 50, 'Accesorios', 4.7, 400, 3000, 'ACTIVO', NOW(), NOW(), 0),
(17, 'Soporte para Monitor Doble VIVO', 'Soporte articulado de escritorio para dos monitores.', 35000, 35, 'Accesorios', 4.8, 280, 1200, 'ACTIVO', NOW(), NOW(), 0),
(18, 'Funda para Laptop Incase', 'Funda protectora acolchada para laptops de 13 y 14 pulgadas.', 25000, 100, 'Accesorios', 4.5, 90, 800, 'ACTIVO', NOW(), NOW(), 0);

-- Reset sequence for id to avoid conflicts when creating new products via API
SELECT setval('productos_id_seq', 18);

INSERT INTO producto_imagenes (producto_id, imagen_url) VALUES 
-- Periféricos
(1, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&q=80'),
(2, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&q=80'),
(3, 'https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?w=800&q=80'),
-- Monitores
(4, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&q=80'),
(5, 'https://images.unsplash.com/photo-1585792180666-f7347c490ee2?w=800&q=80'),
(6, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=800&q=80'),
-- Muebles
(7, 'https://images.unsplash.com/photo-1580480055273-228ff5388ef8?w=800&q=80'),
(8, 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=800&q=80'),
(9, 'https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=800&q=80'),
-- Audio
(10, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80'),
(11, 'https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=800&q=80'),
(12, 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=800&q=80'),
-- Laptops
(13, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80'),
(14, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=800&q=80'),
(15, 'https://images.unsplash.com/photo-1593642702821-c823b281625c?w=800&q=80'),
-- Accesorios
(16, 'https://images.unsplash.com/photo-1625723044792-44de16ccb4e9?w=800&q=80'),
(17, 'https://images.unsplash.com/photo-1593642532744-d377ab507dc8?w=800&q=80'),
(18, 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=800&q=80');
ALTER TABLE productos 
ADD COLUMN destacado BOOLEAN DEFAULT false,
ADD COLUMN oferta BOOLEAN DEFAULT false,
ADD COLUMN nuevo BOOLEAN DEFAULT false;

UPDATE productos SET destacado = true WHERE id IN (1, 3, 5, 7, 9);
UPDATE productos SET oferta = true WHERE id IN (2, 4, 6, 8, 10);
UPDATE productos SET nuevo = true WHERE id IN (11, 13, 15, 17, 19);

ALTER TABLE producto_imagenes 
ADD COLUMN id BIGSERIAL PRIMARY KEY,
ADD COLUMN es_principal BOOLEAN DEFAULT false,
ADD COLUMN orden INTEGER DEFAULT 0,
ADD COLUMN fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

UPDATE producto_imagenes 
SET es_principal = true 
WHERE id IN (
  SELECT min(id) FROM producto_imagenes GROUP BY producto_id
);

-- Nuevos productos para cumplir con 8 por cada de las 10 categor�as
-- Las 10 categor�as son: Perif�ricos, Monitores, Laptops, Audio, Muebles, Accesorios, Redes, Gaming, Almacenamiento, Smart Home
-- Inserto registros de prueba gen�ricos para completar
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (20, 'Producto Gen�rico Perif�ricos 1', 'Descripci�n autogenerada para completar cat�logo', 442751, 58, 'Perif�ricos', 'ACTIVO', 3.9, 3, 31, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (20, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (21, 'Producto Gen�rico Perif�ricos 2', 'Descripci�n autogenerada para completar cat�logo', 409341, 91, 'Perif�ricos', 'ACTIVO', 3.8, 12, 128, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (21, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (22, 'Producto Gen�rico Perif�ricos 3', 'Descripci�n autogenerada para completar cat�logo', 371624, 36, 'Perif�ricos', 'ACTIVO', 4.7, 22, 224, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (22, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (23, 'Producto Gen�rico Perif�ricos 4', 'Descripci�n autogenerada para completar cat�logo', 251209, 37, 'Perif�ricos', 'ACTIVO', 3.6, 49, 499, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (23, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (24, 'Producto Gen�rico Perif�ricos 5', 'Descripci�n autogenerada para completar cat�logo', 100869, 90, 'Perif�ricos', 'ACTIVO', 3.8, 43, 433, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (24, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (25, 'Producto Gen�rico Monitores 1', 'Descripci�n autogenerada para completar cat�logo', 387622, 96, 'Monitores', 'ACTIVO', 3.9, 37, 373, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (25, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (26, 'Producto Gen�rico Monitores 2', 'Descripci�n autogenerada para completar cat�logo', 212230, 18, 'Monitores', 'ACTIVO', 4.9, 42, 426, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (26, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (27, 'Producto Gen�rico Monitores 3', 'Descripci�n autogenerada para completar cat�logo', 238537, 62, 'Monitores', 'ACTIVO', 3.7, 36, 366, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (27, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (28, 'Producto Gen�rico Monitores 4', 'Descripci�n autogenerada para completar cat�logo', 203074, 87, 'Monitores', 'ACTIVO', 4.6, 15, 156, NOW(), NOW(), 0, true, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (28, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (29, 'Producto Gen�rico Monitores 5', 'Descripci�n autogenerada para completar cat�logo', 386403, 38, 'Monitores', 'ACTIVO', 4.1, 27, 273, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (29, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (30, 'Producto Gen�rico Laptops 1', 'Descripci�n autogenerada para completar cat�logo', 20760, 23, 'Laptops', 'ACTIVO', 4.2, 30, 306, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (30, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (31, 'Producto Gen�rico Laptops 2', 'Descripci�n autogenerada para completar cat�logo', 335706, 78, 'Laptops', 'ACTIVO', 4.9, 2, 28, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (31, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (32, 'Producto Gen�rico Laptops 3', 'Descripci�n autogenerada para completar cat�logo', 460302, 84, 'Laptops', 'ACTIVO', 4.7, 11, 112, NOW(), NOW(), 0, true, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (32, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (33, 'Producto Gen�rico Laptops 4', 'Descripci�n autogenerada para completar cat�logo', 462128, 21, 'Laptops', 'ACTIVO', 4.7, 17, 177, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (33, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (34, 'Producto Gen�rico Laptops 5', 'Descripci�n autogenerada para completar cat�logo', 232460, 33, 'Laptops', 'ACTIVO', 4.9, 35, 351, NOW(), NOW(), 0, true, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (34, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (35, 'Producto Gen�rico Audio 1', 'Descripci�n autogenerada para completar cat�logo', 188861, 90, 'Audio', 'ACTIVO', 3.5, 4, 49, NOW(), NOW(), 0, true, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (35, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (36, 'Producto Gen�rico Audio 2', 'Descripci�n autogenerada para completar cat�logo', 172231, 58, 'Audio', 'ACTIVO', 4.9, 36, 369, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (36, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (37, 'Producto Gen�rico Audio 3', 'Descripci�n autogenerada para completar cat�logo', 74329, 17, 'Audio', 'ACTIVO', 3.9, 34, 345, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (37, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (38, 'Producto Gen�rico Audio 4', 'Descripci�n autogenerada para completar cat�logo', 477921, 70, 'Audio', 'ACTIVO', 4.6, 43, 435, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (38, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (39, 'Producto Gen�rico Audio 5', 'Descripci�n autogenerada para completar cat�logo', 113381, 14, 'Audio', 'ACTIVO', 4.4, 27, 274, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (39, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (40, 'Producto Gen�rico Muebles 1', 'Descripci�n autogenerada para completar cat�logo', 263474, 43, 'Muebles', 'ACTIVO', 4.7, 7, 75, NOW(), NOW(), 0, true, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (40, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (41, 'Producto Gen�rico Muebles 2', 'Descripci�n autogenerada para completar cat�logo', 456328, 99, 'Muebles', 'ACTIVO', 3.8, 25, 258, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (41, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (42, 'Producto Gen�rico Muebles 3', 'Descripci�n autogenerada para completar cat�logo', 237881, 42, 'Muebles', 'ACTIVO', 4.5, 20, 206, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (42, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (43, 'Producto Gen�rico Muebles 4', 'Descripci�n autogenerada para completar cat�logo', 64931, 42, 'Muebles', 'ACTIVO', 3.6, 25, 252, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (43, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (44, 'Producto Gen�rico Muebles 5', 'Descripci�n autogenerada para completar cat�logo', 442920, 100, 'Muebles', 'ACTIVO', 4.9, 39, 392, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (44, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (45, 'Producto Gen�rico Accesorios 1', 'Descripci�n autogenerada para completar cat�logo', 233792, 68, 'Accesorios', 'ACTIVO', 4.9, 48, 484, NOW(), NOW(), 0, true, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (45, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (46, 'Producto Gen�rico Accesorios 2', 'Descripci�n autogenerada para completar cat�logo', 47248, 51, 'Accesorios', 'ACTIVO', 4.0, 44, 441, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (46, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (47, 'Producto Gen�rico Accesorios 3', 'Descripci�n autogenerada para completar cat�logo', 121626, 43, 'Accesorios', 'ACTIVO', 4.1, 26, 268, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (47, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (48, 'Producto Gen�rico Accesorios 4', 'Descripci�n autogenerada para completar cat�logo', 211488, 34, 'Accesorios', 'ACTIVO', 4.6, 44, 443, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (48, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (49, 'Producto Gen�rico Accesorios 5', 'Descripci�n autogenerada para completar cat�logo', 490318, 86, 'Accesorios', 'ACTIVO', 4.8, 20, 201, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (49, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (50, 'Producto Gen�rico Redes 1', 'Descripci�n autogenerada para completar cat�logo', 236625, 22, 'Redes', 'ACTIVO', 4.9, 14, 147, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (50, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (51, 'Producto Gen�rico Redes 2', 'Descripci�n autogenerada para completar cat�logo', 134409, 79, 'Redes', 'ACTIVO', 4.3, 33, 331, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (51, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (52, 'Producto Gen�rico Redes 3', 'Descripci�n autogenerada para completar cat�logo', 102038, 40, 'Redes', 'ACTIVO', 4.4, 29, 298, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (52, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (53, 'Producto Gen�rico Redes 4', 'Descripci�n autogenerada para completar cat�logo', 334092, 20, 'Redes', 'ACTIVO', 4.0, 31, 316, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (53, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (54, 'Producto Gen�rico Redes 5', 'Descripci�n autogenerada para completar cat�logo', 227872, 90, 'Redes', 'ACTIVO', 4.3, 25, 251, NOW(), NOW(), 0, true, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (54, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (55, 'Producto Gen�rico Redes 6', 'Descripci�n autogenerada para completar cat�logo', 229550, 38, 'Redes', 'ACTIVO', 3.8, 18, 187, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (55, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (56, 'Producto Gen�rico Redes 7', 'Descripci�n autogenerada para completar cat�logo', 309809, 72, 'Redes', 'ACTIVO', 4.5, 27, 278, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (56, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (57, 'Producto Gen�rico Redes 8', 'Descripci�n autogenerada para completar cat�logo', 77518, 62, 'Redes', 'ACTIVO', 5.0, 20, 207, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (57, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (58, 'Producto Gen�rico Gaming 1', 'Descripci�n autogenerada para completar cat�logo', 468263, 93, 'Gaming', 'ACTIVO', 4.2, 34, 345, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (58, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (59, 'Producto Gen�rico Gaming 2', 'Descripci�n autogenerada para completar cat�logo', 134201, 23, 'Gaming', 'ACTIVO', 3.6, 3, 31, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (59, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (60, 'Producto Gen�rico Gaming 3', 'Descripci�n autogenerada para completar cat�logo', 447939, 58, 'Gaming', 'ACTIVO', 4.0, 47, 477, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (60, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (61, 'Producto Gen�rico Gaming 4', 'Descripci�n autogenerada para completar cat�logo', 381065, 76, 'Gaming', 'ACTIVO', 4.0, 5, 54, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (61, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (62, 'Producto Gen�rico Gaming 5', 'Descripci�n autogenerada para completar cat�logo', 470176, 13, 'Gaming', 'ACTIVO', 4.6, 20, 209, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (62, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (63, 'Producto Gen�rico Gaming 6', 'Descripci�n autogenerada para completar cat�logo', 469189, 74, 'Gaming', 'ACTIVO', 4.9, 33, 336, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (63, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (64, 'Producto Gen�rico Gaming 7', 'Descripci�n autogenerada para completar cat�logo', 247602, 29, 'Gaming', 'ACTIVO', 4.6, 26, 266, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (64, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (65, 'Producto Gen�rico Gaming 8', 'Descripci�n autogenerada para completar cat�logo', 446729, 10, 'Gaming', 'ACTIVO', 4.8, 19, 194, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (65, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (66, 'Producto Gen�rico Almacenamiento 1', 'Descripci�n autogenerada para completar cat�logo', 229222, 20, 'Almacenamiento', 'ACTIVO', 4.3, 27, 271, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (66, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (67, 'Producto Gen�rico Almacenamiento 2', 'Descripci�n autogenerada para completar cat�logo', 82021, 68, 'Almacenamiento', 'ACTIVO', 4.8, 14, 143, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (67, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (68, 'Producto Gen�rico Almacenamiento 3', 'Descripci�n autogenerada para completar cat�logo', 459615, 25, 'Almacenamiento', 'ACTIVO', 4.9, 42, 426, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (68, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (69, 'Producto Gen�rico Almacenamiento 4', 'Descripci�n autogenerada para completar cat�logo', 461250, 87, 'Almacenamiento', 'ACTIVO', 4.4, 44, 443, NOW(), NOW(), 0, true, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (69, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (70, 'Producto Gen�rico Almacenamiento 5', 'Descripci�n autogenerada para completar cat�logo', 174954, 36, 'Almacenamiento', 'ACTIVO', 4.1, 46, 461, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (70, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (71, 'Producto Gen�rico Almacenamiento 6', 'Descripci�n autogenerada para completar cat�logo', 80760, 56, 'Almacenamiento', 'ACTIVO', 3.6, 46, 463, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (71, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (72, 'Producto Gen�rico Almacenamiento 7', 'Descripci�n autogenerada para completar cat�logo', 215727, 46, 'Almacenamiento', 'ACTIVO', 4.4, 19, 192, NOW(), NOW(), 0, true, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (72, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (73, 'Producto Gen�rico Almacenamiento 8', 'Descripci�n autogenerada para completar cat�logo', 330036, 11, 'Almacenamiento', 'ACTIVO', 4.1, 34, 343, NOW(), NOW(), 0, true, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (73, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (74, 'Producto Gen�rico Smart Home 1', 'Descripci�n autogenerada para completar cat�logo', 380366, 88, 'Smart Home', 'ACTIVO', 3.7, 37, 373, NOW(), NOW(), 0, true, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (74, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (75, 'Producto Gen�rico Smart Home 2', 'Descripci�n autogenerada para completar cat�logo', 97058, 42, 'Smart Home', 'ACTIVO', 3.9, 47, 470, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (75, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (76, 'Producto Gen�rico Smart Home 3', 'Descripci�n autogenerada para completar cat�logo', 168749, 41, 'Smart Home', 'ACTIVO', 4.5, 13, 132, NOW(), NOW(), 0, false, true, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (76, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (77, 'Producto Gen�rico Smart Home 4', 'Descripci�n autogenerada para completar cat�logo', 83538, 27, 'Smart Home', 'ACTIVO', 4.6, 48, 487, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (77, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (78, 'Producto Gen�rico Smart Home 5', 'Descripci�n autogenerada para completar cat�logo', 432111, 22, 'Smart Home', 'ACTIVO', 3.7, 19, 198, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (78, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (79, 'Producto Gen�rico Smart Home 6', 'Descripci�n autogenerada para completar cat�logo', 311858, 33, 'Smart Home', 'ACTIVO', 4.0, 38, 383, NOW(), NOW(), 0, true, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (79, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (80, 'Producto Gen�rico Smart Home 7', 'Descripci�n autogenerada para completar cat�logo', 294873, 83, 'Smart Home', 'ACTIVO', 4.5, 31, 312, NOW(), NOW(), 0, false, false, true);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (80, 'assets/images/product-placeholder.webp', true, 1, NOW());
INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, estado, rating_promedio, total_ratings, cantidad_vendidos, fecha_creacion, fecha_actualizacion, version, destacado, oferta, nuevo) VALUES (81, 'Producto Gen�rico Smart Home 8', 'Descripci�n autogenerada para completar cat�logo', 170407, 96, 'Smart Home', 'ACTIVO', 4.9, 16, 169, NOW(), NOW(), 0, false, false, false);
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion) VALUES (81, 'assets/images/product-placeholder.webp', true, 1, NOW());
SELECT setval('productos_id_seq', 83);
-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V7__add_missing_categories.sql
-- Description: Add products for missing categories: Redes, Gaming, Almacenamiento, Smart Home
-- ============================================

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion, version) VALUES 
-- Redes
('Switch Cisco Catalyst 2960', 'Switch gestionable de 24 puertos Gigabit para redes empresariales.', 450000, 15, 'Redes', 4.7, 80, 200, 'ACTIVO', NOW(), NOW(), 0),
('Cable Ethernet Cat6 UTP', 'Cable de red Cat6 de 10 metros, blindado para alta velocidad.', 8990, 200, 'Redes', 4.5, 300, 1500, 'ACTIVO', NOW(), NOW(), 0),
('Access Point Ubiquiti UniFi', 'Punto de acceso Wi-Fi de alto rendimiento para interiores.', 120000, 25, 'Redes', 4.8, 150, 400, 'ACTIVO', NOW(), NOW(), 0),

-- Gaming
('Consola PlayStation 5', 'Consola de última generación con lector de discos 4K.', 650000, 10, 'Gaming', 4.9, 500, 2000, 'ACTIVO', NOW(), NOW(), 0),
('Joystick Xbox Elite Series 2', 'Control premium con paddles traseros programables.', 120000, 30, 'Gaming', 4.8, 250, 800, 'ACTIVO', NOW(), NOW(), 0),
('Tarjeta Gráfica RTX 4070', 'GPU NVIDIA GeForce RTX 4070 12GB GDDR6X.', 850000, 5, 'Gaming', 4.9, 100, 300, 'ACTIVO', NOW(), NOW(), 0),

-- Almacenamiento
('SSD Samsung 980 Pro 1TB', 'Unidad de estado sólido NVMe M.2 de alta velocidad.', 180000, 40, 'Almacenamiento', 4.8, 400, 1200, 'ACTIVO', NOW(), NOW(), 0),
('Disco Duro Externo 4TB', 'Disco externo portátil USB 3.0 de 4TB.', 120000, 35, 'Almacenamiento', 4.6, 200, 600, 'ACTIVO', NOW(), NOW(), 0),
('Memoria USB 128GB', 'Unidad flash USB 3.1 de alta capacidad.', 25000, 150, 'Almacenamiento', 4.5, 350, 1800, 'ACTIVO', NOW(), NOW(), 0),

-- Smart Home
('Google Nest Hub', 'Pantalla inteligente con Google Assistant y control del hogar.', 180000, 20, 'Smart Home', 4.7, 180, 500, 'ACTIVO', NOW(), NOW(), 0),
('Cámara de Seguridad Ring', 'Cámara Wi-Fi con visión nocturna y detección de movimiento.', 120000, 25, 'Smart Home', 4.6, 220, 700, 'ACTIVO', NOW(), NOW(), 0),
('Termostato Inteligente Ecobee', 'Termostato WiFi con control por voz y ahorro de energía.', 150000, 18, 'Smart Home', 4.8, 150, 400, 'ACTIVO', NOW(), NOW(), 0)
ON CONFLICT (nombre) DO NOTHING;

-- INSERT IMAGES

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Switch Cisco Catalyst 2960' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1605810230434-7631ac76ec81?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Cable Ethernet Cat6 UTP' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Access Point Ubiquiti UniFi' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Consola PlayStation 5' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1592840496694-26d035b52b48?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Joystick Xbox Elite Series 2' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1591488320449-011701bb6704?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Tarjeta Gráfica RTX 4070' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=800&q=80', true, 0 FROM productos WHERE nombre = 'SSD Samsung 980 Pro 1TB' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1531492746076-161ca9bcad58?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Disco Duro Externo 4TB' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1618410320928-25228d811631?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Memoria USB 128GB' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1558002038-1091a1661116?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Google Nest Hub' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1557597774-9d273605dfa9?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Cámara de Seguridad Ring' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1563810842013-3392ea180a3a?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Termostato Inteligente Ecobee' ON CONFLICT DO NOTHING;
-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V8__clean_duplicate_images.sql
-- Description: Clean duplicate images and fix associations
-- ============================================

-- Eliminar duplicados de producto_imagenes (mismo producto_id + misma url)
DELETE FROM producto_imagenes 
WHERE id NOT IN (
    SELECT MIN(id) 
    FROM producto_imagenes 
    GROUP BY producto_id, imagen_url
);

-- Actualizar campo orden para todas las imágenes
-- Esto asegura que cada imagen tenga un orden secuencial por producto
WITH ordered_images AS (
    SELECT 
        id,
        producto_id,
        imagen_url,
        ROW_NUMBER() OVER (PARTITION BY producto_id ORDER BY id) - 1 as new_orden
    FROM producto_imagenes
)
UPDATE producto_imagenes pi
SET orden = oi.new_orden
FROM ordered_images oi
WHERE pi.id = oi.id;

-- Asegurar que la primera imagen de cada producto tenga es_principal = true
UPDATE producto_imagenes pi
SET es_principal = true
WHERE id = (
    SELECT MIN(id)
    FROM producto_imagenes
    WHERE producto_id = pi.producto_id
);

-- Asegurar que las demás imágenes tengan es_principal = false
UPDATE producto_imagenes pi
SET es_principal = false
WHERE id NOT IN (
    SELECT MIN(id)
    FROM producto_imagenes
    GROUP BY producto_id
);

-- Verificar integridad: eliminar imágenes huérfanas (sin producto válido)
DELETE FROM producto_imagenes 
WHERE producto_id NOT IN (SELECT id FROM productos);

-- Verificar integridad: eliminar productos sin imágenes (opcional, descomentar si se desea)
-- DELETE FROM productos 
-- WHERE id NOT IN (SELECT DISTINCT producto_id FROM producto_imagenes);
-- V9__add_comentario_features.sql
ALTER TABLE producto_comentario ADD COLUMN destacado BOOLEAN DEFAULT FALSE;
ALTER TABLE producto_comentario ADD COLUMN votos_utilidad INTEGER DEFAULT 0;
-- ========================================================================
-- V9__add_realistic_products.sql
-- Inserción de productos realistas para las categorías existentes
-- Compatible con schema post-V6 (columnas: destacado, oferta, nuevo en productos;
-- es_principal, orden, fecha_creacion en producto_imagenes)
-- ========================================================================

-- ===================== PERIFÉRICOS =====================
WITH p1 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Logitech MX Master 3S',
        'Ratón inalámbrico avanzado con sensor óptico 8K DPI. Diseño ergonómico de primera clase ideal para productividad y creación de contenido. Clics silenciosos y rueda de desplazamiento MagSpeed ultrarrápida.',
        99.99, 150, 'Periféricos', 4.8, 1205, 3400, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p1;

WITH p2 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Keychron Q1 Pro',
        'Teclado mecánico custom inalámbrico con chasis de aluminio CNC completo. Diseño al 75% con conectividad Bluetooth 5.1 y cable USB-C. Switches intercambiables en caliente con soporte nativo para QMK/VIA.',
        199.00, 75, 'Periféricos', 4.9, 340, 850, 'ACTIVO', true, false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1595225476474-87563907a212?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p2;

WITH p3 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Sony WH-1000XM5',
        'Auriculares inalámbricos con cancelación de ruido activa líder en la industria. Audio de alta resolución, hasta 30 horas de autonomía y control táctil inteligente. Perfectos para trabajo y viajes.',
        348.00, 200, 'Periféricos', 4.7, 8900, 15000, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p3;

WITH p4 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Elgato Stream Deck MK.2',
        'Controlador de estudio con 15 teclas LCD personalizables. Automatiza y controla tus aplicaciones, herramientas y plataformas favoritas como OBS, Twitch y YouTube con un solo toque.',
        149.99, 120, 'Periféricos', 4.8, 1150, 4200, 'ACTIVO', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p4;

-- ===================== MONITORES =====================
WITH p5 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'LG UltraGear 27" 144Hz Nano IPS',
        'Monitor gaming QHD (2560×1440) de 27 pulgadas con panel Nano IPS. Tiempo de respuesta 1ms (GtG), compatibilidad con NVIDIA G-SYNC y AMD FreeSync Premium. Soporte ajustable en altura e inclinación.',
        299.99, 85, 'Monitores', 4.6, 600, 1200, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p5;

WITH p6 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Dell UltraSharp 32" 4K USB-C Hub',
        'Monitor profesional 4K UHD 31.5 pulgadas con hub USB-C de 90W, Ethernet integrada y cobertura de color de grado profesional (99% sRGB, 99% Rec 709). Ideal para diseño y productividad.',
        759.00, 40, 'Monitores', 4.8, 250, 600, 'ACTIVO', true, false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1586772002130-b0f3daa6288b?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p6;

WITH p7 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Samsung Odyssey G9 49" Curvo DQHD',
        'Monitor ultrapanorámico 49 pulgadas, curva 1000R, resolución DQHD 5120×1440. Tasa de refresco 240Hz, QLED y panel VA con colores vibrantes. La experiencia gaming definitiva.',
        1299.99, 15, 'Monitores', 4.5, 410, 800, 'ACTIVO', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1616012480717-fd9867059ca0?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p7;

WITH p8 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'BenQ Zowie XL2546K 240Hz eSports',
        'Monitor de eSports 1080p con tecnología DyAc+ que minimiza el desenfoque en movimiento. Base rediseñada para optimizar el espacio del escritorio. Control rápido con S-Switch incluido.',
        429.00, 60, 'Monitores', 4.7, 820, 2100, 'ACTIVO', false, false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1593640408182-31c228b60d3d?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p8;

-- ===================== MUEBLES =====================
WITH p9 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Herman Miller Aeron Remastered',
        'Silla ergonómica de culto con malla Pellicle transpirable que adapta el cuerpo perfectamente. Soporte lumbar PostureFit SL y reposabrazos 4D totalmente ajustables. El estándar de oro en ergonomía de oficina.',
        1450.00, 30, 'Muebles', 4.9, 1400, 3500, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1505843490538-5133c6c7d0e1?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p9;

WITH p10 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Secretlab Titan Evo 2022',
        'Silla gamer premium con espuma precurvada de alta densidad y piel sintética híbrida ultra resistente. Sistema de soporte lumbar magnético L-ADAPT para largas sesiones de trabajo y gaming.',
        549.00, 100, 'Muebles', 4.6, 2100, 6500, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1598300042247-d088f8ab3a91?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p10;

WITH p11 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Flexispot E7 Standing Desk',
        'Escritorio elevable eléctrico con doble motor silencioso y panel táctil con memoria de 4 alturas. Tablero de bambú ecológico resistente a arañazos. Soporta hasta 125kg. Mejora tu postura y salud.',
        499.99, 45, 'Muebles', 4.8, 620, 1300, 'ACTIVO', false, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p11;

WITH p12 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'IKEA Alex Cajonera Blanco Mate',
        'Cajonera icónica con 5 cajones espaciosos en blanco mate brillante. El favorito de setuppers y creadores de contenido. Perfecta como elemento independiente o apoyo para encimeras largas de escritorio.',
        89.99, 250, 'Muebles', 4.7, 5400, 22000, 'ACTIVO', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1556909172-54557c7e4fb7?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p12;
