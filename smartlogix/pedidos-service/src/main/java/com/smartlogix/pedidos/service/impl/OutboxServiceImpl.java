package com.smartlogix.pedidos.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.pedidos.model.OutboxEvent;
import com.smartlogix.pedidos.repository.OutboxEventRepository;
import com.smartlogix.pedidos.service.OutboxService;
import org.springframework.stereotype.Service;

@Service
public class OutboxServiceImpl implements OutboxService {
    
    private final OutboxEventRepository repository;
    private final ObjectMapper mapper;
    
    public OutboxServiceImpl(OutboxEventRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void guardarEvento(String aggregateType, String aggregateId, String eventType, Object payload) {
        try {
            OutboxEvent event = new OutboxEvent();
            event.setAggregateType(aggregateType);
            event.setAggregateId(aggregateId);
            event.setEventType(eventType);
            event.setPayload(mapper.writeValueAsString(payload));
            event.setEstado("PENDING");
            repository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando evento outbox en pedidos", e);
        }
    }
}
