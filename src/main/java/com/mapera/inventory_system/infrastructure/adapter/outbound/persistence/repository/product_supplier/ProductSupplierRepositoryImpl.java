package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductSupplierEntity;

import reactor.core.publisher.Mono;

@Repository
public class ProductSupplierRepositoryImpl implements ProductSupplierRespositoryCustom {

    ProductSupplierEntity productSupplierEntity = new ProductSupplierEntity();

    @Autowired
    ProductSupplierCrudRepository productSupplierCrudRepository;

    @Override
    public Mono<Boolean> addProductSupplierRelation(Long productId, Long supplierId) {
        // Check first if there is a current relation.
        return productSupplierCrudRepository.findRelation(productId, supplierId)
                .flatMap(relation -> {
                    // Return Mono with false if the relation already exists
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // If no existing relation, create a new one and return true
                    ProductSupplierEntity productSupplierEntity = new ProductSupplierEntity();
                    productSupplierEntity.setProductId(productId);
                    productSupplierEntity.setSupplierId(supplierId);
                    return productSupplierCrudRepository.save(productSupplierEntity)
                            .then(Mono.just(true));
                }));
    }

    @Override
    public Mono<Boolean> deleteProductSupplierRelation(Long productId, Long supplierId) {
        // Check first if there is a current relation.
        return productSupplierCrudRepository.findRelation(productId, supplierId)
                .flatMap(relation -> {
                    // Delete the relation by its ID and return true if the operation was
                    // successfuld
                    return productSupplierCrudRepository.deleteById(relation.getId())
                            .then(Mono.just(true));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Return Mono with false if the relation already does not exists
                    return Mono.just(false);
                }));
    }

}
