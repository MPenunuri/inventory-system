
package com.mapera.inventory_system.infrastructure.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationManager(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        if (jwtTokenProvider.validateToken(token)) {
            Claims claims = jwtTokenProvider.getClaimsFromToken(token);
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);
            return Mono.just(new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
        } else {
            return Mono.empty();
        }
    }

}
