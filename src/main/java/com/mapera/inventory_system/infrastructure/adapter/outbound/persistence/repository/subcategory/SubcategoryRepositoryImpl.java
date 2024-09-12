package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<SubcategoryEntity> registerSubcategory(long categoryId, String name) {
        SubcategoryEntity subcategoryEntity = new SubcategoryEntity();
        subcategoryEntity.setCategory_id(categoryId);
        subcategoryEntity.setName(name);
        return subcategoryCrudRepository.save(subcategoryEntity);
    }

    @Override
    public Flux<SubcategoryEntity> findSubcategoriesByCategoryId(long categoryId) {
        return subcategoryCrudRepository.findSubcategoriesByCategoryId(categoryId);
    }

    @Override
    public Flux<SubcategoryEntity> getAllSubcategories() {
        return subcategoryCrudRepository.findAll();
    }

    @Override
    public Mono<SubcategoryEntity> changeSubcategoryCategory(Long subcategoryId, Long newCategoryId) {
        return subcategoryCrudRepository.findById(subcategoryId).flatMap(subcategory -> {
            subcategory.setCategory_id(newCategoryId);
            return subcategoryCrudRepository.save(subcategory);
        });
    }

    @Override
    public Mono<SubcategoryEntity> renameSubcategory(Long subcategoryId, String newName) {
        return subcategoryCrudRepository.findById(subcategoryId).flatMap(subcategory -> {
            subcategory.setName(newName);
            return subcategoryCrudRepository.save(subcategory);
        });
    }

    @Override
    public Mono<Void> deleteSubcategory(Long subcategoryId) {
        return subcategoryCrudRepository.deleteById(subcategoryId);
    }

}
