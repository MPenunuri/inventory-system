package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

public interface ProductCrudRepository extends ReactiveCrudRepository<ProductEntity, Long> {

}
