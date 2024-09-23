
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
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
public class UpdateProduct {

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
                Samples samples = new Samples();
                UserEntity userEntity = samples.user();
                AtomicReference<Long> nextSubcategoryIdRef = new AtomicReference<>();
                CategoryEntity category = samples.category();
                SubcategoryEntity prevSubcategory = samples.subcategory();
                SubcategoryEntity nextSubcategory = samples.subcategory();
                nextSubcategory.setName("Light soda");
                ProductEntity product = samples.product();

                AtomicReference<Long> prevCurrencyIdRef = new AtomicReference<>();
                CurrencyEntity currency1 = new CurrencyEntity();
                currency1.setName("USD");
                AtomicReference<Long> nextCurrencyIdRef = new AtomicReference<>();
                CurrencyEntity currency2 = new CurrencyEntity();
                currency2.setName("MXN");

                Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
                        category.setUser_id(u.getId());
                        prevSubcategory.setUser_id(u.getId());
                        nextSubcategory.setUser_id(u.getId());
                        product.setUser_id(u.getId());
                        currency1.setUser_id(u.getId());
                        currency2.setUser_id(u.getId());
                }).then();

                StepVerifier.create(savedUser).verifyComplete();

                Mono<Void> savedPrevCurrency = currencyRepository.save(currency1)
                                .doOnNext(c -> prevCurrencyIdRef.set(c.getId())).then();

                Mono<Void> savedNextCurrency = currencyRepository.save(currency2)
                                .doOnNext(c -> nextCurrencyIdRef.set(c.getId())).then();

                Mono<Long> savedPrevSubcategoryId = categoryRepository.save(category)
                                .flatMap(savedCategory -> {
                                        nextSubcategory.setCategory_id(savedCategory.getId());
                                        prevSubcategory.setCategory_id(savedCategory.getId());
                                        return subcategoryRepository.save(nextSubcategory)
                                                        .flatMap(savedSubcategory -> {
                                                                nextSubcategoryIdRef.set(savedSubcategory.getId());
                                                                return subcategoryRepository.save(prevSubcategory)
                                                                                .map(savedPrevSubcategory -> savedPrevSubcategory
                                                                                                .getId());
                                                        });

                                });

                Mono<Long> savedProductId = savedPrevSubcategoryId.flatMap(
                                subcategoryId -> {
                                        product.setSubcategory_id(subcategoryId);
                                        product.setPrice_currency_id(prevCurrencyIdRef.get());
                                        return productRepository.save(product).map(prod -> {
                                                return prod.getId();
                                        });
                                });

                Mono<ProductEntity> updatedProduct = savedProductId
                                .flatMap(productId -> {
                                        return productRepository.updateProductName(
                                                        product.getUser_id(), productId, "Fanta light")
                                                        .thenReturn(productId);
                                })
                                .flatMap(productId -> {
                                        return productRepository.updateSubcategory(
                                                        product.getUser_id(), productId, nextSubcategoryIdRef.get())
                                                        .thenReturn(productId);
                                })
                                .flatMap(productId -> {
                                        return productRepository.updateProductPresentation(
                                                        product.getUser_id(), productId, "Plastic bottle 600ml")
                                                        .thenReturn(productId);
                                })
                                .flatMap(productId -> {
                                        return productRepository.updateMinimumStock(
                                                        product.getUser_id(), productId, 30).thenReturn(productId);
                                })
                                .flatMap(productId -> {
                                        return productRepository.updateRetailPrice(
                                                        product.getUser_id(), productId, 1.25).thenReturn(productId);
                                })
                                .flatMap(productId -> {
                                        return productRepository.updateWholesalePrice(
                                                        product.getUser_id(), productId, 1.00).thenReturn(productId);
                                })
                                .flatMap(productId -> {
                                        return productRepository.updatePriceCurrency(
                                                        product.getUser_id(), productId, nextCurrencyIdRef.get());
                                });

                Mono<ProductEntity> updated = savedPrevCurrency.then(savedNextCurrency).then(updatedProduct);

                StepVerifier.create(
                                updated).expectNextMatches(
                                                p -> p.getName().equals("Fanta light") &&
                                                                p.getSubcategory_id() == nextSubcategoryIdRef.get() &&
                                                                p.getProductPresentation()
                                                                                .equals("Plastic bottle 600ml")
                                                                &&
                                                                p.getMinimumStock() == 30 &&
                                                                p.getRetail_price() == 1.25 &&
                                                                p.getWholesale_price() == 1.00 &&
                                                                p.getPrice_currency_id() == nextCurrencyIdRef.get())
                                .verifyComplete();

        }
}
