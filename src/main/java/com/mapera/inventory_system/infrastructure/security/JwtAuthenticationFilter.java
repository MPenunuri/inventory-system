
package com.mapera.inventory_system.infrastructure.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);

        setServerAuthenticationConverter(new ServerAuthenticationConverter() {
            @Override
            public Mono<Authentication> convert(ServerWebExchange exchange) {
                String token = extractToken(exchange);
                return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(null, token));
            }

            private String extractToken(ServerWebExchange exchange) {
                String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
                if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                    return bearerToken.substring(7).trim();
                }
                return null;
            }
        });

        setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.matchers(
                ServerWebExchangeMatchers.pathMatchers("/api/secure/**"),
                ServerWebExchangeMatchers.pathMatchers("/api/admin/**")));
    }

}
