package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.SubcategoryPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SubcategoryRepositoryImpl
        implements SubcategoryRepositoryCustom, SubcategoryPersistencePort {

    @Autowired
    SubcategoryCrudRepository subcategoryCrudRepository;

    @Override
    public Mono<SubcategoryEntity> registerSubcategory(Long userId, long categoryId, String name) {
        SubcategoryEntity subcategoryEntity = new SubcategoryEntity();
        subcategoryEntity.setUser_id(userId);
        subcategoryEntity.setCategory_id(categoryId);
        subcategoryEntity.setName(name);
        return subcategoryCrudRepository.save(subcategoryEntity);
    }

    @Override
    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(Long userId, long categoryId) {
        return subcategoryCrudRepository.findSubcategoriesByCategoryId(userId, categoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("No subcategories found")));
    }

    @Override
    public Flux<SubcategoryEntity> getAllSubcategories(Long userId) {
        return subcategoryCrudRepository.findAllUserSubcategories(userId);
    }

    @Override
    public Mono<SubcategoryEntity> changeSubcategoryCategory(Long userId, Long subcategoryId, Long newCategoryId) {
        return subcategoryCrudRepository.findById(subcategoryId).flatMap(subcategory -> {
            if (!subcategory.getUser_id().equals(userId)) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            subcategory.setCategory_id(newCategoryId);
            return subcategoryCrudRepository.save(subcategory)
                    .onErrorMap(error -> {
                        return new IllegalArgumentException(
                                "Failed change Subcategory Category: " + subcategoryId);
                    });
        }).switchIfEmpty(Mono.error(new RuntimeException("Subcategory not found")));
    }

    @Override
    public Mono<SubcategoryEntity> renameSubcategory(Long userId, Long subcategoryId, String newName) {
        return subcategoryCrudRepository.findById(subcategoryId).flatMap(subcategory -> {
            if (!subcategory.getUser_id().equals(userId)) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            subcategory.setName(newName);
            return subcategoryCrudRepository.save(subcategory)
                    .onErrorMap(error -> {
                        return new IllegalArgumentException(
                                "Failed rename subcategory : " + subcategoryId);
                    });
        }).switchIfEmpty(Mono.error(new RuntimeException("Subcategory not found")));
    }

    @Override
    public Mono<Void> deleteSubcategory(Long userId, Long subcategoryId) {
        return subcategoryCrudRepository.findById(subcategoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("Subcategory not found"))).flatMap(s -> {
                    System.out.println("Subcategory found");
                    if (!s.getUser_id().equals(userId)) {
                        System.out.println("No invalid credentials");
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    return subcategoryCrudRepository.delete(s)
                            .doOnSuccess(unused -> System.out.println("Subcategory deleted"))
                            .then();
                })
                .doOnError(error -> {
                    if (error instanceof DataIntegrityViolationException) {
                        Mono.error(new IllegalStateException(
                                "Failed to delete subcategory with ID: " + subcategoryId + ". " +
                                        "The subcategory is associated with other records and cannot be deleted. "
                                        +
                                        "Please remove any related registry before attempting to delete this subcategory."));
                    }
                    Mono.error(new IllegalArgumentException(
                            "Failed to delete subcategory with ID: " + subcategoryId + ". Unexpected error occurred."));
                });
    }

}
