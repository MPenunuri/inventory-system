package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.SupplierPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.supplier.SupplierDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SupplierApplicationService {

    @Autowired
    private SupplierPersistencePort supplierPersistencePort;

    public Mono<SupplierEntity> registerSupplier(Long userId, String name) {
        return supplierPersistencePort.registerSupplier(userId, name);
    }

    public Flux<SupplierDTO> getSuppliers(Long userId) {
        return supplierPersistencePort.getSuppliers(userId);
    }

    public Flux<SupplierEntity> getSuppliersWithProductRelation(Long userId, Long productId) {
        return supplierPersistencePort.getSuppliersWithProductRelation(userId, productId);
    }

    public Flux<SupplierEntity> getSuppliersWithNoProductRelation(Long userId, Long productId) {
        return supplierPersistencePort.getSuppliersWithNoProductRelation(userId, productId);
    }

    public Mono<SupplierEntity> renameSupplier(Long userId, Long supplierId, String name) {
        return supplierPersistencePort.renameSupplier(userId, supplierId, name);
    }

    public Mono<Void> deleteSupplier(Long userId, Long supplierId) {
        return supplierPersistencePort.deleteSupplier(userId, supplierId);
    }

}
