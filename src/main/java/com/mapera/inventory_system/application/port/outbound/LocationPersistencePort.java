package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Mono;

public interface LocationPersistencePort {

    public Mono<LocationEntity> registerLocation(String name);

    public Mono<LocationEntity> registerLocation(String name, String address);

    public Mono<Location> findLocationById(Long locationId);

    public Mono<LocationEntity> updateLocationName(Long locationId, String name);

    public Mono<LocationEntity> updateLocationAddress(Long locationId, String address);

    public Mono<Void> deleteLocationById(Long locationId);
}
