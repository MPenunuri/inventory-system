package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.LocationPersistencePort;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LocationApplicationService {

    @Autowired
    private LocationPersistencePort locationPersistencePort;

    public Flux<LocationEntity> getLocations() {
        return locationPersistencePort.getLocations();
    }

    public Mono<LocationEntity> registerLocation(String name) {
        return locationPersistencePort.registerLocation(name);
    }

    public Mono<LocationEntity> registerLocation(String name, String address) {
        return locationPersistencePort.registerLocation(name, address);
    }

    public Mono<Location> findLocationById(Long locationId) {
        return locationPersistencePort.findLocationById(locationId);
    }

    public Mono<LocationEntity> updateLocationName(Long locationId, String name) {
        return locationPersistencePort.updateLocationName(locationId, name);
    }

    public Mono<LocationEntity> updateLocationAddress(Long locationId, String address) {
        return locationPersistencePort.updateLocationAddress(locationId, address);
    }

    public Mono<Void> deleteLocationById(Long locationId) {
        return locationPersistencePort.deleteLocationById(locationId);
    }
}