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

    public Mono<CategoryEntity> registerCategory(Long userId, String name) {
        return categoryPersitencePort.registerCategory(userId, name);
    }

    public Flux<CategoryEntity> getCategories(Long userId) {
        return categoryPersitencePort.getCategories(userId);
    }

    public Mono<CategoryEntity> updateCategoryName(
            Long userId, Long categoryId, String name) {
        return categoryPersitencePort.updateCategoryName(userId, categoryId, name);
    }

    public Mono<Void> deleteCategoryById(Long userId, Long categoryId) {
        return categoryPersitencePort.deleteCategoryById(userId, categoryId);
    }
}
