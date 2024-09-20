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

    public Flux<LocationEntity> getLocations(Long userId) {
        return locationPersistencePort.getLocations(userId);
    }

    public Mono<LocationEntity> registerLocation(
            Long userId, String name) {
        return locationPersistencePort.registerLocation(userId, name);
    }

    public Mono<LocationEntity> registerLocation(
            Long userId, String name, String address) {
        return locationPersistencePort.registerLocation(userId, name, address);
    }

    public Mono<Location> findLocationById(Long userId, Long locationId) {
        return locationPersistencePort.findLocationById(userId, locationId);
    }

    public Mono<LocationEntity> updateLocationName(
            Long userId, Long locationId, String name) {
        return locationPersistencePort.updateLocationName(
                userId, locationId, name);
    }

    public Mono<LocationEntity> updateLocationAddress(
            Long userId, Long locationId, String address) {
        return locationPersistencePort.updateLocationAddress(
                userId, locationId, address);
    }

    public Mono<Void> deleteLocationById(Long userId, Long locationId) {
        return locationPersistencePort.deleteLocationById(userId, locationId);
    }
}