package com.mapera.inventory_system.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.CurrencyPersistencePort;
import com.mapera.inventory_system.application.port.outbound.LocationPersistencePort;
import com.mapera.inventory_system.application.port.outbound.MovementPersistencePort;
import com.mapera.inventory_system.application.port.outbound.ProductPersistencePort;
import com.mapera.inventory_system.domain.aggregate.inventory_movement.InventoryEntryMovement;
import com.mapera.inventory_system.domain.aggregate.inventory_movement.InventoryOutputMovement;
import com.mapera.inventory_system.domain.aggregate.inventory_movement.InventoryTransferMovement;
import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.valueobject.Cost;
import com.mapera.inventory_system.domain.valueobject.CostType;
import com.mapera.inventory_system.domain.valueobject.Sell;
import com.mapera.inventory_system.domain.valueobject.SellType;
import com.mapera.inventory_system.domain.valueobject.TransferType;
import com.mapera.inventory_system.domain.valueobject.EntryType;
import com.mapera.inventory_system.domain.valueobject.OutputType;
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

@Service
public class MovementApplicationService {

    private final ProductPersistencePort productPersistencePort;
    private final MovementPersistencePort movementPersistencePort;
    private final LocationPersistencePort locationPersistencePort;
    private final CurrencyPersistencePort currencyPersistencePort;

    public MovementApplicationService(
            ProductPersistencePort productPersistencePort,
            MovementPersistencePort movementPersistencePort,
            LocationPersistencePort locationPersistencePort,
            CurrencyPersistencePort currencyPersistencePort) {
        this.productPersistencePort = productPersistencePort;
        this.movementPersistencePort = movementPersistencePort;
        this.locationPersistencePort = locationPersistencePort;
        this.currencyPersistencePort = currencyPersistencePort;
    }

    public Flux<StandardMovementDTO> getMovements(Long userId, Long productId) {
        return movementPersistencePort.getMovements(userId, productId);
    }

    public Flux<EntryMovementDTO> getEntries(Long userId, Long productId) {
        return movementPersistencePort.getEntries(userId, productId);
    }

    public Flux<OutputMovementDTO> getOutputs(Long userId, Long productId) {
        return movementPersistencePort.getOutputs(userId, productId);
    }

    public Flux<TransferMovementDTO> getTransfers(Long userId, Long productId) {
        return movementPersistencePort.getTransfers(userId, productId);
    }

    public Flux<AcquisitionDTO> getAcquisitions(Long userId, Long productId) {
        return movementPersistencePort.getAcquisitions(userId, productId);
    }

    public Flux<CustomerReturnDTO> getCustomerReturns(Long userId, Long productId) {
        return movementPersistencePort.getCustomerReturns(userId, productId);
    }

    public Flux<EntryMovementDTO> getEntryInventoryAdjustments(Long userId, Long productId) {
        return movementPersistencePort.getEntryInventoryAdjustments(userId, productId);
    }

    public Flux<ProductionDTO> getProductions(Long userId, Long productId) {
        return movementPersistencePort.getProductions(userId, productId);
    }

    public Flux<SaleDTO> getSales(Long userId, Long productId) {
        return movementPersistencePort.getSales(userId, productId);
    }

    public Flux<SupplierReturnDTO> getSupplierReturns(Long userId, Long productId) {
        return movementPersistencePort.getSupplierReturns(userId, productId);
    }

    public Flux<OutputMovementDTO> getOutputInventoryAdjustments(Long userId, Long productId) {
        return movementPersistencePort.getOutputInventoryAdjustments(userId, productId);
    }

    public Flux<OutputMovementDTO> getInternalConsumptionMovements(Long userId, Long productId) {
        return movementPersistencePort.getInternalConsumptionMovements(userId, productId);
    }

    public Flux<AcquisitionDTO> getAcquisitionsBySupplierId(Long userId, Long supplierId) {
        return movementPersistencePort.getAcquisitionsBySupplierId(userId, supplierId);
    }

    public Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(Long userId, String costType,
            Long currencyId, double minCost, double maxCost, int fromYear, int toYear) {
        return movementPersistencePort.findAcquisitionsByCostAndYear(userId, costType,
                currencyId, minCost, maxCost, fromYear, toYear);
    }

