package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

import reactor.core.publisher.Mono;

public interface CategoryRepositoryCustom {
    public Mono<CategoryEntity> updateCategoryName(Long userId, Long categoryId, String name);
}
