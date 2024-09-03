
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.category_repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")

@DataR2dbcTest
public class UpdateCategoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        Mono<Void> deleteProducts = productRepository.deleteAll();
        Mono<Void> deleteSubcategories = subcategoryRepository.deleteAll();
        Mono<Void> deleteCategories = categoryRepository.deleteAll();
        Mono<Void> setup = deleteProducts.then(deleteSubcategories).then(deleteCategories);

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
