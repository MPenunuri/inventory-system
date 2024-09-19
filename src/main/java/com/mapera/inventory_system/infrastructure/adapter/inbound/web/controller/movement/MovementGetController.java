package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public Flux<StandardMovementDTO> getMovements() {
        return movementApplicationService.getMovements();
    }

    @GetMapping("/entry")
    public Flux<EntryMovementDTO> getEntries() {
        return movementApplicationService.getEntries();
    }

    @GetMapping("/output")
    public Flux<OutputMovementDTO> getOutputs() {
        return movementApplicationService.getOutputs();
    }

    @GetMapping("/transfer")
    public Flux<TransferMovementDTO> getTransfers() {
        return movementApplicationService.getTransfers();
    }

    @GetMapping("/product/{productId}")
    public Flux<StandardMovementDTO> getMovementsByProductId(
            @PathVariable Long productId) {
        return movementApplicationService.getMovementsByProductId(productId);
    }

}
