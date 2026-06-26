-- =============================================================
-- SmartLogix - Script de datos de prueba (SEED)
-- Ejecutar DESPUÉS de docker compose up -d
-- =============================================================

-- ===== INVENTORY DB =====
\connect inventory_db

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion)
VALUES
  ('Laptop ProBook G5',      'Laptop empresarial Intel Core i7, 16GB RAM, 512GB SSD', 1200.00, 50, 'ELECTRONICA', 4.5, 120, 85,  'ACTIVO', NOW(), NOW()),
  ('Mouse Inalambrico M305', 'Mouse ergonomico inalambrico con receptor USB nano',     25.00,  200, 'ACCESORIOS',  4.2, 340, 290, 'ACTIVO', NOW(), NOW()),
  ('Teclado Mecanico K2',    'Teclado mecanico retroiluminado RGB switches blue',      89.00,   75, 'ACCESORIOS',  4.7,  89,  60, 'ACTIVO', NOW(), NOW()),
  ('Monitor UltraWide 34',   'Monitor curvo 34" IPS 3440x1440 144Hz',                650.00,   20, 'ELECTRONICA', 4.8,  45,  30, 'ACTIVO', NOW(), NOW()),
  ('Auriculares WH-1000XM5', 'Auriculares premium con cancelacion de ruido activa',  350.00,   35, 'AUDIO',       4.9, 230, 180, 'ACTIVO', NOW(), NOW())
ON CONFLICT (nombre) DO NOTHING;

-- ===== USUARIOS DB =====
\connect usuarios_db

INSERT INTO usuarios (nombre, email, auth0_id, estado)
VALUES ('Admin SmartLogix', 'admin@smartlogix.com', 'OmTPVf437mhIS422gzlQEiE3ftSqwFOc@clients', 'ACTIVO')
ON CONFLICT (auth0_id) DO NOTHING;
