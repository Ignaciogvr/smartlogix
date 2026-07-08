package com.smartlogix.inventory.service;

public interface OutboxService {
    void guardarEvento(String aggregateType, String aggregateId, String eventType, Object payload);
}
