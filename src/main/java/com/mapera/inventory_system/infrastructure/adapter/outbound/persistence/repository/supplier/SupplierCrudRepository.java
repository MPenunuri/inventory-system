package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.supplier.SupplierDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierCrudRepository extends ReactiveCrudRepository<SupplierEntity, Long> {

    @Query("SELECT COUNT(*) FROM suppliers s WHERE s.user_id = :userId ")
    Mono<Integer> countByUserId(Long userId);

    @Query("DELETE FROM suppliers s WHERE s.user_id = :userId ")
    Mono<Void> deleteByUserId(Long userId);

    @Query("SELECT s.id AS supplier_id, " +
            "s.name AS supplier_name, " +
            "COUNT(DISTINCT ps.id) AS products, " +
            "COUNT(DISTINCT m.id) AS movements " +
            "FROM suppliers s " +
            "LEFT JOIN product_supplier ps ON ps.supplier_id = s.id " +
            "LEFT JOIN movements m ON m.supplier_id = s.id " +
            "WHERE s.user_id = :userId " +
            "GROUP BY s.id, s.name ")
    public Flux<SupplierDTO> findAllUserSuppliers(Long userId);

    @Query("SELECT DISTINCT s.id AS supplier_id, " +
            "s.name AS supplier_name " +
            "FROM suppliers s " +
            "LEFT JOIN product_supplier ps ON ps.supplier_id = s.id " +
            "WHERE (ps.product_id IS NULL OR ps.product_id != :productId) AND p.user_id = :userId ")
    Flux<SupplierEntity> getSuppliersWithNoProductRelation(Long userId, Long productId);
}