    public Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(
            Long userId, Long productId,
            Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgUnitCostByAcquisition(
                userId, productId, currencyId, fromYear, toYear);
    }

    public Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(
            Long userId, Long productId,
            Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgTotalCostByAcquisition(
                userId, productId, currencyId, fromYear, toYear);
    }

    public Flux<ProductionDTO> findProductionByCostAndYear(
            Long userId, String costType,
            Long currencyId, double minCost, double maxCost,
            int fromYear, int toYear) {
        return movementPersistencePort.findProductionByCostAndYear(
                userId, costType, currencyId, minCost,
                maxCost, fromYear, toYear);
    }

    public Flux<AverageCostProductDTO> getAvgUnitProductionCost(Long userId,
            Long productId, Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgUnitProductionCost(
                userId, productId, currencyId, fromYear, toYear);
    }

    public Flux<AverageCostProductDTO> getAvgTotalProductionCost(Long userId,
            Long productId, Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgTotalProductionCost(
                userId, productId, currencyId, fromYear, toYear);
    }

    public Flux<SaleDTO> findSalesByValueAndYear(
            Long userId, String sellType,
            Long currencyId, double minValue, double maxValue,
            int fromYear, int toYear) {
        return movementPersistencePort.findSalesByValueAndYear(userId,
                sellType, currencyId, minValue, maxValue, fromYear, toYear);
    }

    public Flux<AverageSellProductDTO> getAvgUnitSellValue(Long userId,
            Long productId, Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgUnitSellValue(
                userId, productId, currencyId, fromYear, toYear);
    }

    public Flux<AverageSellProductDTO> getAvgTotalSellValue(Long userId,
            Long productId, Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgTotalSellValue(
                userId, productId, currencyId, fromYear, toYear);
    }

