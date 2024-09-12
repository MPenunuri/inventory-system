package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.SupplierPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SupplierApplicationService {

    @Autowired
    private SupplierPersistencePort supplierPersistencePort;

    public Mono<SupplierEntity> registerSupplier(String name) {
        return supplierPersistencePort.registerSupplier(name);
    }

    public Flux<SupplierEntity> getSuppliers() {
        return supplierPersistencePort.getSuppliers();
    }

    public Mono<SupplierEntity> renameSupplier(Long supplierId, String name) {
        return supplierPersistencePort.renameSupplier(supplierId, name);
    }

    public Mono<Void> deleteSupplier(Long supplierId) {
        return supplierPersistencePort.deleteSupplier(supplierId);
    }

}
