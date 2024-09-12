package com.mapera.inventory_system.application.port.outbound;

import reactor.core.publisher.Mono;

public interface ProductSupplierPersistencePort {
    public Mono<Boolean> addProductSupplierRelation(Long productId, Long supplierId);

    public Mono<Boolean> deleteProductSupplierRelation(Long productId, Long supplierId);
}
