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

        @Query(ProductQuery.STANDARD_QUERY)
        Flux<StandardProductDTO> findAllProducts();

        @Query(ProductQuery.FULL_QUERY)
        Flux<FullProductDTO> findFullProductById(Long productId);

        @Query(ProductQuery.STANDARD_QUERY + "WHERE c.id = :categoryId")
        Flux<StandardProductDTO> findProductsByCategoryId(long categoryId);

        @Query(ProductQuery.STANDARD_QUERY + "WHERE s.id = :subcategoryId")
        Flux<StandardProductDTO> findProductsBySubcategoryId(Long subcategoryId);

        @Query(ProductQuery.SUPPLIER_QUERY + "WHERE su.id = :supplierId")
        Flux<SupplierProductDTO> findProductsBySupplierId(Long supplierId);

        @Query(ProductQuery.LOCATION_QUERY + "WHERE l.id = :locationId")
        Flux<LocationProductDTO> findProductsByLocationid(Long locationId);

        @Query(ProductQuery.MINIMUM_STOCK_QUERY + "HAVING SUM(sl.quantity) < p.minimum_stock")
        Flux<StockProductDTO> findProductsWithMinimumStock();

        @Query(ProductQuery.STOCK_QUERY)
        Mono<StockProductDTO> getProductStockById(Long productId);

        @Query(ProductQuery.STANDARD_QUERY + "WHERE p.price_currency_id = :currencyId AND " +
                        "p.retail_price >= :min AND p.retail_price <= :max")
        Flux<StandardProductDTO> findProductsBySellingRetailPrice(Long currencyId, Double min, Double max);

        @Query(ProductQuery.STANDARD_QUERY + "WHERE p.price_currency_id = :currencyId AND " +
                        "p.wholesale_price >= :min AND p.wholesale_price <= :max")
        Flux<StandardProductDTO> findProductsBySellingWholesalePrice(Long currencyId, Double min, Double max);
}
