package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.FullProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductCrudRepository extends ReactiveCrudRepository<ProductEntity, Long> {

        @Query("SELECT COUNT(*) FROM products p WHERE p.user_id = :userId")
        Mono<Integer> countByUserId(Long userId);

        @Query("DELETE FROM products p WHERE p.user_id = :userId")
        Mono<Void> deleteByUserId(Long userId);

        @Query(ProductQuery.STANDARD_QUERY + "HAVING p.user_id = :userId")
        Flux<StandardProductDTO> findAllProducts(Long userId);

        @Query(ProductQuery.FULL_QUERY)
        Flux<FullProductDTO> findFullProductById(Long userId, Long productId);

        @Query(ProductQuery.STANDARD_QUERY + "HAVING c.id = :categoryId AND p.user_id = :userId")
        Flux<StandardProductDTO> findProductsByCategoryId(Long userId, long categoryId);

        @Query(ProductQuery.STANDARD_QUERY + "HAVING s.id = :subcategoryId AND p.user_id = :userId")
        Flux<StandardProductDTO> findProductsBySubcategoryId(Long userId, Long subcategoryId);

        @Query(ProductQuery.SUPPLIER_QUERY + "WHERE su.id = :supplierId AND p.user_id = :userId")
        Flux<SupplierProductDTO> findProductsBySupplierId(Long userId, Long supplierId);

        @Query(ProductQuery.LOCATION_QUERY + "WHERE l.id = :locationId AND p.user_id = :userId")
        Flux<LocationProductDTO> findProductsByLocationid(Long userId, Long locationId);

        @Query(ProductQuery.MINIMUM_STOCK_QUERY)
        Flux<StockProductDTO> findProductsWithMinimumStock(Long userId);

        @Query(ProductQuery.STOCK_QUERY)
        Mono<StockProductDTO> getProductStockById(Long userId, Long productId);

        @Query(ProductQuery.STANDARD_QUERY + "HAVING p.price_currency_id = :currencyId AND " +
                        "p.retail_price >= :min AND p.retail_price <= :max " +
                        "AND p.user_id = :userId ")
        Flux<StandardProductDTO> findProductsBySellingRetailPrice(
                        Long userId, Long currencyId, Double min, Double max);

        @Query(ProductQuery.STANDARD_QUERY + "HAVING p.price_currency_id = :currencyId AND " +
                        "p.wholesale_price >= :min AND p.wholesale_price <= :max " +
                        "AND p.user_id = :userId ")
        Flux<StandardProductDTO> findProductsBySellingWholesalePrice(
                        Long userId, Long currencyId, Double min, Double max);
}
