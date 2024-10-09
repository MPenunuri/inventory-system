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

        Flux<StandardMovementDTO> getMovements(Long userId, Long productId);

        Flux<StandardMovementDTO> getMovementsOnLocation(Long userId, Long locationId);

        Flux<EntryMovementDTO> getEntries(Long userId, Long productId);

        Flux<OutputMovementDTO> getOutputs(Long userId, Long productId);

        Flux<TransferMovementDTO> getTransfers(Long userId, Long productId);

        Flux<AcquisitionDTO> getAcquisitions(Long userId, Long productId);

        Flux<CustomerReturnDTO> getCustomerReturns(Long userId, Long productId);

        Flux<EntryMovementDTO> getEntryInventoryAdjustments(Long userId, Long productId);

        Flux<ProductionDTO> getProductions(Long userId, Long productId);

        Flux<SaleDTO> getSales(Long userId, Long productId);

        Flux<SupplierReturnDTO> getSupplierReturns(Long userId, Long productId);

        Flux<OutputMovementDTO> getOutputInventoryAdjustments(Long userId, Long productId);

        Flux<OutputMovementDTO> getInternalConsumptionMovements(Long userId, Long productId);

        Flux<AcquisitionDTO> getAcquisitionsBySupplierId(Long userId, Long supplierId);

        Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(Long userId, String costType,
                        Long currencyId, double minCost, double maxCost,
                        int fromYear, int toYear);

        Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(Long userId,
                        Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(Long userId,
                        Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<ProductionDTO> findProductionByCostAndYear(Long userId, String costType,
                        Long currencyId, double minCost, double maxCost,
                        int fromYear, int toYear);

        Flux<AverageCostProductDTO> getAvgUnitProductionCost(Long userId,
                        Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<AverageCostProductDTO> getAvgTotalProductionCost(Long userId,
                        Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<SaleDTO> findSalesByValueAndYear(Long userId,
                        String sellType, Long currencyId, double minValue, double maxValue,
                        int fromYear, int toYear);

        Flux<AverageSellProductDTO> getAvgUnitSellValue(Long userId,
                        Long productId, Long currencyId,
                        int fromYear, int toYear);

        Flux<AverageSellProductDTO> getAvgTotalSellValue(Long userId,
                        Long productId, Long currencyId,
                        int fromYear, int toYear);

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
