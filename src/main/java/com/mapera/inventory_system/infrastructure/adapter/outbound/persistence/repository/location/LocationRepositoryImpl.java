package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.LocationPersistencePort;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Mono;

@Repository
public class LocationRepositoryImpl implements LocationRepositoryCustom, LocationPersistencePort {

    @Autowired
    private LocationCrudRepository locationCrudRepository;

    @Override
    public Mono<LocationEntity> registerLocation(String name) {
        return registerLocation(name, null);
    }

    @Override
    public Mono<LocationEntity> registerLocation(String name, String address) {
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setName(name);
        if (address != null) {
            locationEntity.setAddress(address);
        }
        return locationCrudRepository.save(locationEntity);
    }

    @Override
    public Mono<LocationEntity> updateLocationName(Long locationId, String name) {
        return locationCrudRepository.findById(locationId)
                .flatMap(location -> {
                    location.setName(name);
                    return locationCrudRepository.save(location);
                });
    }

    @Override
    public Mono<LocationEntity> updateLocationAddress(Long locationId, String address) {
        return locationCrudRepository.findById(locationId)
                .flatMap(location -> {
                    location.setAddress(address);
                    return locationCrudRepository.save(location);
                });
    }

    @Override
    public Mono<Location> findLocationById(Long locationId) {
        return locationCrudRepository.findById(locationId).flatMap(
                savedLoc -> {
                    Location location = new Location(
                            savedLoc.getId(),
                            savedLoc.getName(),
                            savedLoc.getAddress());
                    return Mono.just(location);
                });
    }

    @Override
    public Mono<Void> deleteLocationById(Long locationId) {
        return locationCrudRepository.deleteById(locationId);
    }
}
