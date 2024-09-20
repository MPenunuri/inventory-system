package com.mapera.inventory_system.infraestructure.adapter.inbound.controller;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.user.UserLoginRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    UserCrudRepository userCrudRepository;

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

        AtomicReference<String> userToken = new AtomicReference<>();
        AuthTestUtil.performSignupAndVerify(webTestClient);
        AuthTestUtil.performLoginAndVerify(webTestClient, userToken);

        AtomicReference<String> adminToken = new AtomicReference<>();
        UserEntity adminEntity = new UserEntity();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        adminEntity.setUsername("mapera");
        adminEntity.setEmail("mapera@domain.com");
        adminEntity.setPassword(passwordEncoder.encode("123456"));
        adminEntity.setRoles("ADMIN");

        Mono<Void> savedAdmin = userCrudRepository.save(adminEntity).then();

        StepVerifier.create(savedAdmin).verifyComplete();

        UserLoginRequest adminLoginRequest = new UserLoginRequest();
        adminLoginRequest.setEmail("mapera@domain.com");
        adminLoginRequest.setPassword("123456");

        webTestClient.post()
                .uri("/api/auth/login")
                .bodyValue(adminLoginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    adminToken.set(responseBody);
                });

        webTestClient.get()
                .uri("/api/admin/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.get())
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/api/admin/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken.get())
                .exchange()
                .expectStatus().isForbidden();

        webTestClient.get()
                .uri("/api/admin/user")
                .exchange()
                .expectStatus().isUnauthorized();

        webTestClient.get()
                .uri("/api/admin/user/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.get())
                .exchange()
                .expectStatus().isOk();

        webTestClient.delete()
                .uri("/api/admin/user/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken.get())
                .exchange()
                .expectStatus().isOk();

    }
}
