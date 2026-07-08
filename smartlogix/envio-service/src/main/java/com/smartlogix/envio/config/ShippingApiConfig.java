package com.smartlogix.envio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShippingApiConfig {

    @Value("${shipping.api.url:https://api.mock-shipping.com}")
    private String shippingApiUrl;

    public String getShippingApiUrl() {
        return shippingApiUrl;
    }
}