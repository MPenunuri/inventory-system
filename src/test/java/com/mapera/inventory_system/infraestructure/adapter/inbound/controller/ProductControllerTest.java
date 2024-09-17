package com.mapera.inventory_system.infraestructure.adapter.inbound.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductPostRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ProductControllerTest {

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

        ProductPostRequest productPostRequest = new ProductPostRequest();
        productPostRequest.setName("Coca cola");

        // Request with no token
        webTestClient.post()
                .uri("/api/secure/product")
                .bodyValue(productPostRequest)
                .exchange()
                .expectStatus().isUnauthorized();

        // Request with invalid token
        String fakeToken = "invalid.token.here";
        webTestClient.post()
                .uri("/api/secure/product")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + fakeToken)
                .bodyValue(productPostRequest)
                .exchange()
                .expectStatus().isUnauthorized();

        // Request with valid token
        AtomicReference<String> token = new AtomicReference<>();
        AuthTestUtil.performSignupAndVerify(webTestClient);
        AuthTestUtil.performLoginAndVerify(webTestClient, token);

        webTestClient.post()
                .uri("/api/secure/product")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productPostRequest)
                .exchange()
                .expectStatus().isOk();

    }

}
