
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
    public Mono<CategoryEntity> registerCategory(Long userId, String name) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUser_id(userId);
        categoryEntity.setName(name);
        return categoryCrudRepository.save(categoryEntity);
    }

    @Override
    public Flux<CategoryEntity> getCategories(Long userId) {
        return categoryCrudRepository.findAllUserCategories(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("No categories found")));
    }

    @Override
    public Mono<CategoryEntity> updateCategoryName(
            Long userId, Long categoryId, String name) {
        return categoryCrudRepository.findById(categoryId)
                .flatMap(category -> {
                    if (!category.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    category.setName(name);
                    return categoryCrudRepository.save(category);
                }).switchIfEmpty(Mono.error(new RuntimeException("Category not found")));
    }

    @Override
    public Mono<Void> deleteCategoryById(Long userId, Long categoryId) {
        return categoryCrudRepository.findById(categoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("Category not found")))
                .flatMap(c -> {
                    if (!c.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    return categoryCrudRepository.delete(c);
                }).onErrorMap(error -> {
                    if (error instanceof DataIntegrityViolationException) {
                        return new IllegalStateException(
                                "Failed to delete category with ID: " + categoryId + ". " +
                                        "The category is associated with other records and cannot be deleted. "
                                        +
                                        "Please remove any related registry before attempting to delete this category.");
                    }
                    return new IllegalArgumentException(
                            "Failed to delete category with ID: " + categoryId + ". Unexpected error occurred.");
                });
    }

}
