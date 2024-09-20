package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryCrudRepository extends ReactiveCrudRepository<CategoryEntity, Long> {

    @Query("SELECT COUNT(*) FROM categories c WHERE c.user_id = :userId")
    Mono<Integer> countByUserId(Long userId);

    @Query("DELETE FROM categories c WHERE c.user_id = :userId")
    Mono<Void> deleteByUserId(Long userId);

    @Query("SELECT * FROM categories c WHERE c.user_id = :userId ")
    public Flux<CategoryEntity> findAllUserCategories(Long userId);
}
