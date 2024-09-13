package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Mono;

public interface UserPersistencePort {

    Mono<UserEntity> registerUser(String name, String email, String password);

    Mono<UserEntity> findUserById(Long id);

    Mono<UserEntity> findUserByEmail(String email);

    Mono<UserEntity> updateUserName(Long id, String name);

    Mono<UserEntity> updateUserEmail(Long id, String email);

    Mono<UserEntity> updateUserPassword(Long id, String password);

    Mono<Void> deleteUserById(Long id);
}
