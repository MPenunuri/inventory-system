package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Mono;

public interface UserCrudRepository extends ReactiveCrudRepository<UserEntity, Long> {

    Mono<UserEntity> findUserByEmail(String name);
}
