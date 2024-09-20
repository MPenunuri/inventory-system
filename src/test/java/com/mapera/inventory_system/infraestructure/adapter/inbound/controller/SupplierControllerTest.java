package com.mapera.inventory_system.infraestructure.adapter.inbound.controller;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.net.URI;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductPostRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.ProductSupplierRelationRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierPatchRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierRegisterRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
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
        MovementApplicationService movementApplicationService;

        @Autowired
        CategoryRepository categoryRepository;

        @Autowired
        SubcategoryRepository subcategoryRepository;

        @Autowired
        ProductRepository productRepository;

        @Autowired
        SupplierRepository supplierRepository;

        @Autowired
        ProductSupplierRepository productSupplierRepository;

        @Autowired
        LocationRepository locationRepository;

        @Autowired
        CurrencyRepository currencyRepository;

        @Autowired
        MovementRepository movementRepository;

        @Autowired
        StockRepository stockRepository;

        @BeforeEach
        void setUp() {
                Mono<Void> deleteMovements = movementRepository.deleteAll();
                Mono<Void> deleteProductSupplier = productSupplierRepository.deleteAll();
                Mono<Void> deleteSuppliers = supplierRepository.deleteAll();
                Mono<Void> deleteStocklist = stockRepository.deleteAll();
                Mono<Void> deleteProducts = productRepository.deleteAll();
                Mono<Void> deleteSubcategories = subcategoryRepository.deleteAll();
                Mono<Void> deleteCategories = categoryRepository.deleteAll();
                Mono<Void> deleteCurrencies = currencyRepository.deleteAll();
                Mono<Void> deleteLocations = locationRepository.deleteAll();
                Mono<Void> deleteUsers = userRepository.deleteAll();

                Mono<Void> setup = deleteProductSupplier
                                .then(deleteStocklist)
                                .then(deleteMovements)
                                .then(deleteSuppliers)
                                .then(deleteProducts)
                                .then(deleteSubcategories)
                                .then(deleteCategories)
                                .then(deleteCurrencies)
                                .then(deleteLocations)
                                .then(deleteUsers);

                setup.block();
        }

        @Test
        public void test() {
                SupplierRegisterRequest supplierRegisterRequest = new SupplierRegisterRequest();
                supplierRegisterRequest.setName("Solutions S.A.");

                SupplierPatchRequest supplierPatchRequest = new SupplierPatchRequest();
                supplierPatchRequest.setName("New name solutions S.A.");

                ProductSupplierRelationRequest productSupplierRelationRequest = new ProductSupplierRelationRequest();

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
                                                productSupplierRelationRequest.setSupplierId(
                                                                responseBody.getId());
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

                // Test add and delete product supplier relation methods, before testing delete
                // supplier method

                ProductPostRequest productPostRequest = new ProductPostRequest();
                productPostRequest.setName("Coca cola");

                webTestClient.post()
                                .uri("/api/secure/product")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(productPostRequest)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(ProductEntity.class)
                                .consumeWith(response -> {
                                        ProductEntity body = response.getResponseBody();
                                        if (body != null) {
                                                productSupplierRelationRequest.setProductId(
                                                                body.getId());
                                        }
                                });

                webTestClient.post()
                                .uri("/api/secure/supplier/product")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(productSupplierRelationRequest)
                                .exchange()
                                .expectStatus().isCreated();

                URI uri = UriComponentsBuilder
                                .fromPath("/api/secure/supplier/product")
                                .queryParam("productId", productSupplierRelationRequest.getProductId())
                                .queryParam("supplierId", productSupplierRelationRequest.getSupplierId())
                                .build()
                                .toUri();

                webTestClient.delete().uri("/api/secure/supplier/" + supplierPatchRequest.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange().expectStatus().is4xxClientError();

                webTestClient.delete()
                                .uri(uri)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk();

                webTestClient.delete().uri("/api/secure/supplier/" + supplierPatchRequest.getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange().expectStatus().isOk();

                webTestClient.get().uri("/api/secure/supplier")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isNotFound();

        }
}
