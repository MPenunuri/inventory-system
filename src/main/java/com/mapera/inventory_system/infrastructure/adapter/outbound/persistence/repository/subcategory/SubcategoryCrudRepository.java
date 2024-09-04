package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import reactor.core.publisher.Flux;

public interface SubcategoryCrudRepository extends ReactiveCrudRepository<SubcategoryEntity, Long> {

    @Query("SELECT * FROM subcategories WHERE category_id = :categoryId")
    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(long categoryId);
}
