package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.SupplierPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SupplierRepositoryImpl
        implements SupplierRepositoryCustom, SupplierPersistencePort {

    @Autowired
    SupplierCrudRepository supplierCrudRepository;

    @Override
    public Mono<SupplierEntity> registerSupplier(String name) {
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setName(name);
        return supplierCrudRepository.save(supplierEntity);
    }

    @Override
    public Flux<SupplierEntity> getSuppliers() {
        return supplierCrudRepository.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("Not suppliers found")));
    }

    @Override
    public Mono<SupplierEntity> renameSupplier(Long supplierId, String name) {
        return supplierCrudRepository.findById(supplierId).flatMap(s -> {
            s.setName(name);
            return supplierCrudRepository.save(s);
        }).switchIfEmpty(Mono.error(new RuntimeException("Supplier not found")));
    }

    @Override
    public Mono<Void> deleteSupplier(Long supplierId) {
        return supplierCrudRepository.deleteById(supplierId)
                .onErrorMap(error -> {
                    if (error instanceof DataIntegrityViolationException) {
                        return new IllegalStateException(
                                "Failed to delete supplier with ID: " + supplierId + ". " +
                                        "The supplier is associated with other records and cannot be deleted. "
                                        +
                                        "Please remove any related registry before attempting to delete this supplier.",
                                error);
                    }
                    return new IllegalArgumentException(
                            "Failed to delete supplier with ID: " + supplierId + ". Unexpected error occurred.");
                });
    }

}
