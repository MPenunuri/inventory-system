package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubcategoryPersistencePort {

    public Mono<SubcategoryEntity> registerSubcategory(long categoryId, String name);

    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(long categoryId);

    public Flux<SubcategoryEntity> getAllSubcategories();

    public Mono<SubcategoryEntity> changeSubcategoryCategory(Long subcategoryId, Long newCategoryId);

    public Mono<SubcategoryEntity> renameSubcategory(Long subcategoryId, String newName);

    public Mono<Void> deleteSubcategory(Long subcategoryId);
}
