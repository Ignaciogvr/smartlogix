package com.smartlogix.envio.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro para gestionar requestId en MDC.
 * - Lee X-Request-Id del header (si viene de upstream)
 * - Genera UUID si no existe
 * - Lo guarda en MDC para logs
 * - Lo propaga en response header
 * - Limpia MDC al finalizar
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-Id";
    public static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Leer requestId del header (si viene de upstream)
            String requestId = request.getHeader(REQUEST_ID_HEADER);

            // 2. Si no existe, generar uno nuevo
            if (requestId == null || requestId.isBlank()) {
                requestId = UUID.randomUUID().toString();
            }

            // 3. Guardar en MDC para logs
            MDC.put(REQUEST_ID_MDC_KEY, requestId);

            // 4. Propagar en response header
            response.setHeader(REQUEST_ID_HEADER, requestId);

            filterChain.doFilter(request, response);

        } finally {
            // 5. Limpiar MDC al finalizar request
            MDC.clear();
        }
    }
}
