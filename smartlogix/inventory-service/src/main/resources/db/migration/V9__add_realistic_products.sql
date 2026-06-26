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
