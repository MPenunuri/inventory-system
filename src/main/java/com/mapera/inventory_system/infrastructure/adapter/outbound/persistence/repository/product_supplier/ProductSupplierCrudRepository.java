package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductSupplierEntity;

import reactor.core.publisher.Mono;

public interface ProductSupplierCrudRepository extends ReactiveCrudRepository<ProductSupplierEntity, Long> {

    @Query("SELECT * FROM product_supplier ps WHERE ps.product_id = :productId AND ps.supplier_id = :supplierId")
    public Mono<ProductSupplierEntity> findRelation(Long productId, Long supplierId);

}
