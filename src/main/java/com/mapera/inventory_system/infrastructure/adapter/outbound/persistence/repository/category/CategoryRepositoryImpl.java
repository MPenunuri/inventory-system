
package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.CategoryPersitencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom, CategoryPersitencePort {

    @Autowired
    CategoryCrudRepository categoryCrudRepository;

    @Override
    public Mono<CategoryEntity> registerCategory(String name) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(name);
        return categoryCrudRepository.save(categoryEntity);
    }

    @Override
    public Flux<CategoryEntity> getCategories() {
        return categoryCrudRepository.findAll();
    }

    @Override
    public Mono<CategoryEntity> updateCategoryName(Long categoryId, String name) {
        return categoryCrudRepository.findById(categoryId)
                .flatMap(category -> {
                    category.setName(name);
                    return categoryCrudRepository.save(category);
                });
    }

    @Override
    public Mono<Void> deleteCategoryById(Long categoryId) {
        return categoryCrudRepository.deleteById(categoryId);
    }

}
