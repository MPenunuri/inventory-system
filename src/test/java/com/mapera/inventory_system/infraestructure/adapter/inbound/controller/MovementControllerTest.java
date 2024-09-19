package com.mapera.inventory_system.infraestructure.adapter.inbound.controller;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.currency.RegisterCurrencyRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.location.RegisterLocationRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.movement.RegisterMovementRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductPostRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductUpdateRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierRegisterRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AcquisitionDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AverageCostProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.CustomerReturnDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.EntryMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.OutputMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.ProductionDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.SaleDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.StandardMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.SupplierReturnDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.TransferMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class MovementControllerTest {

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

                // GET AUTHORIZATION

                AtomicReference<String> token = new AtomicReference<>();
                AuthTestUtil.performSignupAndVerify(webTestClient);
                AuthTestUtil.performLoginAndVerify(webTestClient, token);

                // SET UP ENTRY DATA DTO REQUESTS

                RegisterCurrencyRequest registerCurrencyRequest = new RegisterCurrencyRequest();
                registerCurrencyRequest.setName("USD");

                ProductPostRequest productPostRequest = new ProductPostRequest();
                productPostRequest.setName("Coca cola");

                ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
                productUpdateRequest.setRetailPrice(1.00);
                productUpdateRequest.setWholesalePrice(.75);

                SupplierRegisterRequest supplierRegisterRequest = new SupplierRegisterRequest();
                supplierRegisterRequest.setName("International supplier");

                RegisterLocationRequest registerLocationRequest = new RegisterLocationRequest();
                registerLocationRequest.setName("Central warehouse");

                // REGISTER CURRENCY

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

                // REGISTER AND UPDATE PRODUCT

                AtomicReference<ProductEntity> product = new AtomicReference<>();

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
                                                productUpdateRequest.setId(body.getId());
                                        }
                                });

                webTestClient.patch()
                                .uri("/api/secure/product/currency")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(productUpdateRequest)
                                .exchange()
                                .expectStatus().isOk();

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
                                .expectStatus().isOk()
                                .expectBody(ProductEntity.class)
                                .consumeWith(reponse -> {
                                        ProductEntity body = reponse.getResponseBody();
                                        assertNotEquals(null, body);
                                        if (body != null) {
                                                product.set(body);
                                        }
                                });

                // Register supplier

                AtomicReference<Long> supplierId = new AtomicReference<>();

                webTestClient.post()
                                .uri("/api/secure/supplier")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(supplierRegisterRequest)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(SupplierEntity.class)
                                .consumeWith(reponse -> {
                                        SupplierEntity body = reponse.getResponseBody();
                                        assertNotEquals(null, body);
                                        if (body != null) {
                                                supplierId.set(body.getId());
                                        }
                                });

                // Register locations

                AtomicReference<Long> location1Id = new AtomicReference<>();
                AtomicReference<Long> location2Id = new AtomicReference<>();

                webTestClient.post()
                                .uri("/api/secure/location")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(registerLocationRequest)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(LocationEntity.class)
                                .consumeWith(reponse -> {
                                        LocationEntity body = reponse.getResponseBody();
                                        assertNotEquals(null, body);
                                        if (body != null) {
                                                location1Id.set(body.getId());
                                        }
                                });

                webTestClient.post()
                                .uri("/api/secure/location")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(registerLocationRequest)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(LocationEntity.class)
                                .consumeWith(reponse -> {
                                        LocationEntity body = reponse.getResponseBody();
                                        assertNotEquals(null, body);
                                        if (body != null) {
                                                location2Id.set(body.getId());
                                        }
                                });

                // Set up register movements requests

                RegisterMovementRequest acquisitionReq = new RegisterMovementRequest();
                acquisitionReq.setProductId(product.get().getId());
                acquisitionReq.setDateTime(LocalDateTime.of(2023, 12, 25, 10, 30, 45));
                acquisitionReq.setReason("Regular acquisition");
                acquisitionReq.setComment("No comment");
                acquisitionReq.setQuantity(38);
                acquisitionReq.setSupplierId(supplierId.get());
                acquisitionReq.setToLocationId(location1Id.get());
                acquisitionReq.setTransactionSubtype("PER_UNIT");
                acquisitionReq.setTransactionValue(.75);
                acquisitionReq.setTransactionCurrencyId(product.get().getPrice_currency_id());

                RegisterMovementRequest customerReturnReq = new RegisterMovementRequest();
                customerReturnReq.setProductId(product.get().getId());
                customerReturnReq.setDateTime(LocalDateTime.of(2023, 12, 29, 10, 30, 45));
                customerReturnReq.setReason("Damaged Product");
                customerReturnReq.setComment("No comment");
                customerReturnReq.setQuantity(2);
                customerReturnReq.setToLocationId(location1Id.get());
                customerReturnReq.setTransactionSubtype("PER_UNIT");
                customerReturnReq.setTransactionValue(.75);
                customerReturnReq.setTransactionCurrencyId(product.get().getPrice_currency_id());

                RegisterMovementRequest entryAdjusmentReq = new RegisterMovementRequest();
                entryAdjusmentReq.setProductId(product.get().getId());
                entryAdjusmentReq.setDateTime(LocalDateTime.of(2023, 12, 29, 10, 30, 45));
                entryAdjusmentReq.setReason("Lost product found");
                entryAdjusmentReq.setComment("No comment");
                entryAdjusmentReq.setQuantity(2);
                entryAdjusmentReq.setToLocationId(location1Id.get());

                RegisterMovementRequest productionReq = new RegisterMovementRequest();
                productionReq.setProductId(product.get().getId());
                productionReq.setDateTime(LocalDateTime.of(2023, 12, 29, 10, 30, 45));
                productionReq.setReason("Regular production");
                productionReq.setComment("No comment");
                productionReq.setQuantity(18);
                productionReq.setToLocationId(location1Id.get());
                productionReq.setTransactionSubtype("PER_UNIT");
                productionReq.setTransactionValue(.75);
                productionReq.setTransactionCurrencyId(product.get().getPrice_currency_id());

                RegisterMovementRequest saleReq = new RegisterMovementRequest();
                saleReq.setProductId(product.get().getId());
                saleReq.setDateTime(LocalDateTime.of(2024, 01, 29, 10, 30, 45));
                saleReq.setReason("Regular sale");
                saleReq.setComment("No comment");
                saleReq.setQuantity(18);
                saleReq.setFromLocationId(location1Id.get());
                saleReq.setTransactionSubtype("RETAIL");
                saleReq.setTransactionValue(1);
                saleReq.setTransactionCurrencyId(product.get().getPrice_currency_id());

                RegisterMovementRequest supplierReturnReq = new RegisterMovementRequest();
                supplierReturnReq.setProductId(product.get().getId());
                supplierReturnReq.setDateTime(LocalDateTime.of(2024, 01, 29, 10, 30, 45));
                supplierReturnReq.setReason("Damaged product");
                supplierReturnReq.setComment("No comment");
                supplierReturnReq.setQuantity(2);
                supplierReturnReq.setSupplierId(supplierId.get());
                supplierReturnReq.setFromLocationId(location1Id.get());
                supplierReturnReq.setTransactionSubtype("RETAIL");
                supplierReturnReq.setTransactionValue(1);
                supplierReturnReq.setTransactionCurrencyId(product.get().getPrice_currency_id());

                RegisterMovementRequest outputAdjustmentReq = new RegisterMovementRequest();
                outputAdjustmentReq.setProductId(product.get().getId());
                outputAdjustmentReq.setDateTime(LocalDateTime.of(2024, 01, 29, 10, 30, 45));
                outputAdjustmentReq.setReason("Lost product");
                outputAdjustmentReq.setComment("No comment");
                outputAdjustmentReq.setQuantity(2);
                outputAdjustmentReq.setFromLocationId(location1Id.get());

                RegisterMovementRequest internalConsumptionReq = new RegisterMovementRequest();
                internalConsumptionReq.setProductId(product.get().getId());
                internalConsumptionReq.setDateTime(LocalDateTime.of(2024, 01, 29, 10, 30, 45));
                internalConsumptionReq.setReason("Boss party");
                internalConsumptionReq.setComment("No comment");
                internalConsumptionReq.setQuantity(18);
                internalConsumptionReq.setFromLocationId(location1Id.get());

                RegisterMovementRequest transferReq = new RegisterMovementRequest();
                transferReq.setProductId(product.get().getId());
                transferReq.setDateTime(LocalDateTime.of(2024, 01, 29, 10, 30, 45));
                transferReq.setReason("Boss party");
                transferReq.setComment("No comment");
                transferReq.setQuantity(18);
                transferReq.setFromLocationId(location1Id.get());
                transferReq.setToLocationId(location2Id.get());

                // Register movements

                webTestClient.post()
                                .uri("/api/secure/movement/acquisition")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(acquisitionReq)
                                .exchange()
                                .expectStatus().isCreated();

                webTestClient.post()
                                .uri("/api/secure/movement/customer-return")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(customerReturnReq)
                                .exchange()
                                .expectStatus().isCreated();

                webTestClient.post()
                                .uri("/api/secure/movement/entry-adjusment")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(entryAdjusmentReq)
                                .exchange()
                                .expectStatus().isCreated();

                webTestClient.post()
                                .uri("/api/secure/movement/production")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(productionReq)
                                .exchange()
                                .expectStatus().isCreated();

                webTestClient.post()
                                .uri("/api/secure/movement/sale")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(saleReq)
                                .exchange()
                                .expectStatus().isCreated();

                webTestClient.post()
                                .uri("/api/secure/movement/supplier-return")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(supplierReturnReq)
                                .exchange()
                                .expectStatus().isCreated();

                webTestClient.post()
                                .uri("/api/secure/movement/output-adjustment")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(outputAdjustmentReq)
                                .exchange()
                                .expectStatus().isCreated();

                webTestClient.post()
                                .uri("/api/secure/movement/internal-consumption")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(internalConsumptionReq)
                                .exchange()
                                .expectStatus().isCreated();

                AtomicReference<Long> transferId = new AtomicReference<>();
                webTestClient.post()
                                .uri("/api/secure/movement/transfer")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .bodyValue(transferReq)
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(MovementEntity.class)
                                .consumeWith(reponse -> {
                                        MovementEntity body = reponse.getResponseBody();
                                        assertNotEquals(null, body);
                                        if (body != null) {
                                                transferId.set(body.getId());
                                        }
                                });

                // Test general get methods

                webTestClient.get()
                                .uri("/api/secure/movement")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(StandardMovementDTO.class)
                                .hasSize(9);

                webTestClient.get()
                                .uri("/api/secure/movement/entry")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(EntryMovementDTO.class)
                                .hasSize(4);

                webTestClient.get()
                                .uri("/api/secure/movement/output")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(OutputMovementDTO.class)
                                .hasSize(4);

                webTestClient.get()
                                .uri("/api/secure/movement/transfer")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(TransferMovementDTO.class).hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/product/" + product.get().getId())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(StandardMovementDTO.class)
                                .hasSize(9);

                // Test entry get methods

                webTestClient.get()
                                .uri("/api/secure/movement/acquisition")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(AcquisitionDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/customer-return")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(CustomerReturnDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/entry-adjustment")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(EntryMovementDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/production")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(ProductionDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/acquisition/supplier/" + supplierId.get())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(AcquisitionDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/acquisition/cost/" +
                                                product.get().getPrice_currency_id() + "?" +
                                                "costType=PER_UNIT" + "&" +
                                                "minCost=.75" + "&" +
                                                "maxCost=.75" + "&" +
                                                "fromYear=2023" + "&" +
                                                "toYear=2023")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(AcquisitionDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/acquisition/avg-unit/cost/" +
                                                product.get().getId() + "?" +
                                                "currencyId=" + product.get().getPrice_currency_id() + "&" +
                                                "fromYear=2023" + "&" +
                                                "toYear=2023")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(AverageCostProductDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/acquisition/avg-total/cost/" +
                                                product.get().getId() + "?" +
                                                "currencyId=" + product.get().getPrice_currency_id() + "&" +
                                                "fromYear=2023" + "&" +
                                                "toYear=2023")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isNotFound();

                webTestClient.get()
                                .uri("/api/secure/movement/production/cost/" +
                                                product.get().getPrice_currency_id() + "?" +
                                                "costType=PER_UNIT" + "&" +
                                                "minCost=.75" + "&" +
                                                "maxCost=.75" + "&" +
                                                "fromYear=2023" + "&" +
                                                "toYear=2023")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(AcquisitionDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/production/avg-unit/cost/" +
                                                product.get().getId() + "?" +
                                                "currencyId=" + product.get().getPrice_currency_id() + "&" +
                                                "fromYear=2023" + "&" +
                                                "toYear=2023")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(AverageCostProductDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/production/avg-total/cost/" +
                                                product.get().getId() + "?" +
                                                "currencyId=" + product.get().getPrice_currency_id() + "&" +
                                                "fromYear=2023" + "&" +
                                                "toYear=2023")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isNotFound();

                // Test output get methods

                webTestClient.get()
                                .uri("/api/secure/movement/sale")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(SaleDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/supplier-return")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(SupplierReturnDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/output-adjusment")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(OutputMovementDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/internal-consumption")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(OutputMovementDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/sales/" +
                                                product.get().getPrice_currency_id() + "?" +
                                                "sellType=RETAIL&" +
                                                "minValue=1&" +
                                                "maxValue=1&" +
                                                "fromYear=2024&" +
                                                "toYear=2024")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(SaleDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/sales/avg-unit/" +
                                                product.get().getId() + "?" +
                                                "currencyId=" +
                                                product.get().getPrice_currency_id() +
                                                "&" +
                                                "maxValue=1&" +
                                                "fromYear=2024&" +
                                                "toYear=2024")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(SaleDTO.class)
                                .hasSize(1);

                webTestClient.get()
                                .uri("/api/secure/movement/sales/avg-total/" +
                                                product.get().getId() + "?" +
                                                "currencyId=" +
                                                product.get().getPrice_currency_id() +
                                                "&" +
                                                "maxValue=1&" +
                                                "fromYear=2024&" +
                                                "toYear=2024")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isNotFound();

                // Cancel transfer movement

                webTestClient.delete()
                                .uri("/api/secure/movement/" + transferId.get())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.get())
                                .exchange()
                                .expectStatus().isOk();
        }
}
