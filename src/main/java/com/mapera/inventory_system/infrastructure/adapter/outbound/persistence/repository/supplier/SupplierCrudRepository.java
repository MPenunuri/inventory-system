package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

public interface SupplierCrudRepository extends ReactiveCrudRepository<SupplierEntity, Long> {

}
