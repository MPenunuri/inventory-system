package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement;

import java.time.LocalDateTime;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;

import reactor.core.publisher.Mono;

public interface MovementRepositoryCustom {
        public Mono<MovementEntity> addAcquisitionEntryMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long supplierId, Long toLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addCustomerReturnEntryMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long toLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addInventoryAdjustmentEntryMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long toLocationId);

        public Mono<MovementEntity> addProductionEntryMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long toLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addSalesOutputMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long fromLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addSupplierReturnOutputMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long supplierId, Long fromLocationId,
                        String transactionSubtype, double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addInventoryAdjustmentOutputMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long fromLocationId);

        public Mono<MovementEntity> addInternalConsumptionOutputMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long fromLocationId);

        public Mono<MovementEntity> addTransferMovement(
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long fromLocationId, Long toLocationId);

        public Mono<Boolean> cancelMovementById(Long movementId);

}
