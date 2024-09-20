package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier;

import reactor.core.publisher.Mono;

public interface ProductSupplierRespositoryCustom {
    public Mono<Boolean> addProductSupplierRelation(Long userId, Long productId, Long supplierId);

    public Mono<Boolean> deleteProductSupplierRelation(Long userId, Long productId, Long supplierId);
}
