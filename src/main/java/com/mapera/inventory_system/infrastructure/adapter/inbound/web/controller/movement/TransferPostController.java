package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.movement.RegisterMovementRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/movement")
@Validated
public class TransferPostController {

    @Autowired
    MovementApplicationService movementApplicationService;

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovementEntity> addTransferMovement(
            @RequestBody RegisterMovementRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return movementApplicationService.addTransferMovement(
                    userId,
                    request.getProductId(), request.getDateTime(),
                    request.getReason(), request.getComment(),
                    request.getQuantity(),
                    request.getFromLocationId(), request.getToLocationId());
        });
    }
}
