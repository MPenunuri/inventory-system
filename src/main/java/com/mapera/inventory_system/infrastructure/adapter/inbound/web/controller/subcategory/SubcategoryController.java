package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.subcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.SubcategoryApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory.PatchSubategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.subcategory.RegisterSubcategoryRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/secure/subcategory")
@Validated
public class SubcategoryController {

    @Autowired
    SubcategoryApplicationService subcategoryApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SubcategoryEntity> registerSubcategory(
            @Valid @RequestBody RegisterSubcategoryRequest request) {
        return subcategoryApplicationService.registerSubcategory(
                request.getCategoryId(), request.getName());
    }

    @GetMapping("/category/{id}")
    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(
            @PathVariable Long id) {
        return subcategoryApplicationService.findSubcategoriesByCategoryId(id);
    }

    @GetMapping
    public Flux<SubcategoryEntity> getAllSubcategories() {
        return subcategoryApplicationService.getAllSubcategories();
    }

    @PatchMapping("/category")
    public Mono<SubcategoryEntity> changeSubcategoryCategory(
            @Valid @RequestBody PatchSubategoryRequest request) {
        return subcategoryApplicationService.changeSubcategoryCategory(
                request.getId(), request.getCategoryId());
    }

    @PatchMapping
    public Mono<SubcategoryEntity> renameSubcategory(
            @Valid @RequestBody PatchSubategoryRequest request) {
        return subcategoryApplicationService.renameSubcategory(
                request.getId(), request.getName());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSubcategory(@PathVariable Long id) {
        return subcategoryApplicationService.deleteSubcategory(id);
    }

}
