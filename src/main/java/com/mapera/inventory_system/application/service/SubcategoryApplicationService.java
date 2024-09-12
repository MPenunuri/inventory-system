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

    public Mono<SubcategoryEntity> registerSubcategory(long categoryId, String name) {
        return subcategoryPersistencePort.registerSubcategory(categoryId, name);
    }

    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(long categoryId) {
        return subcategoryPersistencePort.findSubcategoriesByCategoryId(categoryId);
    }

    public Flux<SubcategoryEntity> getAllSubcategories() {
        return subcategoryPersistencePort.getAllSubcategories();
    }

    public Mono<SubcategoryEntity> changeSubcategoryCategory(Long subcategoryId, Long newCategoryId) {
        return subcategoryPersistencePort.changeSubcategoryCategory(subcategoryId, newCategoryId);
    }

    public Mono<SubcategoryEntity> renameSubcategory(Long subcategoryId, String newName) {
        return subcategoryPersistencePort.renameSubcategory(subcategoryId, newName);
    }

    public Mono<Void> deleteSubcategory(Long subcategoryId) {
        return subcategoryPersistencePort.deleteSubcategory(subcategoryId);
    }
}