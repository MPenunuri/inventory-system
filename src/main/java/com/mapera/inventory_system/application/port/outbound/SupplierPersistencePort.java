package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierPersistencePort {

    public Mono<SupplierEntity> registerSupplier(String name);

    public Flux<SupplierEntity> getSuppliers();

    public Mono<SupplierEntity> renameSupplier(Long supplierId, String name);

    public Mono<Void> deleteSupplier(Long supplierId);
}
