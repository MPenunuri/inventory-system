package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.service.MovementApplicationService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/movement")
@Validated
public class MovementDeleteController {

    @Autowired
    MovementApplicationService movementApplicationService;

    @Autowired
    private AuthenticationService authService;

    @DeleteMapping("/{movementId}")
    public Mono<Boolean> cancelMovementById(@PathVariable Long movementId) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return movementApplicationService.cancelMovementById(userId, movementId);
        });

    }

}
