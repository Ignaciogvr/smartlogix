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
