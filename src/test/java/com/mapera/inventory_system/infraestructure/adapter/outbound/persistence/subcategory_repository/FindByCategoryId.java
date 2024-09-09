package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.subcategory_repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
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
public class FindByCategoryId {

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
        CategoryEntity category = new CategoryEntity();
        category.setName("Electronics");
        SubcategoryEntity subcategory1 = new SubcategoryEntity();
        subcategory1.setName("Laptops");
        SubcategoryEntity subcategory2 = new SubcategoryEntity();
        subcategory2.setName("Smartphones");

        Mono<Long> savedCategoryId = categoryRepository.save(category).map(c -> c.getId());
        // savedSubcategories return categoryId
        Mono<Long> savedSubcategories = savedCategoryId
                .flatMap(id -> {
                    subcategory1.setCategory_id(id);
                    return subcategoryRepository.save(subcategory1).thenReturn(id);
                })
                .flatMap(id -> {
                    subcategory2.setCategory_id(id);
                    return subcategoryRepository.save(subcategory2).thenReturn(id);
                });

        // savedSubcategories return categoryId
        Flux<SubcategoryEntity> found = savedSubcategories
                .flatMapMany(id -> subcategoryRepository.findSubcategoriesByCategoryId(id));

        StepVerifier.create(found).expectNextCount(2).verifyComplete();

    }

}
