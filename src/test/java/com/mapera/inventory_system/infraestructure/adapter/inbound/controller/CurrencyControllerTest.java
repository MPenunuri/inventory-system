package com.mapera.inventory_system.infraestructure.adapter.inbound.controller;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.currency.PatchCurrencyRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.currency.RegisterCurrencyRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class CurrencyControllerTest {

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
        RegisterCurrencyRequest registerCurrencyRequest = new RegisterCurrencyRequest();
        registerCurrencyRequest.setName("UDS");

        // Request with invalid token
        String fakeToken = "invalid.token.here";
        webTestClient.post()
                .uri("api/secure/currency")
                .bodyValue(registerCurrencyRequest)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + fakeToken)
                .exchange()
                .expectStatus().isUnauthorized();

        // Request with valid token
        AtomicReference<String> token = new AtomicReference<>();
        AuthTestUtil.performSignupAndVerify(webTestClient);
        AuthTestUtil.performLoginAndVerify(webTestClient, token);

        PatchCurrencyRequest patchCurrencyRequest = new PatchCurrencyRequest();
        patchCurrencyRequest.setName("USD");

        webTestClient.post()
                .uri("/api/secure/currency")
                .bodyValue(registerCurrencyRequest)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CurrencyEntity.class)
                .consumeWith(response -> {
                    CurrencyEntity body = response.getResponseBody();
                    assertNotEquals(body, null);
                    if (body != null) {
                        patchCurrencyRequest.setId(body.getId());
                    }
                });

        webTestClient.patch()
                .uri("/api/secure/currency")
                .bodyValue(patchCurrencyRequest)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/api/secure/currency")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk();

        webTestClient.delete()
                .uri("/api/secure/currency/" + patchCurrencyRequest.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk();
    }
}
