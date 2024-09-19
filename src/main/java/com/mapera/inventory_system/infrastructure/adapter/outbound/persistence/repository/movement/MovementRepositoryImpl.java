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
                String errMsg = "Failed to cancel movement with ID: " + movementId
                                + ". Quantity in stock cannot be less than 0.";
                return CancelMovementById.execute(movementCrudRepository, stockRepository, movementId)
                                .onErrorMap(error -> {
                                        return new IllegalStateException(errMsg);
                                });
        }

        @Override
        public Flux<StandardMovementDTO> getMovements() {
                return movementCrudRepository.getMovements()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<EntryMovementDTO> getEntries() {
                return movementCrudRepository.getEntries()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<OutputMovementDTO> getOutputs() {
                return movementCrudRepository.getOutputs()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<TransferMovementDTO> getTransfers() {
                return movementCrudRepository.getTransfers()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AcquisitionDTO> getAcquisitions() {
                return movementCrudRepository.getAcquisitions()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<CustomerReturnDTO> getCustomerReturns() {
                return movementCrudRepository.getCustomerReturns()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<EntryMovementDTO> getEntryInventoryAdjustments() {
                return movementCrudRepository.getEntryInventoryAdjustments()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<ProductionDTO> getProductions() {
                return movementCrudRepository.getProductions()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<SaleDTO> getSales() {
                return movementCrudRepository.getSales()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<SupplierReturnDTO> getSupplierReturns() {
                return movementCrudRepository.getSupplierReturns()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<OutputMovementDTO> getOutputInventoryAdjustments() {
                return movementCrudRepository.getOutputInventoryAdjustments()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<OutputMovementDTO> getInternalConsumptionMovements() {
                return movementCrudRepository.getInternalConsumptionMovements()
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<StandardMovementDTO> getMovementsByProductId(Long productId) {
                return movementCrudRepository.getMovementsByProductId(productId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AcquisitionDTO> getAcquisitionsBySupplierId(Long supplierId) {
                return movementCrudRepository.getAcquisitionsBySupplierId(supplierId)
                                .switchIfEmpty(Mono.error(
                                                new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(String costType,
                        Long currencyId, double minCost, double maxCost, int fromYear, int toYear) {
                return movementCrudRepository.findAcquisitionsByCostAndYear(costType,
                                currencyId, minCost, maxCost,
                                fromYear, toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgUnitCostByAcquisition(productId, currencyId, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG acquisition cost found")));
        }

        @Override
        public Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgTotalCostByAcquisition(productId, currencyId, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

        @Override
        public Flux<ProductionDTO> findProductionByCostAndYear(String costType,
                        Long currencyId, double minCost, double maxCost, int fromYear, int toYear) {
                return movementCrudRepository.findProductionByCostAndYear(costType,
                                currencyId, minCost, maxCost,
                                fromYear, toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AverageCostProductDTO> getAvgUnitProductionCost(
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgUnitProductionCost(productId, currencyId, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

        @Override
        public Flux<AverageCostProductDTO> getAvgTotalProductionCost(
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgTotalProductionCost(productId, currencyId, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

        @Override
        public Flux<SaleDTO> findSalesByValueAndYear(String sellType,
                        Long currencyId, double minValue, double maxValue, int fromYear, int toYear) {
                return movementCrudRepository.findSalesByValueAndYear(sellType,
                                currencyId, minValue, maxValue, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No movements found")));
        }

        @Override
        public Flux<AverageSellProductDTO> getAvgUnitSellValue(
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgUnitSellValue(productId, currencyId, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

        @Override
        public Flux<AverageSellProductDTO> getAvgTotalSellValue(
                        Long productId, Long currencyId, int fromYear, int toYear) {
                return movementCrudRepository.getAvgTotalSellValue(productId, currencyId, fromYear,
                                toYear).switchIfEmpty(
                                                Mono.error(new RuntimeException("No AVG found")));
        }

}
