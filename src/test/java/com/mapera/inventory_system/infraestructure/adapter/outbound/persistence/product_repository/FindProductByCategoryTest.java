package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class FindProductByCategoryTest {

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
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductSupplierRepository productSupplierRepository;

    @BeforeEach
    void setUp() {
        Mono<Void> deleteProductSupplier = productSupplierRepository.deleteAll();
        Mono<Void> deleteSuppliers = supplierRepository.deleteAll();
        Mono<Void> deleteStocklist = stockRepository.deleteAll();
        Mono<Void> deleteLocations = locationRepository.deleteAll();
        Mono<Void> deleteProducts = productRepository.deleteAll();
        Mono<Void> deleteSubcategories = subcategoryRepository.deleteAll();
        Mono<Void> deleteCategories = categoryRepository.deleteAll();

        Mono<Void> setup = deleteProductSupplier
                .then(deleteSuppliers)
                .then(deleteStocklist)
                .then(deleteLocations)
                .then(deleteProducts)
                .then(deleteSubcategories)
                .then(deleteCategories);

        setup.block();
    }

    @Test
    public void test() {
        Samples samples = new Samples();
        CategoryEntity category = samples.category();
        SubcategoryEntity subcategory = samples.subcategory();
        ProductEntity product = samples.product();

        categoryRepository.save(category)
                .flatMap(savedCategory -> {
                    subcategory.setCategory_id(savedCategory.getId());
                    return subcategoryRepository.save(subcategory)
                            .map(savedSubcategory -> {
                                product.setSubcategory_id(savedSubcategory.getId());
                                return savedCategory.getId();
                            });
                })
                .flatMapMany(categoryId -> productRepository.save(product)
                        .thenMany(productRepository.findProductsByCategoryId(categoryId)))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

    }

}
