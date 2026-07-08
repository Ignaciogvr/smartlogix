package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.response.DashboardResponse;

public interface DashboardService {

    DashboardResponse dashboardAdmin();

    DashboardResponse dashboardUsuario(
            String usuarioId
    );

    DashboardResponse dashboardVentas();

    DashboardResponse dashboardLogistica();
}