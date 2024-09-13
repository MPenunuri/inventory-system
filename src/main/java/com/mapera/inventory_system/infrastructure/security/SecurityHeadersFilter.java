package com.mapera.inventory_system.infrastructure.security;

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
        exchange.getResponse().getHeaders().add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        exchange.getResponse().getHeaders().add("Pragma", "no-cache");
        exchange.getResponse().getHeaders().add("Content-Security-Policy", "default-src 'self'");
        exchange.getResponse().getHeaders().add("Referrer-Policy", "no-referrer");
        return chain.filter(exchange);
    }
}