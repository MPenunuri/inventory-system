
package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

import reactor.core.publisher.Mono;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    @Autowired
    CategoryCrudRepository categoryCrudRepository;

    @Override
    public Mono<CategoryEntity> updateCategoryName(Long categoryId, String name) {
        return categoryCrudRepository.findById(categoryId)
                .flatMap(category -> {
                    category.setName(name);
                    return categoryCrudRepository.save(category);
                });
    }

}
