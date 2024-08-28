package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

public interface CategoryRepository extends ReactiveCrudRepository<CategoryEntity, Long> {

}
