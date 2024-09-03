package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.stock_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.StockEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class StockRepositoryCustomMethods {
        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private SubcategoryRepository subcategoryRepository;

        @Autowired
        private LocationRepository locationRepository;

        @Autowired
        private StockRepository stockRepository;

        @Autowired
        private ProductSupplierRepository productSupplierRepository;

        @BeforeEach
        void setUp() {
                Mono<Void> deleteProductSupplier = productSupplierRepository.deleteAll();
                Mono<Void> deleteStocklist = stockRepository.deleteAll();
                Mono<Void> deleteLocations = locationRepository.deleteAll();
                Mono<Void> deleteProducts = productRepository.deleteAll();
                Mono<Void> deleteSubcategories = subcategoryRepository.deleteAll();
                Mono<Void> deleteCategories = categoryRepository.deleteAll();

                Mono<Void> setup = deleteProductSupplier
                                .then(deleteStocklist)
                                .then(deleteLocations)
                                .then(deleteProducts)
                                .then(deleteSubcategories)
                                .then(deleteCategories);

                setup.block();
        }

        @Test
        public void test() {
                AtomicReference<Long> productIdRef = new AtomicReference<>();
                AtomicReference<Long> locationIdRef = new AtomicReference<>();
                Samples samples = new Samples();
                CategoryEntity category = samples.category();
                SubcategoryEntity subcategory = samples.subcategory();
                ProductEntity product = samples.product();
                LocationEntity location = new LocationEntity();
                location.setName("Location sample");

                Mono<Long> savedSubcategoryId = categoryRepository.save(category)
                                .flatMap(savedCategory -> {
                                        subcategory.setCategory_id(savedCategory.getId());
                                        return subcategoryRepository.save(subcategory)
                                                        .map(savedSubcategory -> {
                                                                return savedSubcategory.getId();
                                                        });

                                });

                Mono<Void> savedProduct = savedSubcategoryId.flatMap(
                                subcategoryId -> {
                                        product.setSubcategory_id(subcategoryId);
                                        return productRepository.save(product).doOnNext(prod -> {
                                                productIdRef.set(prod.getId());
                                        }).then();
                                });

                Mono<Void> savedLocation = locationRepository.save(location)
                                .doOnNext(l -> locationIdRef.set(l.getId()))
                                .then();

                StepVerifier.create(savedProduct).verifyComplete();
                StepVerifier.create(savedLocation).verifyComplete();

                Mono<Boolean> addedStock = stockRepository
                                .addProductStockInLocation(locationIdRef.get(),
                                                productIdRef.get(), 20, 30);

                Mono<Boolean> removedStock = stockRepository
                                .removeProductStockInLocation(locationIdRef.get(),
                                                productIdRef.get());

                StepVerifier.create(addedStock).expectNext(true).verifyComplete();

                Mono<StockEntity> foundStock = stockRepository
                                .findProductStockInLocation(
                                                productIdRef.get(), locationIdRef.get());

                Mono<StockEntity> increseStock = foundStock.flatMap(stock -> {
                        return stockRepository.increseQuantityInStock(stock.getId(), 5);
                });

                Mono<StockEntity> decreseStock = foundStock.flatMap(stock -> {
                        return stockRepository.decreseQuantityInStock(stock.getId(), 5);
                });

                StepVerifier.create(increseStock)
                                .expectNextMatches(stock -> stock.getQuantity() == 25).verifyComplete();

                StepVerifier.create(
                                decreseStock)
                                .expectNextMatches(stock -> stock.getQuantity() == 20).verifyComplete();

                StepVerifier.create(removedStock).expectNext(true).verifyComplete();
        }
}
