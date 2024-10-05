
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.movement_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AcquisitionDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AverageCostProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AverageSellProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.CustomerReturnDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.EntryMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.OutputMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.ProductionDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.SaleDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.StandardMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.SupplierReturnDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.TransferMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.StockEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class MovementsTest {

        @Autowired
        UserRepository userRepository;

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
                CategoryEntity categoryEntity = samples.category();
                SubcategoryEntity subcategoryEntity = samples.subcategory();
                ProductEntity productEntity = samples.product();
                SupplierEntity supplierEntity = new SupplierEntity();
                supplierEntity.setName("International corporative");
                LocationEntity locationEntity1 = new LocationEntity();
                locationEntity1.setName("Central warehouse");
                LocationEntity locationEntity2 = new LocationEntity();
                locationEntity2.setName("Secondary warehouse");
                CurrencyEntity currencyEntity = new CurrencyEntity();
                currencyEntity.setName("USD");

                // Save user

                AtomicReference<Long> userId = new AtomicReference<>();

                Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
                        userId.set(u.getId());
                        categoryEntity.setUser_id(u.getId());
                        subcategoryEntity.setUser_id(u.getId());
                        productEntity.setUser_id(u.getId());
                        supplierEntity.setUser_id(u.getId());
                        locationEntity1.setUser_id(u.getId());
                        locationEntity2.setUser_id(u.getId());
                        currencyEntity.setUser_id(u.getId());
                }).then();

                StepVerifier.create(savedUser).verifyComplete();

                // Define atomic references for id entities

                AtomicReference<Long> productId = new AtomicReference<>();
                AtomicReference<Long> supplierId = new AtomicReference<>();
                AtomicReference<Long> location1Id = new AtomicReference<>();
                AtomicReference<Long> location2Id = new AtomicReference<>();
                AtomicReference<Long> currencyId = new AtomicReference<>();

                // Saved entry data on corresponding repositories and set up atomic references

                Mono<Void> savedProduct = SaveProduct.execute(
                                categoryRepository,
                                subcategoryRepository,
                                productRepository,
                                currencyRepository,
                                categoryEntity,
                                subcategoryEntity,
                                productEntity,
                                currencyEntity,
                                currencyId,
                                productId);

                StepVerifier.create(savedProduct).verifyComplete();

                Mono<Void> savedSupplier = supplierRepository.save(supplierEntity)
                                .doOnNext(s -> supplierId.set(s.getId())).then();

                StepVerifier.create(savedSupplier).verifyComplete();

                Mono<Void> savedLocations = locationRepository.save(locationEntity1).flatMap(
                                savedLoc1 -> {
                                        location1Id.set(savedLoc1.getId());
                                        return locationRepository.save(locationEntity2).doOnNext(savedLoc2 -> {
                                                location2Id.set(savedLoc2.getId());
                                        });
                                }).then();

                StepVerifier.create(savedLocations).verifyComplete();

                // Set up Add movements tool

                AddMovements addMovements = new AddMovements(
                                userId.get(),
                                movementRepository,
                                productId.get(),
                                productEntity,
                                supplierId.get(),
                                location1Id.get(),
                                location2Id.get(),
                                currencyId.get());

                // Set up entry movements tests

                StepVerifier.create(addMovements.setEntries()).verifyComplete();

                Mono<StockProductDTO> entryProduct = productRepository.getProductStockById(userId.get(),
                                productId.get());

                StepVerifier.create(entryProduct)
                                .expectNextMatches(p -> p.getTotalStock() == 181)
                                .verifyComplete();

                Flux<EntryMovementDTO> entryFound = movementRepository.getEntries(userId.get());
                Flux<AcquisitionDTO> acquisitionFound = movementRepository.getAcquisitions(userId.get());
                Flux<CustomerReturnDTO> customerReturnFound = movementRepository.getCustomerReturns(userId.get());
                Flux<EntryMovementDTO> entryAdjusmentFound = movementRepository
                                .getEntryInventoryAdjustments(userId.get());
                Flux<ProductionDTO> productionFound = movementRepository.getProductions(userId.get());

                StepVerifier.create(entryFound).expectNextCount(10).verifyComplete();
                StepVerifier.create(acquisitionFound).expectNextCount(4).verifyComplete();
                StepVerifier.create(customerReturnFound).expectNextCount(1).verifyComplete();
                StepVerifier.create(entryAdjusmentFound).expectNextCount(1).verifyComplete();
                StepVerifier.create(productionFound).expectNextCount(4).verifyComplete();

                Flux<AcquisitionDTO> acquisitionsBySupplier = movementRepository
                                .getAcquisitionsBySupplierId(userId.get(), supplierId.get());

                StepVerifier.create(acquisitionsBySupplier).expectNextCount(4).verifyComplete();

                Flux<AcquisitionDTO> acquisitionsByCostAndYear = movementRepository
                                .findAcquisitionsByCostAndYear(userId.get(), "PER_UNIT",
                                                currencyId.get(), .75, 1, 2023, 2024);

                Flux<AverageCostProductDTO> acquisitionAVGCost1 = movementRepository
                                .getAvgUnitCostByAcquisition(userId.get(),
                                                productId.get(), currencyId.get(), 2022, 2025);

                Flux<AverageCostProductDTO> acquisitionAVGCost2 = movementRepository
                                .getAvgUnitCostByAcquisition(userId.get(),
                                                productId.get(), currencyId.get(), 2023, 2024);

                StepVerifier.create(acquisitionsByCostAndYear).expectNextCount(2).verifyComplete();

                StepVerifier.create(acquisitionAVGCost1).expectNextCount(1).verifyComplete();

                StepVerifier.create(acquisitionAVGCost1)
                                .expectNextMatches(product -> Math.abs(product.getAverageCostValue() - 0.81) < 0.0001)
                                .verifyComplete();

                StepVerifier.create(acquisitionAVGCost2)
                                .expectNextMatches(product -> Math.abs(product.getAverageCostValue()
                                                - 0.88) < 0.0001)
                                .verifyComplete();

                Flux<ProductionDTO> productionsByCostAndYear = movementRepository
                                .findProductionByCostAndYear(userId.get(),
                                                "PER_UNIT",
                                                currencyId.get(), .5, .75, 2023, 2025);

                Flux<AverageCostProductDTO> productionAVGCost1 = movementRepository
                                .getAvgUnitProductionCost(userId.get(),
                                                productId.get(), currencyId.get(), 2023, 2024);

                Flux<AverageCostProductDTO> productionAVGCost2 = movementRepository
                                .getAvgUnitProductionCost(userId.get(),
                                                productId.get(), currencyId.get(), 2022, 2024);

                StepVerifier.create(productionsByCostAndYear).expectNextCount(3).verifyComplete();
                StepVerifier.create(
                                productionAVGCost1)
                                .expectNextMatches(product -> Math.abs(product.getAverageCostValue()
                                                - 0.63) < 0.0001)
                                .verifyComplete();
                StepVerifier.create(
                                productionAVGCost2)
                                .expectNextMatches(product -> Math.abs(product.getAverageCostValue()
                                                - 0.5) < 0.0001)
                                .verifyComplete();

                // Set up output movements tests

                StepVerifier.create(addMovements.setOutputs()).verifyComplete();

                StepVerifier.create(entryProduct)
                                .expectNextMatches(p -> p.getTotalStock() == 70)
                                .verifyComplete();

                Flux<OutputMovementDTO> foundOutput = movementRepository.getOutputs(userId.get());
                Flux<SaleDTO> foundSale = movementRepository.getSales(userId.get());
                Flux<SupplierReturnDTO> foundSupplierReturn = movementRepository.getSupplierReturns(userId.get());
                Flux<OutputMovementDTO> foundOutputAdjustment = movementRepository
                                .getOutputInventoryAdjustments(userId.get());
                Flux<OutputMovementDTO> foundInternalConsumption = movementRepository
                                .getInternalConsumptionMovements(userId.get());

                StepVerifier.create(foundOutput).expectNextCount(7).verifyComplete();
                StepVerifier.create(foundSale).expectNextCount(4).verifyComplete();
                StepVerifier.create(foundSupplierReturn).expectNextCount(1).verifyComplete();
                StepVerifier.create(foundOutputAdjustment).expectNextCount(1).verifyComplete();
                StepVerifier.create(foundInternalConsumption).expectNextCount(1).verifyComplete();

                Flux<SaleDTO> sellByValueAndYear = movementRepository
                                .findSalesByValueAndYear(userId.get(), "RETAIL",
                                                currencyId.get(), 1, 1, 2022, 2025);

                StepVerifier.create(sellByValueAndYear).expectNextCount(2).verifyComplete();

                Flux<AverageSellProductDTO> avgUnitSellValue1 = movementRepository
                                .getAvgUnitSellValue(userId.get(), productId.get(), currencyId.get(), 2023, 2024);

                StepVerifier.create(avgUnitSellValue1)
                                .expectNextMatches(product -> Math.abs(product.getAverageSellValue()
                                                - 1.13) < 0.0001)
                                .verifyComplete();

                Flux<AverageSellProductDTO> avgUnitSellValue2 = movementRepository
                                .getAvgUnitSellValue(userId.get(), productId.get(), currencyId.get(), 2023, 2025);

                StepVerifier.create(avgUnitSellValue2)
                                .expectNextMatches(product -> Math.abs(product.getAverageSellValue()
                                                - 1.08) < 0.0001)
                                .verifyComplete();

                // Set up transfer movement

                StepVerifier.create(addMovements.setTransfers()).verifyComplete();

                Flux<TransferMovementDTO> foundTransfer = movementRepository.getTransfers(userId.get());

                StepVerifier.create(foundTransfer).expectNextCount(1).verifyComplete();

                // Verify if total stocks it's still the same
                StepVerifier.create(entryProduct)
                                .expectNextMatches(p -> p.getTotalStock() == 70)
                                .verifyComplete();

                // Verify if stock was correctly transfered
                Mono<StockEntity> stockOnAlternativeWarehouse = stockRepository.findProductStockInLocation(
                                productId.get(), location2Id.get(), userId.get());

                StepVerifier.create(stockOnAlternativeWarehouse).expectNextMatches(s -> s.getQuantity() == 10)
                                .verifyComplete();

                // Mono set cancelations

                StepVerifier.create(addMovements.setCancelations()).verifyComplete();

                Mono<StockEntity> stockOnCentralWarehouse = stockRepository.findProductStockInLocation(
                                productId.get(), location1Id.get(), userId.get());

                StepVerifier.create(stockOnAlternativeWarehouse).expectNextMatches(s -> s.getQuantity() == 0)
                                .verifyComplete();

                StepVerifier.create(
                                stockOnCentralWarehouse).expectNextMatches(s -> s.getQuantity() == 70)
                                .verifyComplete();

                StepVerifier.create(entryProduct)
                                .expectNextMatches(p -> p.getTotalStock() == 70)
                                .verifyComplete();

                // Verify number of tests

                Flux<StandardMovementDTO> foundMovement = movementRepository.getMovements(userId.get());

                StepVerifier.create(foundMovement).expectNextCount(17).verifyComplete();

                Flux<StandardMovementDTO> foundMovementsByProduct = movementRepository
                                .getMovementsByProductId(userId.get(), productId.get());

                StepVerifier.create(foundMovementsByProduct).expectNextCount(17).verifyComplete();

        }

}
