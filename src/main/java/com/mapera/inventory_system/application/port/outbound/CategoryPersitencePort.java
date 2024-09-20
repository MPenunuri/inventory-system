package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryPersitencePort {

    public Mono<CategoryEntity> registerCategory(Long userId, String name);

    public Flux<CategoryEntity> getCategories(Long userId);

    public Mono<CategoryEntity> updateCategoryName(Long userId, Long categoryId, String name);

    public Mono<Void> deleteCategoryById(Long userId, Long categoryId);
}
