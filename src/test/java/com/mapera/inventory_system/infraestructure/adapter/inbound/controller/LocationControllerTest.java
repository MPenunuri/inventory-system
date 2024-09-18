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

import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.location.PatchLocationRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.location.RegisterLocationRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class LocationControllerTest {
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
                RegisterLocationRequest registerLocationRequest = new RegisterLocationRequest();
                registerLocationRequest.setName("Cenrtal warehouse");

                // Request with invalid token
                String fakeToken = "invalid.token.here";
                webTestClient.post()
                                .uri("api/secure/location")
                                .bodyValue(registerLocationRequest)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + fakeToken)
                                .exchange()
                                .expectStatus().isUnauthorized();

                // Request with valid token
                AtomicReference<String> token = new AtomicReference<>();
                AuthTestUtil.performSignupAndVerify(webTestClient);
                AuthTestUtil.performLoginAndVerify(webTestClient, token);

                PatchLocationRequest patchLocationRequest = new PatchLocationRequest();
                patchLocationRequest.setName("Central warehouse");
                patchLocationRequest.setAddress("Liberty 183, Downtown, San Diego");

                webTestClient.post()
                                .uri("/api/secure/location")
                                .bodyValue(registerLocationRequest)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(LocationEntity.class)
                                .consumeWith(response -> {
                                        LocationEntity body = response.getResponseBody();
                                        assertNotEquals(body, null);
                                        if (body != null) {
                                                patchLocationRequest.setId(body.getId());
                                        }
                                });

                webTestClient.patch()
                                .uri("/api/secure/location/name")
                                .bodyValue(patchLocationRequest)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk();

                webTestClient.patch()
                                .uri("/api/secure/location/address")
                                .bodyValue(patchLocationRequest)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk();

                webTestClient.get()
                                .uri("/api/secure/location/" + patchLocationRequest.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(Location.class);

                webTestClient.get()
                                .uri("/api/secure/location")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk();

                webTestClient.delete()
                                .uri("/api/secure/location/" + patchLocationRequest.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk();
        }
}
