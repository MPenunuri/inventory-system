
package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.location_repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.StockRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.*;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")

@DataR2dbcTest
public class UpdateLocationTest {

        @Autowired
        private LocationRepository locationRepository;

        @Autowired
        private StockRepository stockRepository;

        @BeforeEach
        void setUp() {
                Mono<Void> deleteStocklist = stockRepository.deleteAll();
                Mono<Void> deleteCLocations = locationRepository.deleteAll();

                Mono<Void> setup = deleteStocklist
                                .then(deleteCLocations);

                setup.block();
        }

        @Test
        public void test() {
                String name = "Central warehouse";
                String address = "Liberty 183, San Diego, USA";
                LocationEntity location = new LocationEntity();
                location.setName("Default");

                Mono<Long> savedLocationIdMono = locationRepository.save(location)
                                .map(savedLocation -> savedLocation.getId());

                Mono<LocationEntity> updatedLocation = savedLocationIdMono.flatMap(
                                locationId -> locationRepository.updateLocationName(
                                                locationId,
                                                name))
                                .flatMap(locationWithUpdatedName -> locationRepository.updateLocationAddress(
                                                locationWithUpdatedName.getId(),
                                                address));

                StepVerifier.create(updatedLocation).expectNextMatches(loc -> loc.getAddress().equals(address) &&
                                loc.getName().equals(
                                                name))
                                .verifyComplete();

        }

}
