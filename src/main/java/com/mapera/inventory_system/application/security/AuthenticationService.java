
package com.mapera.inventory_system.application.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.UserPersistencePort;
import com.mapera.inventory_system.infrastructure.security.JwtTokenProvider;

import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {
    private final UserPersistencePort userRepository;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(UserPersistencePort userPersistencePort, JwtTokenProvider tokenProvider) {
        this.userRepository = userPersistencePort;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Mono<String> authenticate(String email, String password) {
        return userRepository.findUserByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    List<String> roles = Arrays.stream(user.getRoles().split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
                    return tokenProvider.generateToken(user.getId(), roles);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }

    public Mono<Long> getUserIdFromToken() {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No auth context")))
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .flatMap(name -> {
                    try {
                        Long userId = Long.parseLong(name);
                        return Mono.just(userId);
                    } catch (NumberFormatException e) {
                        return Mono.error(new IllegalArgumentException("Invalid userId"));
                    }
                })
                .doOnError(error -> Mono.error(new IllegalArgumentException("Invalid credentials")));
    }
}
