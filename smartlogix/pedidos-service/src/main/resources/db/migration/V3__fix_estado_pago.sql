-- Agregar CONFIRMADO al constraint de estado_pago
ALTER TABLE pagos DROP CONSTRAINT IF EXISTS chk_estado_pago_valido;

ALTER TABLE pagos ADD CONSTRAINT chk_estado_pago_valido CHECK (
    estado_pago IN ('PENDIENTE', 'PROCESANDO', 'COMPLETADO', 'CONFIRMADO', 'FALLIDO', 'RECHAZADO', 'REEMBOLSADO')
);
