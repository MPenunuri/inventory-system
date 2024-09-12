package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.CategoryPersitencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryApplicationService {

    @Autowired
    CategoryPersitencePort categoryPersitencePort;

    public Mono<CategoryEntity> registerCategory(String name) {
        return categoryPersitencePort.registerCategory(name);
    }

    public Flux<CategoryEntity> getCategories() {
        return categoryPersitencePort.getCategories();
    }

    public Mono<CategoryEntity> updateCategoryName(Long categoryId, String name) {
        return categoryPersitencePort.updateCategoryName(categoryId, name);
    }

    public Mono<Void> deleteCategoryById(Long categoryId) {
        return categoryPersitencePort.deleteCategoryById(categoryId);
    }
}
