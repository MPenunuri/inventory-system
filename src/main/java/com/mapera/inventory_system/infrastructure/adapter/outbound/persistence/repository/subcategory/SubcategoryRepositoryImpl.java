package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;

import reactor.core.publisher.Mono;

@Repository
public class SubcategoryRepositoryImpl implements SubcategoryRepositoryCustom {

    @Autowired
    SubcategoryCrudRepository subcategoryCrudRepository;

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

}
