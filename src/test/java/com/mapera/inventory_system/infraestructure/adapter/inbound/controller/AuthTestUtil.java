package com.mapera.inventory_system.infraestructure.adapter.inbound.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.springframework.test.web.reactive.server.WebTestClient;

import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.user.UserLoginRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.user.UserSignupRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import java.util.concurrent.atomic.AtomicReference;

public class AuthTestUtil {

    public static UserSignupRequest createSignupRequest() {
        UserSignupRequest signupRequest = new UserSignupRequest();
        signupRequest.setUsername("usertest");
        signupRequest.setEmail("email@test.com");
        signupRequest.setPassword("test");
        return signupRequest;
    }

    public static UserLoginRequest createLoginRequest() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail("email@test.com");
        loginRequest.setPassword("test");
        return loginRequest;
    }

    public static void performSignupAndVerify(WebTestClient webTestClient) {
        webTestClient.post()
                .uri("/api/auth/signup")
                .bodyValue(
                        createSignupRequest())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserEntity.class)
                .consumeWith(response -> {
                    UserEntity responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    if (responseBody != null) {
                        assertEquals("usertest", responseBody.getUsername());
                        assertEquals("email@test.com", responseBody.getEmail());
                        assertNotEquals("test", responseBody.getPassword());
                    }
                });
    }

    public static void performLoginAndVerify(WebTestClient webTestClient,
            AtomicReference<String> token) {
        webTestClient.post()
                .uri("/api/auth/login")
                .bodyValue(
                        createLoginRequest())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    token.set(responseBody);
                });
    }
}
