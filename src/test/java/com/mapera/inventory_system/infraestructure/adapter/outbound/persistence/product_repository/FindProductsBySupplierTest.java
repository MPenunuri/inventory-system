package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductSupplierEntity;
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
public class FindProductsBySupplierTest {

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
                AtomicReference<Long> supplierIdRef = new AtomicReference<>();
                Samples samples = new Samples();
                UserEntity userEntity = samples.user();
                CategoryEntity category = samples.category();
                SubcategoryEntity subcategory = samples.subcategory();
                SupplierEntity supplier = new SupplierEntity();
                supplier.setName("International enterprise");
                ProductSupplierEntity productSupplierEntity = new ProductSupplierEntity();

                ProductEntity[] products = new ProductEntity[3];
                products[0] = samples.product();
                products[1] = samples.product();
                products[2] = samples.product();

                Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
                        category.setUser_id(u.getId());
                        subcategory.setUser_id(u.getId());
                        supplier.setUser_id(u.getId());
                        productSupplierEntity.setUser_id(u.getId());
                        products[0].setUser_id(u.getId());
                        products[1].setUser_id(u.getId());
                        products[2].setUser_id(u.getId());
                }).then();

                StepVerifier.create(savedUser).verifyComplete();

                Flux<ProductEntity> productFlux = Flux.fromArray(products);

                Mono<Long> savedCategoryIdMono = categoryRepository.save(category)
                                .map(savedCategory -> savedCategory.getId());

                Mono<Long> savedSubcategoryIdMono = savedCategoryIdMono
                                .flatMap(categoryId -> {
                                        subcategory.setCategory_id(categoryId);
                                        return subcategoryRepository.save(subcategory);
                                })
                                .map(savedSubcategory -> savedSubcategory.getId());

                Mono<Long> savedSupplierIdMono = supplierRepository.save(supplier)
                                .map(savedSupplier -> {
                                        supplierIdRef.set(savedSupplier.getId());
                                        return savedSupplier.getId();
                                });

                Mono<Void> saveProductsMono = savedSupplierIdMono
                                .flatMap(supplierID -> savedSubcategoryIdMono
                                                .flatMapMany(subcategoryId -> productFlux
                                                                .doOnNext(product -> product
                                                                                .setSubcategory_id(subcategoryId))
                                                                .flatMap(product -> productRepository.save(product)
                                                                                .flatMap(savedProduct -> {
                                                                                        productSupplierEntity
                                                                                                        .setProductId(savedProduct
                                                                                                                        .getId());
                                                                                        productSupplierEntity
                                                                                                        .setSupplierId(supplierID);
                                                                                        return productSupplierRepository
                                                                                                        .save(productSupplierEntity);
                                                                                })))
                                                .then())
                                .then();

                StepVerifier.create(saveProductsMono).verifyComplete();

                Flux<SupplierProductDTO> found = productRepository.findProductsBySupplierId(
                                supplier.getUser_id(), supplierIdRef.get());

                StepVerifier.create(found)
                                .expectNextCount(3)
                                .verifyComplete();

        }
}
