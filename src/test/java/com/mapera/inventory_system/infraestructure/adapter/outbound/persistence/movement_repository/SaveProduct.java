package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.movement_repository;

import java.util.concurrent.atomic.AtomicReference;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SubcategoryEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryRepository;

import reactor.core.publisher.Mono;

public class SaveProduct {
    public static Mono<Void> execute(
            CategoryRepository categoryRepository,
            SubcategoryRepository subcategoryRepository,
            ProductRepository productRepository,
            CurrencyRepository currencyRepository,
            CategoryEntity categoryEntity,
            SubcategoryEntity subcategoryEntity,
            ProductEntity productEntity,
            CurrencyEntity currencyEntity,
            AtomicReference<Long> currencyId,
            AtomicReference<Long> productId) {
        return categoryRepository.save(categoryEntity)
                .flatMap(savedCategory -> {
                    subcategoryEntity.setCategory_id(savedCategory.getId());
                    return subcategoryRepository.save(subcategoryEntity)
                            .flatMap(savedSubcategory -> {
                                productEntity.setSubcategory_id(
                                        savedSubcategory.getId());
                                return currencyRepository.save(currencyEntity)
                                        .flatMap(savedCurrency -> {
                                            currencyId.set(savedCurrency
                                                    .getId());
                                            productEntity.setPrice_currency_id(
                                                    savedCurrency.getId());
                                            return productRepository.save(
                                                    productEntity)
                                                    .doOnNext(savedProd -> productId
                                                            .set(savedProd.getId()));
                                        }).then();
                            }).then();
                }).then();

    }
}
