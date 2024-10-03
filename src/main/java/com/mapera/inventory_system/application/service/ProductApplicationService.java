
package com.mapera.inventory_system.application.service;

import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.ProductPersistencePort;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.NoSupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductApplicationService {

    private final ProductPersistencePort productPersistencePort;

    public ProductApplicationService(ProductPersistencePort productPersistencePort) {
        this.productPersistencePort = productPersistencePort;
    }

    public Mono<ProductEntity> registerProduct(Long userId, String name) {
        return productPersistencePort.registerProduct(userId, name);
    }

    public Flux<StandardProductDTO> findAllProducts(Long userId) {
        return productPersistencePort.findAllProducts(userId);
    };

    public Mono<InventoryProduct> findProductById(Long userId, Long productId) {
        return productPersistencePort.findProductById(userId, productId);
    };

    public Flux<StandardProductDTO> findProductsByCategoryId(Long userId, long categoryId) {
        return productPersistencePort.findProductsByCategoryId(userId, categoryId);
    };

    public Flux<StandardProductDTO> findProductsBySubcategoryId(Long userId, Long subcategoryId) {
        return productPersistencePort.findProductsBySubcategoryId(userId, subcategoryId);
    };

    public Flux<SupplierProductDTO> findProductsBySupplierId(Long userId, Long supplierId) {
        return productPersistencePort.findProductsBySupplierId(userId, supplierId);
    };

    public Flux<NoSupplierProductDTO> findProductsWithNoSupplierRelation(Long userId, Long supplierId) {
        return productPersistencePort.findProductsWithNoSupplierRelation(userId, supplierId);
    };

    public Flux<LocationProductDTO> findProductsByLocationid(Long userId, Long locationId) {
        return productPersistencePort.findProductsByLocationid(userId, locationId);
    };

    public Flux<StockProductDTO> findProductsWithMinimumStock(Long userId) {
        return productPersistencePort.findProductsWithMinimumStock(userId);
    };

    public Mono<StockProductDTO> getProductStockById(Long userId, Long productId) {
        return productPersistencePort.getProductStockById(userId, productId);
    };

    public Flux<StandardProductDTO> findProductsBySellingRetailPrice(
            Long userId, Long currencyId, Double min, Double max) {
        return productPersistencePort.findProductsBySellingRetailPrice(userId, currencyId, min, max);
    };

    public Flux<StandardProductDTO> findProductsBySellingWholesalePrice(
            Long userId, Long currencyId, Double min, Double max) {
        return productPersistencePort.findProductsBySellingWholesalePrice(userId, currencyId, min, max);
    };

    public Mono<ProductEntity> updateProductName(Long userId, Long productId, String name) {
        return productPersistencePort.updateProductName(userId, productId, name);
    };

    public Mono<ProductEntity> updateSubcategory(Long userId, Long productId, Long subcategoryId) {
        return productPersistencePort.updateSubcategory(userId, productId, subcategoryId);
    };

    public Mono<ProductEntity> updateProductPresentation(
            Long userId, Long productId, String productPresentation) {
        return productPersistencePort.updateProductPresentation(userId, productId, productPresentation);
    };

    public Mono<ProductEntity> updateMinimumStock(
            Long userId, Long productId, int minimumStock) {
        return productPersistencePort.updateMinimumStock(userId, productId, minimumStock);
    };

    public Mono<ProductEntity> updateRetailPrice(Long userId, Long productId, Double price) {
        return productPersistencePort.updateRetailPrice(userId, productId, price);
    };

    public Mono<ProductEntity> updateWholesalePrice(Long userId, Long productId, Double price) {
        return productPersistencePort.updateWholesalePrice(userId, productId, price);
    };

    public Mono<ProductEntity> updatePriceCurrency(Long userId, Long productId, Long priceCurrencyId) {
        return productPersistencePort.updatePriceCurrency(userId, productId, priceCurrencyId);
    };

    public Mono<Void> deleteProductById(Long userId, Long productId) {
        return productPersistencePort.deleteProductById(userId, productId);
    };

}
