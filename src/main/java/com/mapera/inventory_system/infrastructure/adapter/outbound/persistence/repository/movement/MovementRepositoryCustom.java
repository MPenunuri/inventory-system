package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement;

import java.time.LocalDateTime;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;

import reactor.core.publisher.Mono;

public interface MovementRepositoryCustom {
        public Mono<MovementEntity> addAcquisitionEntryMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long supplierId, Long toLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addCustomerReturnEntryMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long toLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addInventoryAdjustmentEntryMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long toLocationId);

        public Mono<MovementEntity> addProductionEntryMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long toLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addSalesOutputMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long fromLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addSupplierReturnOutputMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long supplierId, Long fromLocationId,
                        String transactionSubtype, double transactionValue, Long transactionCurrencyId);

        public Mono<MovementEntity> addInventoryAdjustmentOutputMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long fromLocationId);

        public Mono<MovementEntity> addInternalConsumptionOutputMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long fromLocationId);

        public Mono<MovementEntity> addTransferMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason, String comment,
                        int quantity, Long fromLocationId, Long toLocationId);

        public Mono<Boolean> cancelMovementById(Long userId, Long movementId);

}
