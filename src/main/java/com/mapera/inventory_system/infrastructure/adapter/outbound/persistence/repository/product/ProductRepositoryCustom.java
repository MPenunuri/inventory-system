package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Mono;

public interface ProductRepositoryCustom {
    Mono<ProductEntity> updateProductName(Long userId, Long productId, String name);

    Mono<ProductEntity> updateSubcategory(Long userId, Long productId, Long subcategoryId);

    Mono<ProductEntity> updateProductPresentation(Long userId, Long productId, String productPresentation);

    Mono<ProductEntity> updateMinimumStock(Long userId, Long productId, int minimumStock);

    Mono<ProductEntity> updateRetailPrice(Long userId, Long productId, Double price);

    Mono<ProductEntity> updateWholesalePrice(Long userId, Long productId, Double price);

    Mono<ProductEntity> updatePriceCurrency(Long userId, Long productId, Long priceCurrencyId);

}
