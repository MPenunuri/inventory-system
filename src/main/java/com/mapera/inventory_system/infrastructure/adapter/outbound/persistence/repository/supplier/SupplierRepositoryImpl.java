package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.SupplierPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.supplier.SupplierDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SupplierRepositoryImpl
        implements SupplierRepositoryCustom, SupplierPersistencePort {

    @Autowired
    SupplierCrudRepository supplierCrudRepository;

    @Override
    public Mono<SupplierEntity> registerSupplier(Long userId, String name) {
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setUser_id(userId);
        supplierEntity.setName(name);
        return supplierCrudRepository.save(supplierEntity);
    }

    @Override
    public Flux<SupplierDTO> getSuppliers(Long userId) {
        return supplierCrudRepository.findAllUserSuppliers(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not suppliers found")));
    }

    @Override
    public Flux<SupplierEntity> getSuppliersWithNoProductRelation(Long userId, Long productId) {
        return supplierCrudRepository.getSuppliersWithNoProductRelation(userId, productId)
                .switchIfEmpty(Mono.error(new RuntimeException("Not suppliers found")));
    }

    @Override
    public Mono<SupplierEntity> renameSupplier(Long userId, Long supplierId, String name) {
        return supplierCrudRepository.findById(supplierId).flatMap(s -> {
            if (!s.getUser_id().equals(userId)) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            s.setName(name);
            return supplierCrudRepository.save(s);
        }).switchIfEmpty(Mono.error(new RuntimeException("Supplier not found")));
    }

    @Override
    public Mono<Void> deleteSupplier(Long userId, Long supplierId) {
        return supplierCrudRepository.findById(supplierId)
                .switchIfEmpty(Mono.error(new RuntimeException("Supplier not found")))
                .flatMap(s -> {
                    if (!s.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    return supplierCrudRepository.delete(s);
                }).onErrorMap(error -> {
                    if (error instanceof DataIntegrityViolationException) {
                        return new IllegalStateException(
                                "Failed to delete supplier with ID: " + supplierId + ". " +
                                        "The supplier is associated with other records and cannot be deleted. "
                                        +
                                        "Please remove any related registry before attempting to delete this supplier.");
                    }
                    return new IllegalArgumentException(
                            "Failed to delete supplier with ID: " + supplierId + ". Unexpected error occurred.");
                });
    }

}
