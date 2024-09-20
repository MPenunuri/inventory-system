package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {

    Mono<ProductEntity> registerProduct(Long userId, String name);

    Flux<StandardProductDTO> findAllProducts(Long userId);

    Mono<InventoryProduct> findProductById(Long userId, Long productId);

    Flux<StandardProductDTO> findProductsByCategoryId(Long userId, long categoryId);

    Flux<StandardProductDTO> findProductsBySubcategoryId(Long userId, Long subcategoryId);

    Flux<SupplierProductDTO> findProductsBySupplierId(Long userId, Long supplierId);

    Flux<LocationProductDTO> findProductsByLocationid(Long userId, Long locationId);

    Flux<StockProductDTO> findProductsWithMinimumStock(Long userId);

    Mono<StockProductDTO> getProductStockById(Long userId, Long productId);

    Flux<StandardProductDTO> findProductsBySellingRetailPrice(Long userId, Long currencyId, Double min, Double max);

    Flux<StandardProductDTO> findProductsBySellingWholesalePrice(Long userId, Long currencyId, Double min, Double max);

    Mono<ProductEntity> updateProductName(Long userId, Long productId, String name);

    Mono<ProductEntity> updateSubcategory(Long userId, Long productId, Long subcategoryId);

    Mono<ProductEntity> updateProductPresentation(Long userId, Long productId, String productPresentation);

    Mono<ProductEntity> updateMinimumStock(Long userId, Long productId, int minimumStock);

    Mono<ProductEntity> updateRetailPrice(Long userId, Long productId, Double price);

    Mono<ProductEntity> updateWholesalePrice(Long userId, Long productId, Double price);

    Mono<ProductEntity> updatePriceCurrency(Long userId, Long productId, Long priceCurrencyId);

    Mono<Void> deleteProductById(Long userId, Long productId);
}
