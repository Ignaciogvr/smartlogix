-- ============================================
-- SmartLogix - Envio Service
-- Migration: V2__add_delivery_evidence_columns.sql
-- Description: Add foto_entrega_url and firma_receptor_url to envios table
-- ============================================

ALTER TABLE envios
ADD COLUMN foto_entrega_url VARCHAR(500),
ADD COLUMN firma_receptor_url VARCHAR(500);

COMMENT ON COLUMN envios.foto_entrega_url IS 'URL de la fotografía tomada en el momento de la entrega';
COMMENT ON COLUMN envios.firma_receptor_url IS 'URL de la firma digital del receptor';
