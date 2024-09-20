package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LocationPersistencePort {

    public Flux<LocationEntity> getLocations(Long userId);

    public Mono<LocationEntity> registerLocation(Long userId, String name);

    public Mono<LocationEntity> registerLocation(Long userId, String name, String address);

    public Mono<Location> findLocationById(Long userId, Long locationId);

    public Mono<LocationEntity> updateLocationName(Long userId, Long locationId, String name);

    public Mono<LocationEntity> updateLocationAddress(Long userId, Long locationId, String address);

    public Mono<Void> deleteLocationById(Long userId, Long locationId);
}