    public Mono<MovementEntity> addAcquisitionEntryMovement(Long userId,
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long supplierId, Long toLocationId,
            String transactionSubtype, double transactionValue, Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(userId, toLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(userId, transactionCurrencyId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return getCurrencyName.flatMap(currency -> {
                return findLocation.map(location -> {
                    InventoryEntryMovement movement = new InventoryEntryMovement(
                            0, product, dateTime, EntryType.ACQUISITION, reason,
                            quantity, location, new Cost(transactionValue, currency,
                                    CostType.fromString(transactionSubtype)));
                    return Mono.just(movement.execute());
                });
            }).then();
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addAcquisitionEntryMovement(
                userId,
                productId, dateTime, reason, comment, quantity,
                supplierId, toLocationId, transactionSubtype, transactionValue,
                transactionCurrencyId));
    }

    public Mono<MovementEntity> addCustomerReturnEntryMovement(Long userId,
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long toLocationId,
            String transactionSubtype, double transactionValue, Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(userId, toLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(userId, transactionCurrencyId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return getCurrencyName.flatMap(currency -> {
                return findLocation.map(location -> {
                    InventoryEntryMovement movement = new InventoryEntryMovement(
                            0, product, dateTime, EntryType.CUSTOMER_RETURN,
                            reason, quantity, location,
                            new Cost(transactionValue, currency,
                                    CostType.fromString(transactionSubtype)));
                    return Mono.just(movement.execute());
                });
            }).then();
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addCustomerReturnEntryMovement(
                userId,
                productId, dateTime, reason, comment, quantity,
                toLocationId, transactionSubtype, transactionValue,
                transactionCurrencyId));
    }

    public Mono<MovementEntity> addInventoryAdjustmentEntryMovement(Long userId,
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity, Long toLocationId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(userId, toLocationId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return findLocation.map(location -> {
                InventoryEntryMovement movement = new InventoryEntryMovement(
                        0, product, dateTime, EntryType.INVENTORY_ADJUSTMENT,
                        reason, quantity, location, new Cost(0, null, null));
                return Mono.just(movement.execute());
            });
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addInventoryAdjustmentEntryMovement(
                userId,
                productId, dateTime, reason, comment,
                quantity, toLocationId));
    }

    public Mono<MovementEntity> addProductionEntryMovement(Long userId,
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long toLocationId,
            String transactionSubtype, double transactionValue,
            Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(userId, toLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(userId, transactionCurrencyId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return getCurrencyName.flatMap(currency -> {
                return findLocation.map(location -> {
                    InventoryEntryMovement movement = new InventoryEntryMovement(
                            0, product, dateTime, EntryType.PRODUCTION,
                            reason, quantity, location,
                            new Cost(transactionValue, currency,
                                    CostType.fromString(transactionSubtype)));
                    return Mono.just(movement.execute());
                });
            }).then();
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addProductionEntryMovement(
                userId,
                productId, dateTime, reason, comment, quantity,
                toLocationId, transactionSubtype, transactionValue,
                transactionCurrencyId));
    }

    public Mono<MovementEntity> addSalesOutputMovement(Long userId,
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long fromLocationId,
            String transactionSubtype, double transactionValue,
            Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(userId, fromLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(userId, transactionCurrencyId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return getCurrencyName.flatMap(currency -> {
                return findLocation.map(location -> {
                    InventoryOutputMovement movement = new InventoryOutputMovement(
                            0, product, dateTime, OutputType.SALES,
                            reason, quantity, location,
                            new Sell(transactionValue, currency,
                                    SellType.fromString(transactionSubtype)));
                    return Mono.just(movement.execute());
                });
            }).then();
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addSalesOutputMovement(
                userId,
                productId, dateTime, reason, comment, quantity,
                fromLocationId, transactionSubtype, transactionValue,
                transactionCurrencyId));
    }

    public Mono<MovementEntity> addSupplierReturnOutputMovement(Long userId,
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long supplierId,
            Long fromLocationId, String transactionSubtype,
            double transactionValue, Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(userId, fromLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(userId, transactionCurrencyId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return getCurrencyName.flatMap(currency -> {
                return findLocation.map(location -> {
                    InventoryOutputMovement movement = new InventoryOutputMovement(
                            0, product, dateTime, OutputType.SUPPLIER_RETURN,
                            reason, quantity, location,
                            new Sell(transactionValue, currency,
                                    SellType.fromString(transactionSubtype)));
                    return Mono.just(movement.execute());
                });
            }).then();
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addSupplierReturnOutputMovement(
                userId,
                productId, dateTime, reason, comment, quantity,
                supplierId, fromLocationId, transactionSubtype,
                transactionValue, transactionCurrencyId));
    }

    public Mono<MovementEntity> addInventoryAdjustmentOutputMovement(Long userId,
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity,
            Long fromLocationId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(userId, fromLocationId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return findLocation.map(location -> {
                InventoryOutputMovement movement = new InventoryOutputMovement(
                        0, product, dateTime, OutputType.INVENTORY_ADJUSTMENT,
                        reason, quantity, location,
                        new Sell(0, null, null));
                return Mono.just(movement.execute());
            });
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addInventoryAdjustmentOutputMovement(
                userId,
                productId, dateTime, reason, comment,
                quantity, fromLocationId));
    }

    public Mono<MovementEntity> addInternalConsumptionOutputMovement(Long userId,
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity, Long fromLocationId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(userId, fromLocationId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return findLocation.map(location -> {
                InventoryOutputMovement movement = new InventoryOutputMovement(
                        0, product, dateTime, OutputType.INTERNAL_CONSUMPTION,
                        reason, quantity, location,
                        new Sell(0, null, null));
                return Mono.just(movement.execute());
            });
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addInternalConsumptionOutputMovement(
                userId,
                productId, dateTime, reason, comment,
                quantity, fromLocationId));
    }

    public Mono<MovementEntity> addTransferMovement(Long userId,
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long fromLocationId, Long toLocationId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(userId, productId);
        Mono<Location> findFromLocation = locationPersistencePort.findLocationById(userId, fromLocationId);
        Mono<Location> findToLocation = locationPersistencePort.findLocationById(userId, toLocationId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return findFromLocation.flatMap(fromLocation -> {
                return findToLocation.map(toLocation -> {
                    InventoryTransferMovement movement = new InventoryTransferMovement(
                            quantity, product, dateTime, TransferType.NONE,
                            reason, quantity, fromLocation, toLocation);
                    return Mono.just(movement.execute());
                });
            }).then();
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addTransferMovement(userId,
                productId, dateTime, reason, comment, quantity,
                fromLocationId, toLocationId));
    }

    public Mono<Boolean> cancelMovementById(Long userId, Long movementId) {
        return movementPersistencePort.cancelMovementById(userId, movementId);
    }

}
