package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.user.UserData;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserCrudRepository;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/")
public class Secure {

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    private AuthenticationService authService;

    @GetMapping
    public Mono<TokenValidation> validateToken() {
        return Mono.just(new TokenValidation());
    }

    @GetMapping("quotas")
    public Mono<UserData> getQuotas() {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return userCrudRepository.getUserInfo(userId);
        });

    }
}
