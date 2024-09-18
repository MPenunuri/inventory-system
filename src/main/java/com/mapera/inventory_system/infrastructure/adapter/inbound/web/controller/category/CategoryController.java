package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.CategoryApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.category.PatchCategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.category.RegisterCategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/secure/category")
@Validated
public class CategoryController {

    @Autowired
    CategoryApplicationService categoryApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CategoryEntity> registerCategory(
            @Valid @RequestBody RegisterCategoryRequest request) {
        return categoryApplicationService.registerCategory(request.getName());
    }

    @GetMapping
    public Flux<CategoryEntity> getCategories() {
        return categoryApplicationService.getCategories();
    }

    @PatchMapping
    public Mono<CategoryEntity> updateCategoryName(
            @Valid @RequestBody PatchCategoryRequest request) {
        return categoryApplicationService.updateCategoryName(
                request.getId(), request.getName());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCategory(@Valid @PathVariable Long id) {
        return categoryApplicationService.deleteCategoryById(id);
    }
}
