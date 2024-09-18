package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.ProductPersistencePort;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.mapper.ProductMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom, ProductPersistencePort {

    @Autowired
    private ProductCrudRepository productCrudRepository;

    private ProductMapper productMapper = new ProductMapper();

    @Override
    public Mono<ProductEntity> registerProduct(String name) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(name);
        return productCrudRepository.save(productEntity);
    }

    @Override
    public Flux<StandardProductDTO> findAllProducts() {
        return productCrudRepository.findAllProducts()
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Mono<InventoryProduct> findProductById(Long productId) {
        return productCrudRepository.findFullProductById(productId)
                .collectList()
                .map(dtoList -> {
                    InventoryProduct product = productMapper.toDomain(dtoList);
                    return product;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")));
    }

    @Override
    public Flux<StandardProductDTO> findProductsByCategoryId(long categoryId) {
        return productCrudRepository.findProductsByCategoryId(categoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySubcategoryId(Long subcategoryId) {
        return productCrudRepository.findProductsBySubcategoryId(subcategoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<SupplierProductDTO> findProductsBySupplierId(Long supplierId) {
        return productCrudRepository.findProductsBySupplierId(supplierId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<LocationProductDTO> findProductsByLocationid(Long locationId) {
        return productCrudRepository.findProductsByLocationid(locationId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<StockProductDTO> findProductsWithMinimumStock() {
        return productCrudRepository.findProductsWithMinimumStock()
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Mono<StockProductDTO> getProductStockById(Long productId) {
        return productCrudRepository.getProductStockById(productId)
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySellingRetailPrice(Long currencyId, Double min, Double max) {
        return productCrudRepository.findProductsBySellingRetailPrice(currencyId, min, max)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySellingWholesalePrice(Long currencyId, Double min, Double max) {
        return productCrudRepository.findProductsBySellingWholesalePrice(currencyId, min, max)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Mono<ProductEntity> updateProductName(Long productId, String name) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setName(name);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateSubcategory(Long productId, Long subcategoryId) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setSubcategory_id(subcategoryId);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateProductPresentation(Long productId, String productPresentation) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setProductPresentation(productPresentation);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateMinimumStock(Long productId, int minimumStock) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setMinimumStock(minimumStock);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateRetailPrice(Long productId, Double price) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setRetail_price(price);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateWholesalePrice(Long productId, Double price) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setWholesale_price(price);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updatePriceCurrency(Long productId, Long priceCurrencyId) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    product.setPrice_currency_id(priceCurrencyId);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<Void> deleteProductById(Long productId) {
        return productCrudRepository.deleteById(productId)
                .onErrorMap(error -> {
                    if (error instanceof DataIntegrityViolationException) {
                        return new IllegalStateException(
                                "Failed to delete product with ID: " + productId + ". " +
                                        "The product is associated with other records and cannot be deleted. "
                                        +
                                        "Please remove any related registry before attempting to delete this product.",
                                error);
                    }
                    return new IllegalArgumentException(
                            "Failed to delete product with ID: " + productId + ". Unexpected error occurred.");
                });
    }

}
