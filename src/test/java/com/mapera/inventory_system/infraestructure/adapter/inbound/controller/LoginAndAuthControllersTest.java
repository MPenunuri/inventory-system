package com.mapera.inventory_system.infraestructure.adapter.inbound.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.UserLoginRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.UserSignupRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class LoginAndAuthControllersTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        Mono<Void> deleteUsers = userRepository.deleteAll();
        deleteUsers.block();
    }

    @Test
    public void test() {
        UserSignupRequest signupRequest = new UserSignupRequest();
        signupRequest.setUsername("usertest");
        signupRequest.setEmail("emailtest");
        signupRequest.setPassword("test");

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("emailtest");
        loginRequest.setPassword("test");

        webTestClient.post()
                .uri("/auth/signup")
                .bodyValue(signupRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserEntity.class)
                .consumeWith(response -> {
                    UserEntity responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    if (responseBody != null) {
                        assertEquals("usertest", responseBody.getUsername());
                        assertEquals("emailtest", responseBody.getEmail());
                        assertNotEquals("test", responseBody.getPassword());
                    }
                });

        webTestClient.post()
                .uri("/auth/login")
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    System.out.println(responseBody);
                });

    }
}
