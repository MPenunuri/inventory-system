
package com.mapera.inventory_system.application.service;

import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.ProductPersistencePort;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
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

    public Mono<ProductEntity> registerProduct(String name) {
        return productPersistencePort.registerProduct(name);
    }

    public Flux<StandardProductDTO> findAllProducts() {
        return productPersistencePort.findAllProducts();
    };

    public Mono<InventoryProduct> findProductById(Long productId) {
        return productPersistencePort.findProductById(productId);
    };

    public Flux<StandardProductDTO> findProductsByCategoryId(long categoryId) {
        return productPersistencePort.findProductsByCategoryId(categoryId);
    };

    public Flux<StandardProductDTO> findProductsBySubcategoryId(Long subcategoryId) {
        return productPersistencePort.findProductsBySubcategoryId(subcategoryId);
    };

    public Flux<SupplierProductDTO> findProductsBySupplierId(Long supplierId) {
        return productPersistencePort.findProductsBySupplierId(supplierId);
    };

    public Flux<LocationProductDTO> findProductsByLocationid(Long locationId) {
        return productPersistencePort.findProductsByLocationid(locationId);
    };

    public Flux<StockProductDTO> findProductsWithMinimumStock() {
        return productPersistencePort.findProductsWithMinimumStock();
    };

    public Mono<StockProductDTO> getProductStockById(Long productId) {
        return productPersistencePort.getProductStockById(productId);
    };

    public Flux<StandardProductDTO> findProductsBySellingRetailPrice(Long currencyId, Double min, Double max) {
        return productPersistencePort.findProductsBySellingRetailPrice(currencyId, min, max);
    };

    public Flux<StandardProductDTO> findProductsBySellingWholesalePrice(Long currencyId, Double min, Double max) {
        return productPersistencePort.findProductsBySellingWholesalePrice(currencyId, min, max);
    };

    public Mono<ProductEntity> updateProductName(Long productId, String name) {
        return productPersistencePort.updateProductName(productId, name);
    };

    public Mono<ProductEntity> updateSubcategory(Long productId, Long subcategoryId) {
        return productPersistencePort.updateSubcategory(productId, subcategoryId);
    };

    public Mono<ProductEntity> updateProductPresentation(Long productId, String productPresentation) {
        return productPersistencePort.updateProductPresentation(productId, productPresentation);
    };

    public Mono<ProductEntity> updateMinimumStock(Long productId, int minimumStock) {
        return productPersistencePort.updateMinimumStock(productId, minimumStock);
    };

    public Mono<ProductEntity> updateRetailPrice(Long productId, Double price) {
        return productPersistencePort.updateRetailPrice(productId, price);
    };

    public Mono<ProductEntity> updateWholesalePrice(Long productId, Double price) {
        return productPersistencePort.updateWholesalePrice(productId, price);
    };

    public Mono<ProductEntity> updatePriceCurrency(Long productId, Long priceCurrencyId) {
        return productPersistencePort.updatePriceCurrency(productId, priceCurrencyId);
    };

    public Mono<Void> deleteProductById(Long productId) {
        return productPersistencePort.deleteProductById(productId);
    };

}
