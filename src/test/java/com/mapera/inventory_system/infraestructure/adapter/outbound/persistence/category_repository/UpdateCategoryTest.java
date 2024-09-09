
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.category_repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationRepository;
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
public class UpdateCategoryTest {

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
        Mono<Void> deleteLocations = currencyRepository.deleteAll();

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
        Samples samples = new Samples();
        CategoryEntity category = samples.category();

        Mono<Long> savedCategoryId = categoryRepository.save(category).map(c -> c.getId());

        Mono<CategoryEntity> updatedCategory = savedCategoryId.flatMap(id -> {
            return categoryRepository.updateCategoryName(id, "Other");
        });

        StepVerifier.create(updatedCategory).expectNextMatches(c -> c.getName().equals("Other")).verifyComplete();
    }
}
