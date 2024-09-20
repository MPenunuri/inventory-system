package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.EntryMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.OutputMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.StandardMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.TransferMovementDTO;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/secure/movement")
@Validated
public class MovementGetController {

    @Autowired
    MovementApplicationService movementApplicationService;

    @Autowired
    private AuthenticationService authService;

    @GetMapping
    public Flux<StandardMovementDTO> getMovements() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getMovements(userId);
        });
    }

    @GetMapping("/entry")
    public Flux<EntryMovementDTO> getEntries() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getEntries(userId);
        });
    }

    @GetMapping("/output")
    public Flux<OutputMovementDTO> getOutputs() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getOutputs(userId);
        });
    }

    @GetMapping("/transfer")
    public Flux<TransferMovementDTO> getTransfers() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getTransfers(userId);
        });
    }

    @GetMapping("/product/{productId}")
    public Flux<StandardMovementDTO> getMovementsByProductId(
            @PathVariable Long productId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getMovementsByProductId(
                    userId, productId);
        });
    }

}
