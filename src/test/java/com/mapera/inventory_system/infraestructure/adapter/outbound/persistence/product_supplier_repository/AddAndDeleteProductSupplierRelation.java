package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_supplier_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class AddAndDeleteProductSupplierRelation {

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductSupplierRepository productSupplierRepository;

    @BeforeEach
    void setUp() {
        Mono<Void> deleteMovements = movementRepository.deleteAll();
        Mono<Void> deleteProductSupplier = productSupplierRepository.deleteAll();
        Mono<Void> deleteSuppliers = supplierRepository.deleteAll();
        Mono<Void> deleteStocklist = stockRepository.deleteAll();
        Mono<Void> deleteProducts = productRepository.deleteAll();
        Mono<Void> deleteSubcategories = subcategoryRepository.deleteAll();
        Mono<Void> deleteCategories = categoryRepository.deleteAll();

        Mono<Void> setup = deleteProductSupplier
                .then(deleteStocklist)
                .then(deleteMovements)
                .then(deleteSuppliers)
                .then(deleteProducts)
                .then(deleteSubcategories)
                .then(deleteCategories);

        setup.block();
    }

    @Test
    public void test() {

        AtomicReference<Long> productIdRef = new AtomicReference<>();
        AtomicReference<Long> supplierIdRef = new AtomicReference<>();
        Samples samples = new Samples();
        CategoryEntity category = samples.category();
        SubcategoryEntity subcategory = samples.subcategory();
        ProductEntity product = samples.product();
        SupplierEntity supplier = new SupplierEntity();
        supplier.setName("International enterprise");

        Mono<Long> savedCategoryIdMono = categoryRepository.save(category)
                .map(savedCategory -> savedCategory.getId());

        Mono<Long> savedSubcategoryIdMono = savedCategoryIdMono
                .flatMap(categoryId -> {
                    subcategory.setCategory_id(categoryId);
                    return subcategoryRepository.save(subcategory);
                })
                .map(savedSubcategory -> savedSubcategory.getId());

        Mono<Void> savedProductMono = savedSubcategoryIdMono
                .flatMap(subcategoryId -> {
                    product.setSubcategory_id(subcategoryId);
                    return productRepository.save(product).doOnNext(p -> {
                        productIdRef.set(p.getId());
                    }).then();
                });

        Mono<Void> savedSupplierMono = supplierRepository.save(supplier)
                .doOnNext(savedSupplier -> {
                    supplierIdRef.set(savedSupplier.getId());
                })
                .then();

        Mono<Void> savedProductAndSupplier = savedProductMono.then(savedSupplierMono);

        StepVerifier.create(savedProductAndSupplier).verifyComplete();

        Mono<Boolean> savedRelation = productSupplierRepository
                .addProductSupplierRelation(productIdRef.get(), supplierIdRef.get());

        StepVerifier.create(savedRelation).expectNext(true).verifyComplete();
        StepVerifier.create(savedRelation).expectNext(false).verifyComplete();

        Mono<Boolean> deletedRelation = productSupplierRepository
                .deleteProductSupplierRelation(productIdRef.get(), supplierIdRef.get());

        StepVerifier.create(deletedRelation).expectNext(true).verifyComplete();
        StepVerifier.create(deletedRelation).expectNext(false).verifyComplete();

    }

}
