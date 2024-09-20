package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Flux;

public interface LocationCrudRepository extends ReactiveCrudRepository<LocationEntity, Long> {

    @Query("SELECT * FROM locations l WHERE l.user_id = :userId ")
    public Flux<LocationEntity> findAllUserLocations(Long userId);
}
