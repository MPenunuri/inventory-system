package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

public interface LocationCrudRepository extends ReactiveCrudRepository<LocationEntity, Long> {

}
