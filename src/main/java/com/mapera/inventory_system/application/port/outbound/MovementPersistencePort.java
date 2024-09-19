package com.mapera.inventory_system.application.port.outbound;

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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface MovementPersistencePort {

        Flux<StandardMovementDTO> getMovements();

        Flux<EntryMovementDTO> getEntries();

        Flux<OutputMovementDTO> getOutputs();

        Flux<TransferMovementDTO> getTransfers();

        Flux<AcquisitionDTO> getAcquisitions();

        Flux<CustomerReturnDTO> getCustomerReturns();

        Flux<EntryMovementDTO> getEntryInventoryAdjustments();

        Flux<ProductionDTO> getProductions();

        Flux<SaleDTO> getSales();

        Flux<SupplierReturnDTO> getSupplierReturns();

        Flux<OutputMovementDTO> getOutputInventoryAdjustments();

        Flux<OutputMovementDTO> getInternalConsumptionMovements();

        Flux<StandardMovementDTO> getMovementsByProductId(Long productId);

        Flux<AcquisitionDTO> getAcquisitionsBySupplierId(Long supplierId);

        Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(String costType,
                        Long currencyId, double minCost, double maxCost,
                        int fromYear, int toYear);

        Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<ProductionDTO> findProductionByCostAndYear(String costType,
                        Long currencyId, double minCost, double maxCost,
                        int fromYear, int toYear);

        Flux<AverageCostProductDTO> getAvgUnitProductionCost(Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<AverageCostProductDTO> getAvgTotalProductionCost(Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<SaleDTO> findSalesByValueAndYear(String sellType, Long currencyId, double minValue, double maxValue,
                        int fromYear, int toYear);

        Flux<AverageSellProductDTO> getAvgUnitSellValue(Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<AverageSellProductDTO> getAvgTotalSellValue(Long productId, Long currencyId,
                        int fromYear, int toYear);

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
