package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.supplier.SupplierDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierPersistencePort {

    public Mono<SupplierEntity> registerSupplier(Long userId, String name);

    public Flux<SupplierDTO> getSuppliers(Long userId);

    public Flux<SupplierEntity> getSuppliersWithProductRelation(Long userId, Long productId);

    public Flux<SupplierEntity> getSuppliersWithNoProductRelation(Long userId, Long productId);

    public Mono<SupplierEntity> renameSupplier(Long userId, Long supplierId, String name);

    public Mono<Void> deleteSupplier(Long userId, Long supplierId);
}
