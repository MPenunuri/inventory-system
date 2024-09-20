package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.AdminApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.user.UserData;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminApplicationService adminApplicationService;

    @GetMapping("/user")
    public Flux<UserEntity> getUsers() {
        return adminApplicationService.getUsers();
    };

    @GetMapping("/user/{userId}")
    public Mono<UserData> getUserInfo(@PathVariable Long userId) {
        return adminApplicationService.getUserInfo(userId);
    };

    @DeleteMapping("/user/{userId}")
    public Mono<Void> deleteUserAndAllTheirInformation(@PathVariable Long userId) {
        return adminApplicationService.deleteUserAndAllTheirInformation(userId);
    };

}
