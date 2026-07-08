-- Allow the SUSPENDIDO state used by UsuarioServiceImpl.suspenderUsuario.

ALTER TABLE usuarios
DROP CONSTRAINT IF EXISTS chk_estado_usuario_valido;

ALTER TABLE usuarios
ADD CONSTRAINT chk_estado_usuario_valido
CHECK (estado IN ('ACTIVO', 'INACTIVO', 'SUSPENDIDO'));
