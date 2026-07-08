-- V2__seed_test_data.sql
-- Usuario de prueba para M2M tokens
INSERT INTO usuarios (auth0_id, email, nombre, rol, estado, fecha_creacion)
VALUES ('test-user-id', 'test@smartlogix.com', 'Test Admin', 'ADMIN', 'ACTIVO', CURRENT_TIMESTAMP)
ON CONFLICT (auth0_id) DO NOTHING;
