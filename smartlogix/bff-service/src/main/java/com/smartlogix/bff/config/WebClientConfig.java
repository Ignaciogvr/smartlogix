package com.smartlogix.bff.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import com.smartlogix.bff.security.SecurityUtils;
import com.smartlogix.bff.filter.RequestIdWebClientFilter;

@Configuration
public class WebClientConfig {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    @Value("${services.pedidos.url}")
    private String pedidosUrl;

    @Value("${services.usuarios.url}")
    private String usuariosUrl;

    @Value("${services.envios.url}")
    private String enviosUrl;

    @Value("${services.inventory.url}")
    private String inventoryUrl;

    private final RequestIdWebClientFilter requestIdFilter;

    public WebClientConfig(RequestIdWebClientFilter requestIdFilter) {
        this.requestIdFilter = requestIdFilter;
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(requestIdFilter)  // Propagar X-Request-Id
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    var tokenOpt = SecurityUtils.bearerToken();
                    if (tokenOpt.isPresent()) {
                        String token = tokenOpt.get();
                        log.info("🔑 WebClient FILTER → {} {} | Authorization: {}",
                                clientRequest.method(), clientRequest.url(), token);
                        return Mono.just(ClientRequest.from(clientRequest)
                                .header("Authorization", token)
                                .build());
                    } else {
                        log.warn("⚠️ WebClient FILTER → {} {} | NO TOKEN in SecurityContext",
                                clientRequest.method(), clientRequest.url());
                        return Mono.just(clientRequest);
                    }
                }));
    }

    @Bean
    public WebClient pedidosWebClient(WebClient.Builder builder) {
        return builder.baseUrl(pedidosUrl).build();
    }

    @Bean
    public WebClient usuariosWebClient(WebClient.Builder builder) {
        return builder.baseUrl(usuariosUrl).build();
    }

    @Bean
    public WebClient enviosWebClient(WebClient.Builder builder) {
        return builder.baseUrl(enviosUrl).build();
    }

    @Bean
    public WebClient inventoryWebClient(WebClient.Builder builder) {
        return builder.baseUrl(inventoryUrl).build();
    }
}