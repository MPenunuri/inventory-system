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

import com.mapera.inventory_system.domain.entity.Category;
import com.mapera.inventory_system.domain.entity.Subcategory;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.category.PatchCategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.category.RegisterCategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory.PatchSubategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory.RegisterSubcategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class CategoryAndSubcategoryControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MovementRepository movementRepository;

    @BeforeEach
    public void setup() {
        Mono<Void> deleteUsers = userRepository.deleteAll();
        Mono<Void> deleteMovements = movementRepository.deleteAll();
        Mono<Void> deleteProducts = productRepository.deleteAll();
        Mono<Void> deleteSubcategories = subcategoryRepository.deleteAll();
        Mono<Void> deleteCategories = categoryRepository.deleteAll();

        Mono<Void> setUp = deleteUsers
                .then(deleteMovements)
                .then(deleteProducts)
                .then(deleteSubcategories)
                .then(deleteCategories)
                .then();

        setUp.block();
    }

    @Test
    public void test() {

        // Define atomic references
        AtomicReference<Long> category1Id = new AtomicReference<>();
        AtomicReference<Long> category2Id = new AtomicReference<>();

        // Test category controller

        RegisterCategoryRequest registerCategoryRequest = new RegisterCategoryRequest();
        registerCategoryRequest.setName("Category name");

        // Request with no token
        webTestClient.post()
                .uri("/api/secure/category")
                .bodyValue(registerCategoryRequest)
                .exchange()
                .expectStatus().isUnauthorized();

        // Request with valid token
        AtomicReference<String> token = new AtomicReference<>();
        AuthTestUtil.performSignupAndVerify(webTestClient);
        AuthTestUtil.performLoginAndVerify(webTestClient, token);

        webTestClient.post()
                .uri("/api/secure/category")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(registerCategoryRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CategoryEntity.class)
                .consumeWith(response -> {
                    CategoryEntity responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    if (responseBody != null) {
                        category1Id.set(responseBody.getId());
                    }
                });
        ;

        // Register a new category for test patch category and subcategory methods

        PatchCategoryRequest patchCategoryRequest = new PatchCategoryRequest();
        patchCategoryRequest.setName("New category name");

        webTestClient.post()
                .uri("/api/secure/category")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(registerCategoryRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CategoryEntity.class)
                .consumeWith(response -> {
                    CategoryEntity responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    if (responseBody != null) {
                        category2Id.set(responseBody.getId());
                        patchCategoryRequest.setId(responseBody.getId());
                    }
                });
        ;

        webTestClient.patch()
                .uri("/api/secure/category")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(patchCategoryRequest)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get()
                .uri("/api/secure/category")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CategoryEntity.class)
                .hasSize(2);

        // The delete category method will be tested later in the file

        // Test subcategory controller

        RegisterSubcategoryRequest registerSubcategoryRequest = new RegisterSubcategoryRequest();
        registerSubcategoryRequest.setName("Subcategory name");
        registerSubcategoryRequest.setCategoryId(category1Id.get());

        PatchSubategoryRequest patchSubcategoryRequest = new PatchSubategoryRequest();
        patchSubcategoryRequest.setName("New subcategory name");
        patchSubcategoryRequest.setCategoryId(category2Id.get());

        webTestClient.post()
                .uri("/api/secure/subcategory")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(registerSubcategoryRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Subcategory.class)
                .consumeWith(response -> {
                    Subcategory responseBody = response.getResponseBody();
                    assertNotEquals(null, responseBody);
                    if (responseBody != null) {
                        patchSubcategoryRequest.setId(responseBody.getId());
                    }
                });
        ;

        webTestClient.get()
                .uri("/api/secure/subcategory")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Subcategory.class)
                .hasSize(1);

        webTestClient.patch()
                .uri("/api/secure/subcategory")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(patchSubcategoryRequest)
                .exchange()
                .expectStatus().isOk();

        webTestClient.patch()
                .uri("/api/secure/subcategory/category")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(patchSubcategoryRequest)
                .exchange()
                .expectStatus().isOk();

        // Test delete methods

        webTestClient.delete()
                .uri("/api/secure/category/" + category1Id.get())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get()).exchange()
                .expectStatus().isOk();

        webTestClient.delete()
                .uri("/api/secure/category/" + category2Id.get())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get()).exchange()
                .expectStatus().isBadRequest();

        webTestClient.delete()
                .uri("/api/secure/subcategory/" + patchSubcategoryRequest.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get()).exchange()
                .expectStatus().isOk();

        webTestClient.delete()
                .uri("/api/secure/category/" + category2Id.get())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get()).exchange()
                .expectStatus().isOk();

    }
}
