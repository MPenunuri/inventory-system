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

@ActiveProfiles("test")
@DataR2dbcTest
public class StockRepositoryCustomMethods {

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
                AtomicReference<Long> userIdRef = new AtomicReference<>();
                AtomicReference<Long> productIdRef = new AtomicReference<>();
                AtomicReference<Long> locationIdRef = new AtomicReference<>();
                Samples samples = new Samples();
                UserEntity userEntity = samples.user();
                CategoryEntity category = samples.category();
                SubcategoryEntity subcategory = samples.subcategory();
                ProductEntity product = samples.product();
                LocationEntity location = new LocationEntity();
                location.setName("Location sample");

                Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
                        userIdRef.set(u.getId());
                        category.setUser_id(u.getId());
                        subcategory.setUser_id(u.getId());
                        product.setUser_id(u.getId());
                        location.setUser_id(u.getId());
                }).then();

                StepVerifier.create(savedUser).verifyComplete();

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
                                .addProductStockInLocation(userIdRef.get(),
                                                locationIdRef.get(),
                                                productIdRef.get(),
                                                20,
                                                30);

                Mono<Boolean> removedStock = stockRepository
                                .removeProductStockInLocation(userIdRef.get(),
                                                locationIdRef.get(),
                                                productIdRef.get());

                StepVerifier.create(addedStock).expectNext(true).verifyComplete();

                Mono<StockEntity> foundStock = stockRepository
                                .findProductStockInLocation(
                                                productIdRef.get(), locationIdRef.get(), userIdRef.get());

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
