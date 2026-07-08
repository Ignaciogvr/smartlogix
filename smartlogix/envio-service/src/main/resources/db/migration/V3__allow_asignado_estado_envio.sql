-- Align database constraints with EstadoEnvio.ASIGNADO used by the service layer.

ALTER TABLE envios
DROP CONSTRAINT IF EXISTS chk_estado_envio_valido;

ALTER TABLE envios
ADD CONSTRAINT chk_estado_envio_valido
CHECK (estado IN ('PENDIENTE', 'PREPARANDO', 'ASIGNADO', 'EN_RUTA', 'ENTREGADO', 'CANCELADO'));

ALTER TABLE historial_envios
DROP CONSTRAINT IF EXISTS chk_estado_historial_valido;

ALTER TABLE historial_envios
ADD CONSTRAINT chk_estado_historial_valido
CHECK (estado IN ('PENDIENTE', 'PREPARANDO', 'ASIGNADO', 'EN_RUTA', 'ENTREGADO', 'CANCELADO'));
