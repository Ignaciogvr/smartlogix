CREATE TABLE pedido_historial (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    estado_anterior VARCHAR(50),
    estado_nuevo VARCHAR(50) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    CONSTRAINT fk_pedido_historial_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE
);

CREATE INDEX idx_pedido_historial_pedido_id ON pedido_historial(pedido_id);
CREATE INDEX idx_pedido_historial_fecha ON pedido_historial(fecha);
