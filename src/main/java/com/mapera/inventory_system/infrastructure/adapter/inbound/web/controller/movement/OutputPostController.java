package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.movement.RegisterMovementRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/movement")
@Validated
public class OutputPostController {

        @Autowired
        MovementApplicationService movementApplicationService;

        @PostMapping("/sale")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<MovementEntity> addSalesOutputMovement(
                        @RequestBody RegisterMovementRequest request) {
                return movementApplicationService.addSalesOutputMovement(
                                request.getProductId(), request.getDateTime(),
                                request.getReason(), request.getComment(),
                                request.getQuantity(),
                                request.getFromLocationId(), request.getTransactionSubtype(),
                                request.getTransactionValue(), request.getTransactionCurrencyId());
        }

        @PostMapping("/supplier-return")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<MovementEntity> addSupplierReturnOutputMovement(
                        @RequestBody RegisterMovementRequest request) {
                return movementApplicationService.addSupplierReturnOutputMovement(
                                request.getProductId(), request.getDateTime(),
                                request.getReason(), request.getComment(),
                                request.getQuantity(), request.getSupplierId(),
                                request.getFromLocationId(), request.getTransactionSubtype(),
                                request.getTransactionValue(), request.getTransactionCurrencyId());
        }

        @PostMapping("/output-adjustment")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<MovementEntity> addInventoryAdjustmentOutputMovement(
                        @RequestBody RegisterMovementRequest request) {
                return movementApplicationService.addInventoryAdjustmentOutputMovement(
                                request.getProductId(), request.getDateTime(),
                                request.getReason(), request.getComment(),
                                request.getQuantity(), request.getFromLocationId());
        }

        @PostMapping("/internal-consumption")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<MovementEntity> addInternalConsumptionOutputMovement(
                        @RequestBody RegisterMovementRequest request) {
                return movementApplicationService.addInternalConsumptionOutputMovement(
                                request.getProductId(), request.getDateTime(),
                                request.getReason(), request.getComment(),
                                request.getQuantity(), request.getFromLocationId());
        }
}
