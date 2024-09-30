package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.category.CategoriesAndSubcategoriesDTO;
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

    @Query("SELECT c.id AS category_id, " +
            "c.name AS category_name, " +
            "s.id AS subcategory_id, " +
            "s.name AS subcategory_name, " +
            "COUNT(p.id) AS products " +
            "FROM categories c " +
            "LEFT JOIN subcategories s ON s.category_id = c.id " +
            "LEFT JOIN products p ON p.subcategory_id = s.id " +
            "WHERE c.user_id = :userId " +
            "GROUP BY c.id, c.name, s.id, s.name ")
    public Flux<CategoriesAndSubcategoriesDTO> getCategoriesAndSubcategories(Long userId);
}
