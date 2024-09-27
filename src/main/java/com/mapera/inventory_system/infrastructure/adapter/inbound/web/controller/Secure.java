package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/")
public class Secure {

    @GetMapping
    public Mono<TokenValidation> validateToken() {
        return Mono.just(new TokenValidation());
    }
}
