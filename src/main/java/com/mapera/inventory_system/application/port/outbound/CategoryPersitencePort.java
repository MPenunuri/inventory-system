package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryPersitencePort {

    public Mono<CategoryEntity> registerCategory(String name);

    public Flux<CategoryEntity> getCategories();

    public Mono<CategoryEntity> updateCategoryName(Long categoryId, String name);

    public Mono<Void> deleteCategoryById(Long categoryId);
}
