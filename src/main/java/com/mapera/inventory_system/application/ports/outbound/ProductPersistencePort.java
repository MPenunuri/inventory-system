package com.mapera.inventory_system.application.ports.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.FullProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {

    Flux<StandardProductDTO> findAllProducts();

    Flux<FullProductDTO> findProductById(Long productId);

    Flux<StandardProductDTO> findProductsByCategoryId(long categoryId);

    Flux<StandardProductDTO> findProductsBySubcategoryId(Long subcategoryId);

    Flux<SupplierProductDTO> findProductsBySupplierId(Long supplierId);

    Flux<LocationProductDTO> findProductsByLocationid(Long locationId);

    Flux<StockProductDTO> findProductsWithMinimumStock();

    Mono<StockProductDTO> getProductStockById(Long productId);

    Flux<StandardProductDTO> findProductsBySellingRetailPrice(Long currencyId, Double min, Double max);

    Flux<StandardProductDTO> findProductsBySellingWholesalePrice(Long currencyId, Double min, Double max);

    Mono<ProductEntity> updateProductName(Long productId, String name);

    Mono<ProductEntity> updateSubcategory(Long productId, Long subcategoryId);

    Mono<ProductEntity> updateProductPresentation(Long productId, String productPresentation);

    Mono<ProductEntity> updateMinimumStock(Long productId, int minimumStock);

    Mono<ProductEntity> updateRetailPrice(Long productId, Double price);

    Mono<ProductEntity> updateWholesalePrice(Long productId, Double price);

    Mono<ProductEntity> updatePriceCurrency(Long productId, Long priceCurrencyId);

    Mono<Void> deleteProductById(Long productId);
}
