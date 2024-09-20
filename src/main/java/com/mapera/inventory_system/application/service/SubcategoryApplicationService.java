package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.SubcategoryPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SubcategoryApplicationService {

    @Autowired
    private SubcategoryPersistencePort subcategoryPersistencePort;

    public Mono<SubcategoryEntity> registerSubcategory(Long userId, long categoryId, String name) {
        return subcategoryPersistencePort.registerSubcategory(userId, categoryId, name);
    }

    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(Long userId, long categoryId) {
        return subcategoryPersistencePort.findSubcategoriesByCategoryId(userId, categoryId);
    }

    public Flux<SubcategoryEntity> getAllSubcategories(Long userId) {
        return subcategoryPersistencePort.getAllSubcategories(userId);
    }

    public Mono<SubcategoryEntity> changeSubcategoryCategory(Long userId, Long subcategoryId, Long newCategoryId) {
        return subcategoryPersistencePort.changeSubcategoryCategory(userId, subcategoryId, newCategoryId);
    }

    public Mono<SubcategoryEntity> renameSubcategory(Long userId, Long subcategoryId, String newName) {
        return subcategoryPersistencePort.renameSubcategory(userId, subcategoryId, newName);
    }

    public Mono<Void> deleteSubcategory(Long userId, Long subcategoryId) {
        return subcategoryPersistencePort.deleteSubcategory(userId, subcategoryId);
    }
}