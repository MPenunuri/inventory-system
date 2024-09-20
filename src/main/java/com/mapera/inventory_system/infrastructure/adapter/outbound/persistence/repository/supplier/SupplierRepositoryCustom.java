package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Mono;

public interface SupplierRepositoryCustom {
    public Mono<SupplierEntity> renameSupplier(Long userId, Long supplierId, String name);
}
