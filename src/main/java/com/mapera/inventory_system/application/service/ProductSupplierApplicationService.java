package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.ProductSupplierPersistencePort;

import reactor.core.publisher.Mono;

@Service
public class ProductSupplierApplicationService {

    @Autowired
    private ProductSupplierPersistencePort productSupplierPersistencePort;

    public Mono<Boolean> addProductSupplierRelation(Long userId, Long productId, Long supplierId) {
        return productSupplierPersistencePort.addProductSupplierRelation(userId, productId, supplierId);
    }

    public Mono<Boolean> deleteProductSupplierRelation(Long userId, Long productId, Long supplierId) {
        return productSupplierPersistencePort.deleteProductSupplierRelation(userId, productId, supplierId);
    }
}
