package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import org.springframework.beans.factory.annotation.Autowired;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Mono;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @Autowired
    private ProductCrudRepository productCrudRepository;

    @Override
    public Mono<ProductEntity> updateProductName(Long productId, String name) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setName(name);
                    return productCrudRepository.save(product);
                });
    }

    @Override
    public Mono<ProductEntity> updateSubcategory(Long productId, Long subcategoryId) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setSubcategory_id(subcategoryId);
                    return productCrudRepository.save(product);
                });
    }

    @Override
    public Mono<ProductEntity> updateProductPresentation(Long productId, String productPresentation) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setProductPresentation(productPresentation);
                    return productCrudRepository.save(product);
                });
    }

    @Override
    public Mono<ProductEntity> updateMinimumStock(Long productId, int minimumStock) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setMinimumStock(minimumStock);
                    return productCrudRepository.save(product);
                });
    }

    @Override
    public Mono<ProductEntity> updateRetailPrice(Long productId, Double price) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setRetail_price(price);
                    return productCrudRepository.save(product);
                });
    }

    @Override
    public Mono<ProductEntity> updateWholesalePrice(Long productId, Double price) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setWholesale_price(price);
                    return productCrudRepository.save(product);
                });
    }

    @Override
    public Mono<ProductEntity> updatePriceCurrency(Long productId, String priceCurrency) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setPrice_currency(priceCurrency);
                    return productCrudRepository.save(product);
                });
    }

}
