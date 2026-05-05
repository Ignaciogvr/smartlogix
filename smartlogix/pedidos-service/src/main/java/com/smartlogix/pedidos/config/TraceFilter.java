package com.smartlogix.pedidos.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                        ServletResponse response,
                        FilterChain chain)
            throws IOException, ServletException {

        // 🔥 Genera ID único por request
        String traceId = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        // 🔥 lo guarda en el contexto de logs (MDC)
        MDC.put("traceId", traceId);

        try {
            chain.doFilter(request, response);
        } finally {
            // 🔥 limpia para evitar fugas entre requests
            MDC.clear();
        }
    }
}