package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubcategoryPersistencePort {

    public Mono<SubcategoryEntity> registerSubcategory(Long userId, long categoryId, String name);

    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(Long userId, long categoryId);

    public Flux<SubcategoryEntity> getAllSubcategories(Long userId);

    public Mono<SubcategoryEntity> changeSubcategoryCategory(Long userId, Long subcategoryId, Long newCategoryId);

    public Mono<SubcategoryEntity> renameSubcategory(Long userId, Long subcategoryId, String newName);

    public Mono<Void> deleteSubcategory(Long userId, Long subcategoryId);
}
