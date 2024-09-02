package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Mono;

@Repository
public class LocationRepositoryImpl implements LocationRepositoryCustom {

    @Autowired
    private LocationCrudRepository locationCrudRepository;

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
}
