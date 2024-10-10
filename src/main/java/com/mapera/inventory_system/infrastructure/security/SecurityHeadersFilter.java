package com.mapera.inventory_system.infrastructure.security;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class SecurityHeadersFilter implements WebFilter {

        @Override
        @NonNull
        public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
                exchange.getResponse().getHeaders().add("X-Content-Type-Options", "nosniff");
                exchange.getResponse().getHeaders().add("X-Frame-Options", "DENY");
                exchange.getResponse().getHeaders().add("X-XSS-Protection", "1; mode=block");
                exchange.getResponse().getHeaders().add("Cache-Control",
                                "no-store, no-cache, must-revalidate, max-age=0");
                exchange.getResponse().getHeaders().add("Pragma", "no-cache");
                exchange.getResponse().getHeaders().add("Content-Security-Policy", "default-src 'self'");
                exchange.getResponse().getHeaders().add("Referrer-Policy", "no-referrer");

                exchange.getResponse().getHeaders().add(
                                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://free-inventory-manager.netlify.app");
                exchange.getResponse().getHeaders().add(
                                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                                "GET, POST, PATCH, DELETE, OPTIONS");
                exchange.getResponse().getHeaders().add(
                                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                                "Authorization, Content-Type");
                exchange.getResponse().getHeaders().add(
                                HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                return chain.filter(exchange);
        }
}