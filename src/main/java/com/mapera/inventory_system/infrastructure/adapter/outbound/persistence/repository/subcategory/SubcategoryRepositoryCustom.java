package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import reactor.core.publisher.Mono;

public interface SubcategoryRepositoryCustom {
    public Mono<SubcategoryEntity> changeSubcategoryCategory(Long userId, Long subcategoryId, Long newCategoryId);

    public Mono<SubcategoryEntity> renameSubcategory(Long userId, Long subcategoryId, String newName);
}
