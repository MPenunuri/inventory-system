package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddAcquisitionEntryMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddCustomerReturnEntryMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddInternalConsumptionOutputMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddInventoryAdjustmentEntryMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddInventoryAdjustmentOutputMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddProductionEntryMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddSalesOutputMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddSupplierReturnOutputMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.AddTransferMovement;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method.CancelMovementById;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;

import reactor.core.publisher.Mono;

@Repository
public class MovementRepositoryImpl implements MovementRepositoryCustom {

    @Autowired
    MovementCrudRepository movementCrudRepository;

    @Autowired
    StockRepository stockRepository;

    @Override
    public Mono<MovementEntity> addAcquisitionEntryMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long supplierId, Long toLocationId,
            String transactionSubtype,
            double transactionValue, Long transactionCurrencyId) {
        return AddAcquisitionEntryMovement.execute(
                movementCrudRepository, stockRepository,
                productId, dateTime, reason,
                comment, quantity, supplierId, toLocationId,
                transactionSubtype, transactionValue, transactionCurrencyId);
    }

    @Override
    public Mono<MovementEntity> addCustomerReturnEntryMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long toLocationId, String transactionSubtype,
            double transactionValue, Long transactionCurrencyId) {
        return AddCustomerReturnEntryMovement.execute(
                movementCrudRepository, stockRepository,
                productId, dateTime, reason,
                comment, quantity, toLocationId,
                transactionSubtype, transactionValue, transactionCurrencyId);
    }

    @Override
    public Mono<MovementEntity> addInventoryAdjustmentEntryMovement(
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity, Long toLocationId) {
        return AddInventoryAdjustmentEntryMovement.execute(
                movementCrudRepository, stockRepository, productId,
                dateTime, reason, comment, quantity, toLocationId);
    }

    @Override
    public Mono<MovementEntity> addProductionEntryMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long toLocationId, String transactionSubtype,
            double transactionValue, Long transactionCurrencyId) {
        return AddProductionEntryMovement.execute(
                movementCrudRepository, stockRepository,
                productId, dateTime, reason,
                comment, quantity, toLocationId,
                transactionSubtype, transactionValue, transactionCurrencyId);
    }

    @Override
    public Mono<MovementEntity> addSalesOutputMovement(
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity,
            Long fromLocationId, String transactionSubtype,
            double transactionValue, Long transactionCurrencyId) {
        return AddSalesOutputMovement.execute(
                movementCrudRepository, stockRepository,
                productId, dateTime, reason, comment,
                quantity, fromLocationId, transactionSubtype,
                transactionValue, transactionCurrencyId);
    }

    @Override
    public Mono<MovementEntity> addSupplierReturnOutputMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long supplierId,
            Long fromLocationId, String transactionSubtype,
            double transactionValue, Long transactionCurrencyId) {
        return AddSupplierReturnOutputMovement.execute(
                movementCrudRepository, stockRepository,
                productId, dateTime, reason, comment, quantity,
                supplierId, fromLocationId, transactionSubtype,
                transactionValue, transactionCurrencyId);
    }

    @Override
    public Mono<MovementEntity> addInventoryAdjustmentOutputMovement(
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity, Long fromLocationId) {
        return AddInventoryAdjustmentOutputMovement.execute(
                movementCrudRepository, stockRepository, productId,
                dateTime, reason, comment, quantity, fromLocationId);
    }

    @Override
    public Mono<MovementEntity> addInternalConsumptionOutputMovement(
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity, Long fromLocationId) {
        return AddInternalConsumptionOutputMovement.execute(
                movementCrudRepository, stockRepository, productId,
                dateTime, reason, comment, quantity, fromLocationId);
    }

    @Override
    public Mono<MovementEntity> addTransferMovement(Long productId,
            LocalDateTime dateTime, String reason,
            String comment, int quantity, Long fromLocationId, Long toLocationId) {
        return AddTransferMovement.execute(
                movementCrudRepository, stockRepository,
                productId, dateTime, reason, comment,
                quantity, fromLocationId, toLocationId);
    }

    @Override
    public Mono<Boolean> cancelMovementById(Long movementId) {
        return CancelMovementById.execute(movementCrudRepository, stockRepository, movementId);
    }

}
