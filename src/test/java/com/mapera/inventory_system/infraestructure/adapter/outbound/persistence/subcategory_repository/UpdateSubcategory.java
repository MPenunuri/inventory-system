package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.subcategory_repository;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class UpdateSubcategory {

        @Autowired
        CategoryRepository categoryRepository;

        @Autowired
        SubcategoryRepository subcategoryRepository;

        @Test
        public void test() {
                Samples samples = new Samples();
                CategoryEntity initialCategory = samples.category();
                CategoryEntity finalCategory = samples.category();
                finalCategory.setName("Sodas");
                SubcategoryEntity subcategory = samples.subcategory();
                String finalSubcategoryName = "Cola sodas";

                AtomicReference<Long> finalCategoryId = new AtomicReference<>();
                AtomicReference<Long> subcategoryId = new AtomicReference<>();

                Mono<Void> savedfinalCategory = categoryRepository.save(finalCategory)
                                .doOnSuccess(c -> finalCategoryId.set(c.getId()))
                                .then();

                Mono<SubcategoryEntity> savedSubcategory = categoryRepository.save(initialCategory)
                                .flatMap(savedCategory -> {
                                        subcategory.setCategory_id(savedCategory.getId());
                                        subcategoryId.set(savedCategory.getId());
                                        return subcategoryRepository.save(subcategory);
                                });

                Mono<SubcategoryEntity> updatedSubcategory = savedSubcategory
                                .flatMap(sub -> subcategoryRepository.renameSubcategory(
                                                sub.getId(), finalSubcategoryName))
                                .flatMap(sub -> subcategoryRepository.changeSubcategoryCategory(sub.getId(),
                                                finalCategoryId.get()));

                StepVerifier.create(savedfinalCategory).verifyComplete();
                StepVerifier.create(updatedSubcategory)
                                .expectNextMatches(sub -> sub.getName().equals(finalSubcategoryName) &&
                                                sub.getCategory_id() == finalCategoryId.get())
                                .verifyComplete();

        }
}
