package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.security.SignupService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.UserLoginRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.UserSignupRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;
    @Autowired
    private SignupService signupService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserEntity> signup(@RequestBody UserSignupRequest request) {
        return signupService.signup(
                request.getUsername(), request.getEmail(), request.getPassword());
    }

    @PostMapping("/hello")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> hello() {
        return Mono.just("Hello");
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> login(@RequestBody UserLoginRequest request) {
        return authService.authenticate(
                request.getEmail(), request.getPassword());
    }

}
