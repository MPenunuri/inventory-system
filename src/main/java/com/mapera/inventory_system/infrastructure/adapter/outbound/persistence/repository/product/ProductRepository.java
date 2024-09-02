package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import org.springframework.data.r2dbc.repository.Query;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.FullProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.LocationProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.MinimumStockProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.StandardProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.product.SupplierProductDTO;

import reactor.core.publisher.Flux;

public interface ProductRepository
                extends ProductCrudRepository, ProductRepositoryCustom {

        @Query(ProductQuery.STANDARD_QUERY)
        Flux<StandardProductDTO> findAllProducts();

        @Query(ProductQuery.FULL_QUERY + "WHERE p.id = :productId")
        Flux<FullProductDTO> findProductById(Long productId);

        @Query(ProductQuery.STANDARD_QUERY + "WHERE c.id = :categoryId")
        Flux<StandardProductDTO> findProductsByCategoryId(long categoryId);

        @Query(ProductQuery.STANDARD_QUERY + "WHERE s.id = :subcategoryId")
        Flux<StandardProductDTO> findProductsBySubcategoryId(Long subcategoryId);

        @Query(ProductQuery.SUPPLIER_QUERY + "WHERE su.id = :supplierId")
        Flux<SupplierProductDTO> findProductsBySupplierId(Long supplierId);

        @Query(ProductQuery.LOCATION_QUERY + "WHERE l.id = :locationId")
        Flux<LocationProductDTO> findProductsByLocationid(Long locationId);

        @Query(ProductQuery.STOCK_QUERY + "HAVING SUM(sl.quantity) < p.minimum_stock")
        Flux<MinimumStockProductDTO> findProductsWithMinimumStock();

}
