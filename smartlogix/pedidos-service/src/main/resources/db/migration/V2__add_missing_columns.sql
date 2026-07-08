-- V2__add_missing_columns.sql
ALTER TABLE pedidos
ADD COLUMN motivo_cancelacion TEXT,
ADD COLUMN fecha_entrega TIMESTAMP;
