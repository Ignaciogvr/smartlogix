-- 🔹 BASES DE DATOS
CREATE DATABASE inventory_db;
CREATE DATABASE pedidos_db;
CREATE DATABASE usuarios_db;

-- 🔹 (OPCIONAL PERO PRO) USAR LAS DB

\connect inventory_db;

-- datos de ejemplo
INSERT INTO productos (nombre, precio, stock)
VALUES ('Producto Demo', 10000, 10);

\connect usuarios_db;

INSERT INTO usuarios (nombre, email, password)
VALUES ('Admin', 'admin@test.com', '1234');