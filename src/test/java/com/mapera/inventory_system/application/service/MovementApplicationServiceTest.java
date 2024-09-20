package com.mapera.inventory_system.application.service;

import java.util.concurrent.atomic.AtomicReference;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;
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
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("test")
public class MovementApplicationServiceTest {

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

                // Set up entry data
                Samples samples = new Samples();
                UserEntity userEntity = samples.user();
                ProductEntity productEntity = samples.product();
                SupplierEntity supplierEntity = new SupplierEntity();
                supplierEntity.setName("International corporative");
                LocationEntity locationEntity1 = new LocationEntity();
                locationEntity1.setName("Central warehouse");
                LocationEntity locationEntity2 = new LocationEntity();
                locationEntity2.setName("Alternative warehouse");
                CurrencyEntity currencyEntity = new CurrencyEntity();
                currencyEntity.setName("USD");

                // Define atomic references for id entities

                AtomicReference<Long> userId = new AtomicReference<>();
                AtomicReference<Long> productId = new AtomicReference<>();
                AtomicReference<Long> supplierId = new AtomicReference<>();
                AtomicReference<Long> location1Id = new AtomicReference<>();
                AtomicReference<Long> location2Id = new AtomicReference<>();
                AtomicReference<Long> currencyId = new AtomicReference<>();

                // Saved entry data on corresponding repositories and set up atomic references

                Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
                        userId.set(u.getId());
                        productEntity.setUser_id(u.getId());
                        supplierEntity.setUser_id(u.getId());
                        locationEntity1.setUser_id(u.getId());
                        locationEntity2.setUser_id(u.getId());
                        currencyEntity.setUser_id(u.getId());
                }).then();

                StepVerifier.create(savedUser).verifyComplete();

                Mono<Void> savedCurrency = currencyRepository.save(currencyEntity)
                                .doOnNext(c -> currencyId.set(c.getId())).then();

                StepVerifier.create(savedCurrency).verifyComplete();

                Mono<Void> savedSupplier = supplierRepository.save(supplierEntity)
                                .doOnNext(s -> supplierId.set(s.getId())).then();

                StepVerifier.create(savedSupplier).verifyComplete();

                Mono<Void> savedLocation = locationRepository.save(locationEntity1)
                                .doOnNext(l1 -> location1Id.set(l1.getId()))
                                .then(locationRepository.save(locationEntity2)
                                                .doOnNext(l2 -> location2Id.set(l2.getId()))
                                                .then());

                StepVerifier.create(savedLocation).verifyComplete();

                Mono<Void> savedProduct = productRepository.save(productEntity)
                                .doOnNext(p -> productId.set(p.getId())).then();

                StepVerifier.create(savedProduct).verifyComplete();

                Mono<Void> executeAcquisition = movementApplicationService
                                .addAcquisitionEntryMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(), "Regular acquisition",
                                                "No comment", 20, supplierId.get(), location1Id.get(),
                                                "OVERALL",
                                                200, currencyId.get())
                                .then();

                StepVerifier.create(executeAcquisition).verifyComplete();

                Mono<Void> executeCustomerReturn = movementApplicationService
                                .addCustomerReturnEntryMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(),
                                                "Product damaged", "No comment",
                                                20, location1Id.get(),
                                                "PER_UNIT",
                                                200, currencyId.get())
                                .then();

                StepVerifier.create(executeCustomerReturn).verifyComplete();

                Mono<Void> executeEntryAdjusment = movementApplicationService
                                .addInventoryAdjustmentEntryMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(), "Product finded",
                                                "No comment", 20, location1Id.get())
                                .then();

                StepVerifier.create(executeEntryAdjusment).verifyComplete();

                Mono<Void> executeProduction = movementApplicationService
                                .addProductionEntryMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(), "Regular production",
                                                "No comment", 20, location1Id.get(),
                                                "PER_UNIT", .5,
                                                currencyId.get())
                                .then();

                StepVerifier.create(executeProduction).verifyComplete();

                Mono<Void> executeSale = movementApplicationService
                                .addSalesOutputMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(), "Regular production",
                                                "No comment", 20, location1Id.get(),
                                                "WHOLESALE", productEntity.getWholesale_price(),
                                                currencyId.get())
                                .then();

                StepVerifier.create(executeSale).verifyComplete();

                Mono<Void> executeSupplierReturn = movementApplicationService
                                .addSupplierReturnOutputMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(), "Product damaged",
                                                "No comment", 20, supplierId.get(), location1Id.get(),
                                                "RETAIL", productEntity.getRetail_price(),
                                                currencyId.get())
                                .then();

                StepVerifier.create(executeSupplierReturn).verifyComplete();

                Mono<Void> executeOutputAdjusment = movementApplicationService
                                .addInventoryAdjustmentOutputMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(), "Product damaged",
                                                "No comment", 20, location1Id.get())
                                .then();

                StepVerifier.create(executeOutputAdjusment).verifyComplete();

                Mono<Void> executeInternalConsumption = movementApplicationService
                                .addInternalConsumptionOutputMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(), "Boos party",
                                                "No comment", 10, location1Id.get())
                                .then();

                StepVerifier.create(executeInternalConsumption).verifyComplete();

                Mono<Long> transferId = movementApplicationService
                                .addTransferMovement(userId.get(),
                                                productId.get(), LocalDateTime.now(), "Manager indication",
                                                "No comment", 10, location1Id.get(),
                                                location2Id.get())
                                .map(t -> t.getId());

                Mono<Boolean> cancelTransfer = transferId
                                .flatMap(id -> movementApplicationService.cancelMovementById(userId.get(), id));

                StepVerifier.create(cancelTransfer).expectNext(true).verifyComplete();
        }
}
