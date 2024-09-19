package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.movement.RegisterMovementRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/secure/movement")
@Validated
public class EntryPostController {

        @Autowired
        MovementApplicationService movementApplicationService;

        @PostMapping("/acquisition")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<MovementEntity> addAcquisitionEntryMovement(
                        @RequestBody RegisterMovementRequest request) {
                return movementApplicationService.addAcquisitionEntryMovement(
                                request.getProductId(), request.getDateTime(),
                                request.getReason(), request.getComment(),
                                request.getQuantity(), request.getSupplierId(),
                                request.getToLocationId(), request.getTransactionSubtype(),
                                request.getTransactionValue(), request.getTransactionCurrencyId());
        }

        @PostMapping("/customer-return")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<MovementEntity> addCustomerReturnEntryMovement(
                        @RequestBody RegisterMovementRequest request) {
                return movementApplicationService.addCustomerReturnEntryMovement(
                                request.getProductId(), request.getDateTime(),
                                request.getReason(), request.getComment(),
                                request.getQuantity(),
                                request.getToLocationId(), request.getTransactionSubtype(),
                                request.getTransactionValue(), request.getTransactionCurrencyId());
        }

        @PostMapping("/entry-adjusment")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<MovementEntity> addInventoryAdjustmentEntryMovement(
                        @RequestBody RegisterMovementRequest request) {
                return movementApplicationService.addInventoryAdjustmentEntryMovement(
                                request.getProductId(), request.getDateTime(),
                                request.getReason(), request.getComment(),
                                request.getQuantity(), request.getToLocationId());
        }

        @PostMapping("/production")
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<MovementEntity> addProductionEntryMovement(
                        @RequestBody RegisterMovementRequest request) {
                return movementApplicationService.addProductionEntryMovement(
                                request.getProductId(), request.getDateTime(),
                                request.getReason(), request.getComment(),
                                request.getQuantity(), request.getToLocationId(),
                                request.getTransactionSubtype(),
                                request.getTransactionValue(),
                                request.getTransactionCurrencyId());
        }

}
