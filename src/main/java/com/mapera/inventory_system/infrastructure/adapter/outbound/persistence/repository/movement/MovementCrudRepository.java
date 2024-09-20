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

public interface MovementCrudRepository extends ReactiveCrudRepository<MovementEntity, Long> {

        @Query(MovementQuery.ALL_QUERY)
        Flux<StandardMovementDTO> getMovements(Long userId);

        @Query(MovementQuery.ENTRY_QUERY)
        Flux<EntryMovementDTO> getEntries(Long userId);

        @Query(MovementQuery.OUTPUT_QUERY)
        Flux<OutputMovementDTO> getOutputs(Long userId);

        @Query(MovementQuery.TRANSFER_QUERY)
        Flux<TransferMovementDTO> getTransfers(Long userId);

        @Query(MovementQuery.ACQUISITION_QUERY)
        Flux<AcquisitionDTO> getAcquisitions(Long userId);

        @Query(MovementQuery.CUSTOMER_RETURN_QUERY)
        Flux<CustomerReturnDTO> getCustomerReturns(Long userId);

        @Query(MovementQuery.ENTRY_ADJUSMENT_QUERY)
        Flux<EntryMovementDTO> getEntryInventoryAdjustments(Long userId);

        @Query(MovementQuery.PRODUCTION_QUERY)
        Flux<ProductionDTO> getProductions(Long userId);

        @Query(MovementQuery.SALES_QUERY)
        Flux<SaleDTO> getSales(Long userId);

        @Query(MovementQuery.SUPPLIER_RETURN_QUERY)
        Flux<SupplierReturnDTO> getSupplierReturns(Long userId);

        @Query(MovementQuery.OUTPUT_ADJUSMENT_QUERY)
        Flux<OutputMovementDTO> getOutputInventoryAdjustments(Long userId);

        @Query(MovementQuery.INTERNAL_CONSUMPTION_QUERY)
        Flux<OutputMovementDTO> getInternalConsumptionMovements(Long userId);

        @Query(MovementQuery.PRODUCT_QUERY)
        Flux<StandardMovementDTO> getMovementsByProductId(Long userId, Long productId);

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
