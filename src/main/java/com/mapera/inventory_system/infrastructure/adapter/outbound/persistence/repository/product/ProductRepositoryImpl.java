package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.ProductPersistencePort;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.NoSupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.mapper.ProductMapper;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom, ProductPersistencePort {

    @Autowired
    private ProductCrudRepository productCrudRepository;

    @Autowired
    private StockCrudRepository stockCrudRepository;

    @Autowired
    private ProductSupplierCrudRepository productSupplierCrudRepository;

    @Autowired
    private MovementCrudRepository movementCrudRepository;

    private ProductMapper productMapper = new ProductMapper();

    @Override
    public Mono<ProductEntity> registerProduct(Long userId, String name) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setUser_id(userId);
        productEntity.setName(name);
        return productCrudRepository.save(productEntity);
    }

    @Override
    public Flux<StandardProductDTO> findAllProducts(Long userId) {
        return productCrudRepository.findAllProducts(userId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("No products found")));
    }

    @Override
    public Mono<InventoryProduct> findProductById(Long userId, Long productId) {
        return productCrudRepository.findFullProductById(userId, productId)
                .collectList()
                .map(dtoList -> {
                    InventoryProduct product = productMapper.toDomain(dtoList);
                    return product;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")));
    }

    @Override
    public Flux<StandardProductDTO> findProductsByCategoryId(Long userId, long categoryId) {
        return productCrudRepository.findProductsByCategoryId(userId, categoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySubcategoryId(Long userId, Long subcategoryId) {
        return productCrudRepository.findProductsBySubcategoryId(userId, subcategoryId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<SupplierProductDTO> findProductsBySupplierId(Long userId, Long supplierId) {
        return productCrudRepository.findProductsBySupplierId(userId, supplierId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<NoSupplierProductDTO> findProductsWithNoSupplierRelation(Long userId, Long supplierId) {
        return productCrudRepository.findProductsWithNoSupplierRelation(userId, supplierId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<LocationProductDTO> findProductsByLocationid(Long userId, Long locationId) {
        return productCrudRepository.findProductsByLocationid(userId, locationId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<StockProductDTO> findProductsWithMinimumStock(Long userId) {
        return productCrudRepository.findProductsWithMinimumStock(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Mono<StockProductDTO> getProductStockById(Long userId, Long productId) {
        return productCrudRepository.getProductStockById(userId, productId)
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySellingRetailPrice(
            Long userId, Long currencyId, Double min, Double max) {
        return productCrudRepository.findProductsBySellingRetailPrice(userId, currencyId, min, max)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Flux<StandardProductDTO> findProductsBySellingWholesalePrice(
            Long userId, Long currencyId, Double min, Double max) {
        return productCrudRepository.findProductsBySellingWholesalePrice(userId, currencyId, min, max)
                .switchIfEmpty(Mono.error(new RuntimeException("No products found")));
    }

    @Override
    public Mono<ProductEntity> updateProductName(
            Long userId, Long productId, String name) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    if (!product.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    product.setName(name);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateSubcategory(
            Long userId, Long productId, Long subcategoryId) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    if (!product.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    if (subcategoryId == null) {
                        throw new IllegalArgumentException("Subcategory ID cannot be null");
                    }
                    product.setSubcategory_id(subcategoryId);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateProductPresentation(
            Long userId, Long productId, String productPresentation) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    if (!product.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    product.setProduct_presentation(productPresentation);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateMinimumStock(
            Long userId, Long productId, int minimumStock) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    if (!product.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    product.setMinimum_stock(minimumStock);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateRetailPrice(
            Long userId, Long productId, Double price) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    if (!product.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    product.setRetail_price(price);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updateWholesalePrice(
            Long userId, Long productId, Double price) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    if (!product.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    product.setWholesale_price(price);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<ProductEntity> updatePriceCurrency(
            Long userId, Long productId, Long priceCurrencyId) {
        return productCrudRepository.findById(productId)
                .flatMap(product -> {
                    if (!product.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    if (priceCurrencyId == null) {
                        throw new IllegalArgumentException("Currency ID cannot be null");
                    }
                    product.setPrice_currency_id(priceCurrencyId);
                    return productCrudRepository.save(product);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")));
    }

    @Override
    public Mono<Void> deleteProductById(Long userId, Long productId) {
        return productCrudRepository.findById(productId)
                .switchIfEmpty(Mono.error(new RuntimeException("No product found")))
                .flatMap(p -> {
                    if (!p.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    return productSupplierCrudRepository.findProductRelations(productId, userId)
                            .flatMap(productSupplierRelation -> productSupplierCrudRepository
                                    .delete(productSupplierRelation))
                            .thenMany(stockCrudRepository.findProductStock(productId, userId)
                                    .flatMap(stock -> stockCrudRepository.delete(stock)))
                            .thenMany(movementCrudRepository.getMovementsByProductId(userId, productId)
                                    .flatMap(movement -> movementCrudRepository.deleteById(
                                            movement.getMovementId())))
                            .then(productCrudRepository.delete(p))
                            .onErrorMap(error -> {
                                if (error instanceof DataIntegrityViolationException) {
                                    return new IllegalStateException(
                                            "Failed to delete product with ID: " + productId + ". " +
                                                    "The product is associated with other records and cannot be deleted. "
                                                    +
                                                    "Please remove any related registry before attempting to delete this product.");
                                }
                                return new IllegalArgumentException(
                                        "Failed to delete product with ID: " + productId
                                                + ". Unexpected error occurred.");
                            });
                });
    }

}
