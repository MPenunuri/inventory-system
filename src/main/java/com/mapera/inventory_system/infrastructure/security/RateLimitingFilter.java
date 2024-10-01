package com.mapera.inventory_system.infrastructure.security;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Profile("!test")
@Component
public class RateLimitingFilter implements WebFilter {

    private final ConcurrentHashMap<String, ClientRequestInfo> clientRequests = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_SECOND = 20;

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String clientIp = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::getHostAddress)
                .orElse("UNKNOWN");

        ClientRequestInfo requestInfo = clientRequests.computeIfAbsent(clientIp, k -> new ClientRequestInfo());

        synchronized (requestInfo) {
            if (requestInfo.isRateLimitExceeded()) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                String jsonResponse = "{\"error\": \"Too many requests\"}";
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                DataBuffer buffer = exchange.getResponse().bufferFactory()
                        .wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Mono.just(buffer));
            }
        }
        return chain.filter(exchange);
    }

    @Scheduled(fixedRate = 60000)
    public void cleanUpInactiveClients() {
        long inactivityThreshold = 300;

        clientRequests.forEach((ip, info) -> {
            if (info.isInactiveFor(inactivityThreshold)) {
                clientRequests.remove(ip);
            }
        });
    }

    static class ClientRequestInfo {
        private AtomicInteger requestCount = new AtomicInteger(0);
        private Instant lastRequestTime = Instant.now();

        boolean isRateLimitExceeded() {
            Instant now = Instant.now();
            long secondsElapsed = Duration.between(lastRequestTime, now).getSeconds();

            if (secondsElapsed >= 1) {
                lastRequestTime = now;
                requestCount.set(1);
            }

            if (requestCount.incrementAndGet() > MAX_REQUESTS_PER_SECOND) {
                return true;
            }
            return false;
        }

        boolean isInactiveFor(long seconds) {
            return Duration.between(lastRequestTime, Instant.now()).getSeconds() > seconds;
        }
    }
}
