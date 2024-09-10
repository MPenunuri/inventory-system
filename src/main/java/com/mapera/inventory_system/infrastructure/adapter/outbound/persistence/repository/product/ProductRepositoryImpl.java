package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.ports.outbound.ProductPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.FullProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom, ProductPersistencePort {

    @Autowired
    private ProductCrudRepository productCrudRepository;

    @Override
    public Flux<StandardProductDTO> findAllProducts() {
        return productCrudRepository.findAllProducts();
    }

    @Override
    public Flux<FullProductDTO> findProductById(Long productId) {
        return productCrudRepository.findProductById(productId);
    }

    @Override
    public Flux<StandardProductDTO> findProductsByCategoryId(long categoryId) {
        return productCrudRepository.findProductsByCategoryId(categoryId);
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySubcategoryId(Long subcategoryId) {
        return productCrudRepository.findProductsBySubcategoryId(subcategoryId);
    }

    @Override
    public Flux<SupplierProductDTO> findProductsBySupplierId(Long supplierId) {
        return productCrudRepository.findProductsBySupplierId(supplierId);
    }

    @Override
    public Flux<LocationProductDTO> findProductsByLocationid(Long locationId) {
        return productCrudRepository.findProductsByLocationid(locationId);
    }

    @Override
    public Flux<StockProductDTO> findProductsWithMinimumStock() {
        return productCrudRepository.findProductsWithMinimumStock();
    }

    @Override
    public Mono<StockProductDTO> getProductStockById(Long productId) {
        return productCrudRepository.getProductStockById(productId);
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySellingRetailPrice(Long currencyId, Double min, Double max) {
        return productCrudRepository.findProductsBySellingRetailPrice(currencyId, min, max);
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySellingWholesalePrice(Long currencyId, Double min, Double max) {
        return productCrudRepository.findProductsBySellingWholesalePrice(currencyId, min, max);
    }

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
    public Mono<ProductEntity> updatePriceCurrency(Long productId, Long priceCurrencyId) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setPrice_currency_id(priceCurrencyId);
                    return productCrudRepository.save(product);
                });
    }

    @Override
    public Mono<Void> deleteProductById(Long productId) {
        return productCrudRepository.deleteById(productId);
    }

}
