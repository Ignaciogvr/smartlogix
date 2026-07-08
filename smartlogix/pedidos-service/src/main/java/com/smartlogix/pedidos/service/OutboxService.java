package com.smartlogix.pedidos.service;

public interface OutboxService {
    void guardarEvento(String aggregateType, String aggregateId, String eventType, Object payload);
}
