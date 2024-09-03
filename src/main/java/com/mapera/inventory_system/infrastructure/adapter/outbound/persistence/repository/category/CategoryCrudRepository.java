package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CategoryEntity;

public interface CategoryCrudRepository extends ReactiveCrudRepository<CategoryEntity, Long> {

}
