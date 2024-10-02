package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.LocationPersistencePort;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.location.LocationDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LocationRepositoryImpl implements LocationRepositoryCustom, LocationPersistencePort {

    @Autowired
    private LocationCrudRepository locationCrudRepository;

    @Override
    public Mono<LocationEntity> registerLocation(Long userId, String name) {
        return registerLocation(userId, name, null);
    }

    @Override
    public Mono<LocationEntity> registerLocation(Long userId, String name, String address) {
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setUser_id(userId);
        locationEntity.setName(name);
        if (address != null) {
            locationEntity.setAddress(address);
        }
        return locationCrudRepository.save(locationEntity);
    }

    @Override
    public Mono<LocationEntity> updateLocationName(Long userId, Long locationId, String name) {
        return locationCrudRepository.findById(locationId)
                .flatMap(location -> {
                    if (!location.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    location.setName(name);
                    return locationCrudRepository.save(location);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Location not found")));
    }

    @Override
    public Mono<LocationEntity> updateLocationAddress(Long userId, Long locationId, String address) {
        return locationCrudRepository.findById(locationId)
                .flatMap(location -> {
                    if (!location.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    location.setAddress(address);
                    return locationCrudRepository.save(location);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Location not found")));
    }

    @Override
    public Mono<Location> findLocationById(Long userId, Long locationId) {
        return locationCrudRepository.findById(locationId).flatMap(
                savedLoc -> {
                    if (!savedLoc.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    Location location = new Location(
                            savedLoc.getId(),
                            savedLoc.getName(),
                            savedLoc.getAddress());
                    return Mono.just(location);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Location not found")));
    }

    @Override
    public Mono<Void> deleteLocationById(Long userId, Long locationId) {
        return locationCrudRepository.findById(locationId)
                .switchIfEmpty(Mono.error(new RuntimeException("Location not found")))
                .flatMap(l -> {
                    if (!l.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    return locationCrudRepository.delete(l);
                })
                .onErrorMap(error -> {
                    if (error instanceof DataIntegrityViolationException) {
                        return new IllegalStateException(
                                "Failed to delete location with ID: " + locationId + ". " +
                                        "The location is associated with other records and cannot be deleted. "
                                        +
                                        "Please remove any related registry before attempting to delete this location.");
                    }
                    return new IllegalArgumentException(
                            "Failed to delete location with ID: " + locationId + ". Unexpected error occurred.");
                });
    }

    @Override
    public Flux<LocationDTO> getLocations(Long userId) {
        return locationCrudRepository.findAllUserLocations(userId)
                .switchIfEmpty(Mono.error(new RuntimeException("No locations found")));
    }
}
