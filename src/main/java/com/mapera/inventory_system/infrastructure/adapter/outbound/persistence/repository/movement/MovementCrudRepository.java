package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

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

public interface MovementCrudRepository extends ReactiveCrudRepository<MovementEntity, Long> {

        @Query("SELECT COUNT(*) FROM movements m WHERE m.user_id = :userId ")
        Mono<Integer> countByUserId(Long userId);

        @Query("DELETE FROM movements m WHERE m.user_id = :userId ")
        Mono<Void> deleteByUserId(Long userId);

        @Query(MovementQuery.ALL_QUERY)
        Flux<StandardMovementDTO> getMovements(Long userId, Long productId);

        @Query(MovementQuery.ENTRY_QUERY + " AND p.id = :productId")
        Flux<EntryMovementDTO> getEntries(Long userId, Long productId);

        @Query(MovementQuery.OUTPUT_QUERY + " AND p.id = :productId")
        Flux<OutputMovementDTO> getOutputs(Long userId, Long productId);

        @Query(MovementQuery.TRANSFER_QUERY + " AND p.id = :productId")
        Flux<TransferMovementDTO> getTransfers(Long userId, Long productId);

        @Query(MovementQuery.ACQUISITION_QUERY + " AND p.id = :productId")
        Flux<AcquisitionDTO> getAcquisitions(Long userId, Long productId);

        @Query(MovementQuery.CUSTOMER_RETURN_QUERY + " AND p.id = :productId")
        Flux<CustomerReturnDTO> getCustomerReturns(Long userId, Long productId);

        @Query(MovementQuery.ENTRY_ADJUSMENT_QUERY + " AND p.id = :productId")
        Flux<EntryMovementDTO> getEntryInventoryAdjustments(Long userId, Long productId);

        @Query(MovementQuery.PRODUCTION_QUERY + " AND p.id = :productId")
        Flux<ProductionDTO> getProductions(Long userId, Long productId);

        @Query(MovementQuery.SALES_QUERY + " AND p.id = :productId")
        Flux<SaleDTO> getSales(Long userId, Long productId);

        @Query(MovementQuery.SUPPLIER_RETURN_QUERY + " AND p.id = :productId")
        Flux<SupplierReturnDTO> getSupplierReturns(Long userId, Long productId);

        @Query(MovementQuery.OUTPUT_ADJUSMENT_QUERY + " AND p.id = :productId")
        Flux<OutputMovementDTO> getOutputInventoryAdjustments(Long userId, Long productId);

        @Query(MovementQuery.INTERNAL_CONSUMPTION_QUERY + " AND p.id = :productId")
        Flux<OutputMovementDTO> getInternalConsumptionMovements(Long userId, Long productId);

        @Query(MovementQuery.SUPPLIER_QUERY)
        Flux<AcquisitionDTO> getAcquisitionsBySupplierId(Long userId, Long supplierId);

        @Query(MovementQuery.ACQUISITION_COST_QUERY)
        Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(Long userId,
                        String costType, Long currencyId, double minCost,
                        double maxCost, int fromYear, int toYear);

        @Query(MovementQuery.AVERAGE_COST_PERUNIT_ACQUISITION)
        Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(
                        Long userId, Long productId, Long currencyId,
                        int fromYear, int toYear);

        @Query(MovementQuery.AVERAGE_COST_OVERALL_ACQUISITION)
        Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(
                        Long userId, Long productId, Long currencyId,
                        int fromYear, int toYear);

        @Query(MovementQuery.PRODUCTION_COST_QUERY)
        Flux<ProductionDTO> findProductionByCostAndYear(
                        Long userId, String costType,
                        Long currencyId, double minCost, double maxCost,
                        int fromYear, int toYear);

        @Query(MovementQuery.AVERAGE_COST_PERUNIT_PRODUCTION)
        Flux<AverageCostProductDTO> getAvgUnitProductionCost(
                        Long userId, Long productId, Long currencyId,
                        int fromYear, int toYear);

        @Query(MovementQuery.AVERAGE_COST_OVERALL_PRODUCTION)
        Flux<AverageCostProductDTO> getAvgTotalProductionCost(
                        Long userId, Long productId, Long currencyId,
                        int fromYear, int toYear);

        @Query(MovementQuery.SALES_VALUE_QUERY)
        Flux<SaleDTO> findSalesByValueAndYear(Long userId,
                        String sellType, Long currencyId,
                        double minValue, double maxValue,
                        int fromYear, int toYear);

        @Query(MovementQuery.AVERAGE_SELL_RETAIL)
        Flux<AverageSellProductDTO> getAvgUnitSellValue(
                        Long userId, Long productId, Long currencyId,
                        int fromYear, int toYear);

        @Query(MovementQuery.AVERAGE_SELL_WHOLESALE)
        Flux<AverageSellProductDTO> getAvgTotalSellValue(
                        Long userId, Long productId, Long currencyId,
                        int fromYear, int toYear);
}
