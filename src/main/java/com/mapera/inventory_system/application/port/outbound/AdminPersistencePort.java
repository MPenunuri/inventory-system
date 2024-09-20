package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.user.UserData;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminPersistencePort {
    public Flux<UserEntity> getUsers();

    public Mono<UserData> getUserInfo(Long userId);

    public Mono<Void> deleteUserAndAllTheirInformation(Long userId);
}
