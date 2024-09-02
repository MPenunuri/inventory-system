package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Mono;

public interface LocationRepositoryCustom {

    public Mono<LocationEntity> updateLocationName(Long locationId, String name);

    public Mono<LocationEntity> updateLocationAddress(Long locationId, String address);
}
