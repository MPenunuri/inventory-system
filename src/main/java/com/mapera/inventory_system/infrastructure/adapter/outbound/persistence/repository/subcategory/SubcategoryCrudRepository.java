package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import reactor.core.publisher.Flux;

public interface SubcategoryCrudRepository extends ReactiveCrudRepository<SubcategoryEntity, Long> {

    @Query("SELECT * FROM subcategories s WHERE s.user_id = :userId ")
    public Flux<SubcategoryEntity> findAllUserSubcategories(Long userId);

    @Query("SELECT * FROM subcategories s WHERE s.category_id = :categoryId AND s.user_id = :userId ")
    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(Long userId, long categoryId);
}
