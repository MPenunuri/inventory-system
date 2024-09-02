package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Mono;

public interface ProductRepositoryCustom {
    Mono<ProductEntity> updateProductName(Long productId, String name);

    Mono<ProductEntity> updateSubcategory(Long productId, Long subcategoryId);

    Mono<ProductEntity> updateProductPresentation(Long productId, String productPresentation);

    Mono<ProductEntity> updateMinimumStock(Long productId, int minimumStock);

    Mono<ProductEntity> updateRetailPrice(Long productId, Double price);

    Mono<ProductEntity> updateWholesalePrice(Long productId, Double price);

    Mono<ProductEntity> updatePriceCurrency(Long productId, String priceCurrency);

}
