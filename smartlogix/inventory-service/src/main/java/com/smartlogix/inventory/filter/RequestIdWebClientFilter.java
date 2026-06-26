package com.smartlogix.inventory.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * Filtro WebClient para propagar X-Request-Id en llamadas HTTP downstream.
 * Lee requestId del MDC y lo agrega como header en todas las requests.
 */
@Component
public class RequestIdWebClientFilter implements ExchangeFilterFunction {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        // Leer requestId del MDC actual
        String requestId = MDC.get(REQUEST_ID_MDC_KEY);

        if (requestId != null && !requestId.isBlank()) {
            // Propagar en header de la llamada downstream
            ClientRequest newRequest = ClientRequest.from(request)
                    .header(REQUEST_ID_HEADER, requestId)
                    .build();
            return next.exchange(newRequest);
        }

        return next.exchange(request);
    }
}
