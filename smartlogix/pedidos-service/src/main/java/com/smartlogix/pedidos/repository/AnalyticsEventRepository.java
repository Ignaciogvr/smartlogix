package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.model.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {
}
