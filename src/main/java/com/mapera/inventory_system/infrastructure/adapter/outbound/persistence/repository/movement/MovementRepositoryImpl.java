package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.MovementPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AcquisitionDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AverageCostProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AverageSellProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.CustomerReturnDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.EntryMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.OutputMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.ProductionDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.SaleDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.StandardMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.SupplierReturnDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.TransferMovementDTO;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MovementRepositoryImpl implements MovementRepositoryCustom, MovementPersistencePort {

        @Autowired
        MovementCrudRepository movementCrudRepository;

        @Autowired
        StockRepository stockRepository;

        @Override
        public Mono<MovementEntity> addAcquisitionEntryMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason,
                        String comment, int quantity, Long supplierId, Long toLocationId,
                        String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId) {
                return AddAcquisitionEntryMovement.execute(userId,
                                movementCrudRepository, stockRepository,
                                productId, dateTime, reason,
                                comment, quantity, supplierId, toLocationId,
                                transactionSubtype, transactionValue, transactionCurrencyId);
        }

        @Override
        public Mono<MovementEntity> addCustomerReturnEntryMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason,
                        String comment, int quantity, Long toLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId) {
                return AddCustomerReturnEntryMovement.execute(userId,
                                movementCrudRepository, stockRepository,
                                productId, dateTime, reason,
                                comment, quantity, toLocationId,
                                transactionSubtype, transactionValue, transactionCurrencyId);
        }

        @Override
        public Mono<MovementEntity> addInventoryAdjustmentEntryMovement(Long userId,
                        Long productId, LocalDateTime dateTime,
                        String reason, String comment, int quantity, Long toLocationId) {
                return AddInventoryAdjustmentEntryMovement.execute(userId,
                                movementCrudRepository, stockRepository, productId,
                                dateTime, reason, comment, quantity, toLocationId);
        }

        @Override
        public Mono<MovementEntity> addProductionEntryMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason,
                        String comment, int quantity, Long toLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId) {
                return AddProductionEntryMovement.execute(userId,
                                movementCrudRepository, stockRepository,
                                productId, dateTime, reason,
                                comment, quantity, toLocationId,
                                transactionSubtype, transactionValue, transactionCurrencyId);
        }

        @Override
        public Mono<MovementEntity> addSalesOutputMovement(Long userId,
                        Long productId, LocalDateTime dateTime,
                        String reason, String comment, int quantity,
                        Long fromLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId) {
                return AddSalesOutputMovement.execute(userId,
                                movementCrudRepository, stockRepository,
                                productId, dateTime, reason, comment,
                                quantity, fromLocationId, transactionSubtype,
                                transactionValue, transactionCurrencyId);
        }

        @Override
        public Mono<MovementEntity> addSupplierReturnOutputMovement(Long userId,
                        Long productId, LocalDateTime dateTime, String reason,
                        String comment, int quantity, Long supplierId,
                        Long fromLocationId, String transactionSubtype,
                        double transactionValue, Long transactionCurrencyId) {
                return AddSupplierReturnOutputMovement.execute(userId,
                                movementCrudRepository, stockRepository,
                                productId, dateTime, reason, comment, quantity,
                                supplierId, fromLocationId, transactionSubtype,
                                transactionValue, transactionCurrencyId);
        }

        @Override
        public Mono<MovementEntity> addInventoryAdjustmentOutputMovement(Long userId,
                        Long productId, LocalDateTime dateTime,
                        String reason, String comment, int quantity, Long fromLocationId) {
                return AddInventoryAdjustmentOutputMovement.execute(userId,
                                movementCrudRepository, stockRepository, productId,
                                dateTime, reason, comment, quantity, fromLocationId);
        }

        @Override
        public Mono<MovementEntity> addInternalConsumptionOutputMovement(Long userId,
                        Long productId, LocalDateTime dateTime,
                        String reason, String comment, int quantity, Long fromLocationId) {
                return AddInternalConsumptionOutputMovement.execute(userId,
                                movementCrudRepository, stockRepository, productId,
                                dateTime, reason, comment, quantity, fromLocationId);
        }

        @Override
        public Mono<MovementEntity> addTransferMovement(Long userId, Long productId,
                        LocalDateTime dateTime, String reason,
                        String comment, int quantity, Long fromLocationId, Long toLocationId) {
                return AddTransferMovement.execute(userId,
                                movementCrudRepository, stockRepository,
                                productId, dateTime, reason, comment,
                                quantity, fromLocationId, toLocationId);
        }

        @Override
        public Mono<Boolean> cancelMovementById(Long userId, Long movementId) {
                String errMsg = "Failed to cancel movement with ID: " + movementId
                                + ". Quantity in stock cannot be less than 0.";
                return CancelMovementById.execute(userId, movementCrudRepository, stockRepository, movementId)
                                .onErrorMap(error -> {
                                        return new IllegalStateException(errMsg);
                                });
        }

        @Override
        public Flux<StandardMovementDTO> getMovements(Long userId, Long productId) {
                return movementCrudRepository.getMovements(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<StandardMovementDTO> getMovementsOnLocation(Long userId, Long locationId) {
                return movementCrudRepository.getMovementsOnLocation(userId,
                                locationId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<StandardMovementDTO> getSupplierRelatedMovements(Long userId, Long supplierId) {
                return movementCrudRepository.getSupplierRelatedMovements(userId,
                                supplierId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<EntryMovementDTO> getEntries(Long userId, Long productId) {
                return movementCrudRepository.getEntries(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<OutputMovementDTO> getOutputs(Long userId, Long productId) {
                return movementCrudRepository.getOutputs(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<TransferMovementDTO> getTransfers(Long userId, Long productId) {
                return movementCrudRepository.getTransfers(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AcquisitionDTO> getAcquisitions(Long userId, Long productId) {
                return movementCrudRepository.getAcquisitions(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<CustomerReturnDTO> getCustomerReturns(Long userId, Long productId) {
                return movementCrudRepository.getCustomerReturns(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<EntryMovementDTO> getEntryInventoryAdjustments(Long userId, Long productId) {
                return movementCrudRepository.getEntryInventoryAdjustments(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<ProductionDTO> getProductions(Long userId, Long productId) {
                return movementCrudRepository.getProductions(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<SaleDTO> getSales(Long userId, Long productId) {
                return movementCrudRepository.getSales(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<SupplierReturnDTO> getSupplierReturns(Long userId, Long productId) {
                return movementCrudRepository.getSupplierReturns(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<OutputMovementDTO> getOutputInventoryAdjustments(Long userId, Long productId) {
                return movementCrudRepository.getOutputInventoryAdjustments(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<OutputMovementDTO> getInternalConsumptionMovements(Long userId, Long productId) {
                return movementCrudRepository.getInternalConsumptionMovements(userId,
                                productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AcquisitionDTO> getAcquisitionsBySupplierId(Long userId, Long supplierId) {
                return movementCrudRepository.getAcquisitionsBySupplierId(userId, supplierId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(Long userId, String costType,
                        Long currencyId, double minCost, double maxCost, int fromYear, int toYear) {
                return movementCrudRepository.findAcquisitionsByCostAndYear(userId, costType,
                                currencyId, minCost, maxCost,
                                fromYear, toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(Long userId,
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgUnitCostByAcquisition(userId, productId,
                                currencyId, fromYear,
                                toYear)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No AVG acquisition cost found")));
        }

        @Override
        public Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(Long userId,
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgTotalCostByAcquisition(userId, productId,
                                currencyId, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

        @Override
        public Flux<ProductionDTO> findProductionByCostAndYear(Long userId, String costType,
                        Long currencyId, double minCost, double maxCost, int fromYear, int toYear) {
                return movementCrudRepository.findProductionByCostAndYear(userId, costType,
                                currencyId, minCost, maxCost,
                                fromYear, toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AverageCostProductDTO> getAvgUnitProductionCost(Long userId,
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgUnitProductionCost(userId, productId,
                                currencyId, fromYear, toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

        @Override
        public Flux<AverageCostProductDTO> getAvgTotalProductionCost(Long userId,
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgTotalProductionCost(userId, productId,
                                currencyId, fromYear, toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

        @Override
        public Flux<SaleDTO> findSalesByValueAndYear(Long userId, String sellType,
                        Long currencyId, double minValue, double maxValue, int fromYear, int toYear) {
                return movementCrudRepository.findSalesByValueAndYear(userId, sellType,
                                currencyId, minValue, maxValue, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AverageSellProductDTO> getAvgUnitSellValue(Long userId,
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgUnitSellValue(userId, productId,
                                currencyId, fromYear, toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

        @Override
        public Flux<AverageSellProductDTO> getAvgTotalSellValue(Long userId,
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgTotalSellValue(userId, productId,
                                currencyId, fromYear, toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

}
