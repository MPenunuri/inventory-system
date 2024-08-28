package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductSupplierEntity;

import reactor.core.publisher.Mono;

public interface ProductSupplierRepository extends ReactiveCrudRepository<ProductSupplierEntity, Long> {
    Mono<Void> deleteByProductIdAndSupplierId(Long productId, Long supplierId);
}
