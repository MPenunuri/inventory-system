package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

public interface SupplierRepository extends ReactiveCrudRepository<SupplierEntity, Long> {

}
