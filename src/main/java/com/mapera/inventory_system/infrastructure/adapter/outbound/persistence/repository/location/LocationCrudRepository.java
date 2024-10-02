package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.location.LocationDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LocationCrudRepository extends ReactiveCrudRepository<LocationEntity, Long> {

    @Query("SELECT COUNT(*) FROM locations l WHERE l.user_id = :userId ")
    Mono<Integer> countByUserId(Long userId);

    @Query("DELETE FROM locations l WHERE l.user_id = :userId ")
    Mono<Void> deleteByUserId(Long userId);

    @Query("SELECT l.id AS location_id, " +
            "l.name AS location_name, " +
            "l.address AS location_address, " +
            "COUNT(DISTINCT sl.id) AS products, " +
            "COUNT(DISTINCT m.id) AS movements " +
            "FROM locations l " +
            "LEFT JOIN stock_list sl ON sl.location_id = l.id " +
            "LEFT JOIN movements m ON m.from_location_id = l.id OR m.to_location_id = l.id " +
            "WHERE l.user_id = :userId " +
            "GROUP BY l.id, l.name, l.address ")
    public Flux<LocationDTO> findAllUserLocations(Long userId);
}
