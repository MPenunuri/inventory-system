
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
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")

@DataR2dbcTest
public class MovementsTest {

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

                Mono<Void> setup = deleteProductSupplier
                                .then(deleteStocklist)
                                .then(deleteMovements)
                                .then(deleteSuppliers)
                                .then(deleteProducts)
                                .then(deleteSubcategories)
                                .then(deleteCategories)
                                .then(deleteCurrencies)
                                .then(deleteLocations);

                setup.block();
        }

        @Test
        public void test() {

                // Set up entry data
                Samples samples = new Samples();
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
                                movementRepository,
                                productId.get(),
                                productEntity,
                                supplierId.get(),
                                location1Id.get(),
                                location2Id.get(),
                                currencyId.get());

                // Set up entry movements tests

                StepVerifier.create(addMovements.setEntries()).verifyComplete();

                Mono<StockProductDTO> entryProduct = productRepository.getProductStockById(productId.get());

                StepVerifier.create(entryProduct)
                                .expectNextMatches(p -> p.getTotalStock() == 181)
                                .verifyComplete();

                Flux<EntryMovementDTO> entryFound = movementRepository.getEntries();
                Flux<AcquisitionDTO> acquisitionFound = movementRepository.getAcquisitions();
                Flux<CustomerReturnDTO> customerReturnFound = movementRepository.getCustomerReturns();
                Flux<EntryMovementDTO> entryAdjusmentFound = movementRepository.getEntryInventoryAdjustments();
                Flux<ProductionDTO> productionFound = movementRepository.getProductions();

                StepVerifier.create(entryFound).expectNextCount(10).verifyComplete();
                StepVerifier.create(acquisitionFound).expectNextCount(4).verifyComplete();
                StepVerifier.create(customerReturnFound).expectNextCount(1).verifyComplete();
                StepVerifier.create(entryAdjusmentFound).expectNextCount(1).verifyComplete();
                StepVerifier.create(productionFound).expectNextCount(4).verifyComplete();

                Flux<AcquisitionDTO> acquisitionsBySupplier = movementRepository
                                .getAcquisitionsBySupplierId(supplierId.get());

                StepVerifier.create(acquisitionsBySupplier).expectNextCount(4).verifyComplete();

                Flux<AcquisitionDTO> acquisitionsByCostAndYear = movementRepository
                                .findAcquisitionsByCostAndYear(
                                                currencyId.get(), .75, 1, 2023, 2024);

                Flux<AverageCostProductDTO> acquisitionAVGCost1 = movementRepository
                                .getAvgUnitCostByAcquisition(
                                                productId.get(), currencyId.get(), 2022, 2025);

                Flux<AverageCostProductDTO> acquisitionAVGCost2 = movementRepository
                                .getAvgUnitCostByAcquisition(
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
                                .findProductionByCostAndYear(
                                                currencyId.get(), .5, .75, 2023, 2025);

                Flux<AverageCostProductDTO> productionAVGCost1 = movementRepository
                                .getAvgUnitProductionCost(
                                                productId.get(), currencyId.get(), 2023, 2024);

                Flux<AverageCostProductDTO> productionAVGCost2 = movementRepository
                                .getAvgUnitProductionCost(
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

                Flux<OutputMovementDTO> foundOutput = movementRepository.getOutputs();
                Flux<SaleDTO> foundSale = movementRepository.getSales();
                Flux<SupplierReturnDTO> foundSupplierReturn = movementRepository.getSupplierReturns();
                Flux<OutputMovementDTO> foundOutputAdjustment = movementRepository.getOutputInventoryAdjustments();
                Flux<OutputMovementDTO> foundInternalConsumption = movementRepository.getInternalConsumptionMovements();

                StepVerifier.create(foundOutput).expectNextCount(7).verifyComplete();
                StepVerifier.create(foundSale).expectNextCount(4).verifyComplete();
                StepVerifier.create(foundSupplierReturn).expectNextCount(1).verifyComplete();
                StepVerifier.create(foundOutputAdjustment).expectNextCount(1).verifyComplete();
                StepVerifier.create(foundInternalConsumption).expectNextCount(1).verifyComplete();

                Flux<SaleDTO> sellByValueAndYear = movementRepository
                                .findSalesByValueAndYear(
                                                currencyId.get(), 1, 1, 2022, 2025);

                StepVerifier.create(sellByValueAndYear).expectNextCount(2).verifyComplete();

                Flux<AverageSellProductDTO> avgUnitSellValue1 = movementRepository
                                .getAvgUnitSellValue(productId.get(), currencyId.get(), 2023, 2024);

                StepVerifier.create(avgUnitSellValue1)
                                .expectNextMatches(product -> Math.abs(product.getAverageSellValue()
                                                - 1.13) < 0.0001)
                                .verifyComplete();

                Flux<AverageSellProductDTO> avgUnitSellValue2 = movementRepository
                                .getAvgUnitSellValue(productId.get(), currencyId.get(), 2023, 2025);

                StepVerifier.create(avgUnitSellValue2)
                                .expectNextMatches(product -> Math.abs(product.getAverageSellValue()
                                                - 1.08) < 0.0001)
                                .verifyComplete();

                // Set up transfer movement

                StepVerifier.create(addMovements.setTransfers()).verifyComplete();

                Flux<TransferMovementDTO> foundTransfer = movementRepository.getTransfers();

                StepVerifier.create(foundTransfer).expectNextCount(1).verifyComplete();

                // Verify if total stocks it's still the same
                StepVerifier.create(entryProduct)
                                .expectNextMatches(p -> p.getTotalStock() == 70)
                                .verifyComplete();

                // Verify if stock was correctly transfered
                Mono<StockEntity> stockOnAlternativeWarehouse = stockRepository.findProductStockInLocation(
                                productId.get(), location2Id.get());

                StepVerifier.create(stockOnAlternativeWarehouse).expectNextMatches(s -> s.getQuantity() == 10)
                                .verifyComplete();

                // Mono set cancelations

                StepVerifier.create(addMovements.setCancelations()).verifyComplete();

                Mono<StockEntity> stockOnCentralWarehouse = stockRepository.findProductStockInLocation(
                                productId.get(), location1Id.get());

                StepVerifier.create(stockOnAlternativeWarehouse).expectNextMatches(s -> s.getQuantity() == 0)
                                .verifyComplete();

                StepVerifier.create(
                                stockOnCentralWarehouse).expectNextMatches(s -> s.getQuantity() == 70)
                                .verifyComplete();

                StepVerifier.create(entryProduct)
                                .expectNextMatches(p -> p.getTotalStock() == 70)
                                .verifyComplete();

                // Verify number of tests

                Flux<StandardMovementDTO> foundMovement = movementRepository.getMovements();

                StepVerifier.create(foundMovement).expectNextCount(17).verifyComplete();

                Flux<StandardMovementDTO> foundMovementsByProduct = movementRepository
                                .getMovementsByProductId(productId.get());

                StepVerifier.create(foundMovementsByProduct).expectNextCount(17).verifyComplete();

        }

}
