
package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        return categoryCrudRepository.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("No categories found")));
    }

    @Override
    public Mono<CategoryEntity> updateCategoryName(Long categoryId, String name) {
        return categoryCrudRepository.findById(categoryId)
                .flatMap(category -> {
                    category.setName(name);
                    return categoryCrudRepository.save(category);
                }).switchIfEmpty(Mono.error(new RuntimeException("Category not found")));
    }

    @Override
    public Mono<Void> deleteCategoryById(Long categoryId) {
        return categoryCrudRepository.deleteById(categoryId).onErrorMap(error -> {
            if (error instanceof DataIntegrityViolationException) {
                return new IllegalStateException(
                        "Failed to delete category with ID: " + categoryId + ". " +
                                "The category is associated with other records and cannot be deleted. "
                                +
                                "Please remove any related registry before attempting to delete this category.",
                        error);
            }
            return new IllegalArgumentException(
                    "Failed to delete category with ID: " + categoryId + ". Unexpected error occurred.");
        });
    }

}
