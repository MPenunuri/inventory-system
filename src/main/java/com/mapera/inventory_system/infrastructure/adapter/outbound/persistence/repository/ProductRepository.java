package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.presentation.dto.MinimumStockProductDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long> {

        String standardQuery = "SELECT * FROM products p " +
                        "JOIN subcategories s ON p.subcategory_id = s.id " +
                        "JOIN categories c ON s.category_id = c.id ";

        String fullQuery = standardQuery +
                        "LEFT JOIN stock_list sl ON sl.product_id = p.id " +
                        "LEFT JOIN locations l ON l.id = sl.location_id " +
                        "LEFT JOIN product_supplier ps ON ps.product_id = p.id " +
                        "LEFT JOIN suppliers su ON su.id = ps.supplier_id ";

        @Query(standardQuery)
        Flux<ProductEntity> findAllProducts();

        @Query(fullQuery + "WHERE p.id = :productId")
        Mono<ProductEntity> findProductById(Long productId);

        @Query(standardQuery + "WHERE c.id = :categoryId")
        Flux<ProductEntity> findProductsByCategoryId(long categoryId);

        @Query(standardQuery + "WHERE s.id = :subcategoryId")
        Flux<ProductEntity> findProductsBySubcategoryId(Long subcategoryId);

        @Query(standardQuery + "LEFT JOIN product_supplier ps ON ps.product_id = p.id " +
                        "LEFT JOIN suppliers su ON su.id = ps.supplier_id " +
                        "WHERE su.id = :supplierId")
        Flux<ProductEntity> findProductsBySupplierId(Long supplierId);

        @Query(standardQuery + "LEFT JOIN stock_list sl ON sl.product_id = p.id" +
                        "LEFT JOIN locations l ON l.id = sl.location_id"
                        + "WHERE l.id = :locationId")
        Flux<ProductEntity> findProductsByLocationid(Long locationId);

        @Query("SELECT p.id, p.name, p.product_presentation, p.minimum_stock, " +
                        "SUM(sl.quantity) AS total_stock " +
                        "FROM products p " +
                        "JOIN stock_list sl ON sl.product_id = p.id " +
                        "GROUP BY p.id " +
                        "HAVING SUM(sl.quantity) < p.minimum_stock")
        Flux<MinimumStockProductDTO> findProductsWithMinimumStock();

}
