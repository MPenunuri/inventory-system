package com.mapera.inventory_system.application.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("test")
public class SecurityApplicationServices {

    @Autowired
    SignupService signupService;
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        Mono<Void> deleteUsers = userRepository.deleteAll();
        deleteUsers.block();
    }

    @Test
    public void test() {
        Mono<UserEntity> registerUser = signupService.signup("user", "email", "123456test");

        Mono<String> authUser = registerUser.flatMap(user -> {
            return authenticationService.authenticate(user.getEmail(), "123456test");
        });

        StepVerifier.create(authUser).expectNextCount(1).verifyComplete();
    }
}
