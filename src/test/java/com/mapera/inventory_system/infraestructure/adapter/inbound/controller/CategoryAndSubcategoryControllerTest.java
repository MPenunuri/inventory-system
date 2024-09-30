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

import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.domain.entity.Subcategory;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.category.PatchCategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.category.RegisterCategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory.PatchSubategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory.RegisterSubcategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.category.CategoriesAndSubcategoriesDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
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
public class CategoryAndSubcategoryControllerTest {
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

                // Test get categories and subcategories method

                webTestClient.get()
                                .uri("/api/secure/category/with-subcategories-info")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get()).exchange()
                                .expectStatus().isOk()
                                .expectBodyList(CategoriesAndSubcategoriesDTO.class)
                                .hasSize(2);

                // Test delete methods

                webTestClient.delete()
                                .uri("/api/secure/category/" + category1Id.get())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get()).exchange()
                                .expectStatus().isOk();

                webTestClient.delete()
                                .uri("/api/secure/category/" + category2Id.get())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get()).exchange()
                                .expectStatus().is4xxClientError();

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
