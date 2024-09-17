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

import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierPatchRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierRegisterRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class SupplierControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    ProductSupplierRepository productSupplierRepository;

    @Autowired
    MovementRepository movementRepository;

    @BeforeEach
    public void setup() {
        Mono<Void> deleteUsers = userRepository.deleteAll();
        Mono<Void> deleteProductSupplierRelations = productSupplierRepository.deleteAll();
        Mono<Void> deleteSuppliers = supplierRepository.deleteAll();
        Mono<Void> deleteMovements = movementRepository.deleteAll();

        Mono<Void> setUp = deleteUsers
                .then(deleteProductSupplierRelations)
                .then(deleteMovements)
                .then(deleteSuppliers)
                .then();

        setUp.block();
    }

    @Test
    public void test() {
        SupplierRegisterRequest supplierRegisterRequest = new SupplierRegisterRequest();
        supplierRegisterRequest.setName("Solutions S.A.");

        SupplierPatchRequest supplierPatchRequest = new SupplierPatchRequest();
        supplierPatchRequest.setName("New name solutions S.A.");

        // Request with no token
        webTestClient.post()
                .uri("/api/secure/supplier")
                .bodyValue(supplierRegisterRequest)
                .exchange()
                .expectStatus().isUnauthorized();

        // Request with valid token
        AtomicReference<String> token = new AtomicReference<>();
        AuthTestUtil.performSignupAndVerify(webTestClient);
        AuthTestUtil.performLoginAndVerify(webTestClient, token);

        webTestClient.post()
                .uri("/api/secure/supplier")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(supplierRegisterRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SupplierEntity.class)
                .consumeWith(response -> {
                    SupplierEntity responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    if (responseBody != null) {
                        supplierPatchRequest.setId(responseBody.getId());
                    }
                });
        ;

        webTestClient.patch().uri("/api/secure/supplier")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(supplierPatchRequest)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/api/secure/supplier")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SupplierEntity.class)
                .hasSize(1);

        webTestClient.delete().uri("/api/secure/supplier/" + supplierPatchRequest.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange().expectStatus().isOk();

        webTestClient.get().uri("/api/secure/supplier")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isBadRequest();

    }
}
