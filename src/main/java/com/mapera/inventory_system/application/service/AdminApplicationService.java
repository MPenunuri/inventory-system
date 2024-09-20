package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.AdminPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.user.UserData;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AdminApplicationService {

    @Autowired
    private AdminPersistencePort adminPersistencePort;

    public Flux<UserEntity> getUsers() {
        return adminPersistencePort.getUsers();
    };

    public Mono<UserData> getUserInfo(Long userId) {
        return adminPersistencePort.getUserInfo(userId);

    };

    public Mono<Void> deleteUserAndAllTheirInformation(Long userId) {
        return adminPersistencePort.deleteUserAndAllTheirInformation(userId);
    };

}
