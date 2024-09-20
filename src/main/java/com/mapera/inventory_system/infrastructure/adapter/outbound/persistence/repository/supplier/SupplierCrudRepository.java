package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SupplierCrudRepository extends ReactiveCrudRepository<SupplierEntity, Long> {

    @Query("SELECT COUNT(*) FROM suppliers s WHERE s.user_id = :userId ")
    Mono<Integer> countByUserId(Long userId);

    @Query("DELETE FROM suppliers s WHERE s.user_id = :userId ")
    Mono<Void> deleteByUserId(Long userId);

    @Query("SELECT * FROM suppliers s WHERE s.user_id = :userId ")
    public Flux<SupplierEntity> findAllUserSuppliers(Long userId);
}
