
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
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
public class SaveAndFindProductTest {

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
        AtomicReference<Long> subcategoryIdRef = new AtomicReference<>();
        CategoryEntity category = samples.category();
        SubcategoryEntity subcategory = samples.subcategory();
        ProductEntity product = samples.product();

        Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
            category.setUser_id(u.getId());
            subcategory.setUser_id(u.getId());
            product.setUser_id(u.getId());
        }).then();

        StepVerifier.create(savedUser).verifyComplete();

        Mono<ProductEntity> savedProductMono = categoryRepository.save(category)
                .flatMap(savedCategory -> {
                    subcategory.setCategory_id(savedCategory.getId());
                    return subcategoryRepository.save(subcategory);
                })
                .flatMap(savedSubcategory -> {
                    subcategoryIdRef.set(savedSubcategory.getId());
                    product.setSubcategory_id(savedSubcategory.getId());
                    return productRepository.save(product);
                });

        Mono<ProductEntity> foundProductMono = savedProductMono
                .flatMap(savedProduct -> productRepository.findById(savedProduct.getId()));

        StepVerifier.create(foundProductMono)
                .expectNextMatches(foundProduct -> foundProduct.getName().equals("Coca cola")
                        &&
                        foundProduct.getSubcategory_id() == subcategoryIdRef.get() &&
                        foundProduct.getMinimumStock() == 20 &&
                        foundProduct.getRetail_price() == 1 &&
                        foundProduct.getWholesale_price() == 0.75 &&
                        foundProduct.getProductPresentation().equals("Glass container 600 ml"))
                .verifyComplete();
    }
}
