package com.mapera.inventory_system.infraestructure.adapter.inbound.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.category.RegisterCategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.currency.RegisterCurrencyRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductPostRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductUpdateRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory.RegisterSubcategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.ProductSupplierRelationRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierRegisterRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    ProductSupplierRepository productSupplierRepository;

    @Autowired
    MovementRepository movementRepository;

    @BeforeEach
    public void setup() {
        Mono<Void> deleteStocks = stockRepository.deleteAll();
        Mono<Void> deleteProductSupplierRelations = productSupplierRepository.deleteAll();
        Mono<Void> deleteMovements = movementRepository.deleteAll();
        Mono<Void> deleteProducts = productRepository.deleteAll();
        Mono<Void> deleteUsers = userRepository.deleteAll();

        Mono<Void> setup = deleteStocks
                .then(deleteProductSupplierRelations)
                .then(deleteMovements)
                .then(deleteProducts)
                .then(deleteUsers)
                .then();
        setup.block();
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

        AtomicReference<Long> productId = new AtomicReference<>();
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();

        webTestClient.post()
                .uri("/api/secure/product")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productPostRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductEntity.class)
                .consumeWith(reponse -> {
                    ProductEntity body = reponse.getResponseBody();
                    assertNotEquals(null, body);
                    if (body != null) {
                        productId.set(body.getId());
                        productUpdateRequest.setId(body.getId());
                    }
                });

        // Test patch methods
        productUpdateRequest.setName("Fanta");

        webTestClient.patch()
                .uri("/api/secure/product/name")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productUpdateRequest)
                .exchange()
                .expectStatus().isOk();

        productUpdateRequest.setProductPresentation("600ml plastic bottle");

        webTestClient.patch()
                .uri("/api/secure/product/presentation")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productUpdateRequest)
                .exchange()
                .expectStatus().isOk();

        RegisterCategoryRequest registerCategoryRequest = new RegisterCategoryRequest();
        registerCategoryRequest.setName("Drink");

        RegisterSubcategoryRequest registerSubcategoryRequest = new RegisterSubcategoryRequest();
        registerSubcategoryRequest.setName("Soda");

        webTestClient.post()
                .uri("/api/secure/category")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(registerCategoryRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CategoryEntity.class)
                .consumeWith(reponse -> {
                    CategoryEntity body = reponse.getResponseBody();
                    assertNotEquals(null, body);
                    if (body != null) {
                        registerSubcategoryRequest.setCategoryId(body.getId());
                    }
                });

        webTestClient.post()
                .uri("/api/secure/subcategory")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(registerSubcategoryRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SubcategoryEntity.class)
                .consumeWith(reponse -> {
                    SubcategoryEntity body = reponse.getResponseBody();
                    assertNotEquals(null, body);
                    if (body != null) {
                        productUpdateRequest.setSubcategoryId(body.getId());
                    }
                });

        webTestClient.patch()
                .uri("/api/secure/product/subcategory")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productUpdateRequest)
                .exchange()
                .expectStatus().isOk();

        productUpdateRequest.setMinimumStock(80);

        webTestClient.patch()
                .uri("/api/secure/product/minimumStock")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productUpdateRequest)
                .exchange()
                .expectStatus().isOk();

        productUpdateRequest.setRetailPrice(1.00);

        webTestClient.patch()
                .uri("/api/secure/product/retailprice")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productUpdateRequest)
                .exchange()
                .expectStatus().isOk();

        productUpdateRequest.setWholesalePrice(.75);

        webTestClient.patch()
                .uri("/api/secure/product/wholesale")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productUpdateRequest)
                .exchange()
                .expectStatus().isOk();

        RegisterCurrencyRequest registerCurrencyRequest = new RegisterCurrencyRequest();
        registerCurrencyRequest.setName("MXN");

        webTestClient.post()
                .uri("/api/secure/currency")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(registerCurrencyRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CurrencyEntity.class)
                .consumeWith(response -> {
                    CurrencyEntity body = response.getResponseBody();
                    assertNotEquals(null, body);
                    if (body != null) {
                        productUpdateRequest.setPriceCurrencyId(body.getId());
                    }
                });

        webTestClient.patch()
                .uri("/api/secure/product/currency")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productUpdateRequest)
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/api/secure/product/" + productId.get())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBody(InventoryProduct.class);

        webTestClient.get().uri("/api/secure/product/all")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StandardProductDTO.class)
                .hasSize(1);

        webTestClient.get()
                .uri("/api/secure/product/category/" +
                        registerSubcategoryRequest.getCategoryId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StandardProductDTO.class)
                .hasSize(1);

        webTestClient.get()
                .uri("/api/secure/product/subcategory/" +
                        productUpdateRequest.getSubcategoryId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StandardProductDTO.class)
                .hasSize(1);

        SupplierRegisterRequest supplierRegisterRequest = new SupplierRegisterRequest();
        supplierRegisterRequest.setName("Solutions S.A.");

        ProductSupplierRelationRequest productSupplierRelationRequest = new ProductSupplierRelationRequest();
        productSupplierRelationRequest.setProductId(productId.get());

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
                        productSupplierRelationRequest.setSupplierId(
                                responseBody.getId());
                    }
                });

        webTestClient.post()
                .uri("/api/secure/supplier/product")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .bodyValue(productSupplierRelationRequest)
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get()
                .uri("/api/secure/product/supplier/" +
                        productSupplierRelationRequest.getSupplierId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SupplierProductDTO.class)
                .hasSize(1);

        webTestClient.get()
                .uri("/api/secure/product/stock/minimum")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StockProductDTO.class)
                .hasSize(1);

        webTestClient.get()
                .uri("/api/secure/product/stock/" + productId.get())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBody(StockProductDTO.class);

        webTestClient.get()
                .uri("/api/secure/product/price/retail?" +
                        "currencyId=" + productUpdateRequest.getPriceCurrencyId() + "&" +
                        "min=" + productUpdateRequest.getRetailPrice() + "&" +
                        "max=" + productUpdateRequest.getRetailPrice())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StandardProductDTO.class)
                .hasSize(1);

        webTestClient.get()
                .uri("/api/secure/product/price/retail?" +
                        "currencyId=" + productUpdateRequest.getPriceCurrencyId() + "&" +
                        "min=" + productUpdateRequest.getRetailPrice() + "&" +
                        "max=" + productUpdateRequest.getRetailPrice())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StandardProductDTO.class)
                .hasSize(1);

        webTestClient.get()
                .uri("/api/secure/product/price/wholesale?" +
                        "currencyId=" + productUpdateRequest.getPriceCurrencyId() + "&" +
                        "min=" + productUpdateRequest.getWholesalePrice() + "&" +
                        "max=" + productUpdateRequest.getWholesalePrice())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StandardProductDTO.class)
                .hasSize(1);

        webTestClient.delete()
                .uri("/api/secure/product/" + productId.get())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                .exchange()
                .expectStatus().is4xxClientError();

    }

}
