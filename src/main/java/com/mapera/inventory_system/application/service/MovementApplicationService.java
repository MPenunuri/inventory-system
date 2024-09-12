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

    public Flux<StandardMovementDTO> getMovements() {
        return movementPersistencePort.getMovements();
    }

    public Flux<EntryMovementDTO> getEntries() {
        return movementPersistencePort.getEntries();
    }

    public Flux<OutputMovementDTO> getOutputs() {
        return movementPersistencePort.getOutputs();
    }

    public Flux<TransferMovementDTO> getTransfers() {
        return movementPersistencePort.getTransfers();
    }

    public Flux<AcquisitionDTO> getAcquisitions() {
        return movementPersistencePort.getAcquisitions();
    }

    public Flux<CustomerReturnDTO> getCustomerReturns() {
        return movementPersistencePort.getCustomerReturns();
    }

    public Flux<EntryMovementDTO> getEntryInventoryAdjustments() {
        return movementPersistencePort.getEntryInventoryAdjustments();
    }

    public Flux<ProductionDTO> getProductions() {
        return movementPersistencePort.getProductions();
    }

    public Flux<SaleDTO> getSales() {
        return movementPersistencePort.getSales();
    }

    public Flux<SupplierReturnDTO> getSupplierReturns() {
        return movementPersistencePort.getSupplierReturns();
    }

    public Flux<OutputMovementDTO> getOutputInventoryAdjustments() {
        return movementPersistencePort.getOutputInventoryAdjustments();
    }

    public Flux<OutputMovementDTO> getInternalConsumptionMovements() {
        return movementPersistencePort.getInternalConsumptionMovements();
    }

    public Flux<StandardMovementDTO> getMovementsByProductId(Long productId) {
        return movementPersistencePort.getMovementsByProductId(productId);
    }

    public Flux<AcquisitionDTO> getAcquisitionsBySupplierId(Long supplierId) {
        return movementPersistencePort.getAcquisitionsBySupplierId(supplierId);
    }

    public Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(
            Long currencyId, double minCost, double maxCost, int fromYear, int toYear) {
        return movementPersistencePort.findAcquisitionsByCostAndYear(
                currencyId, minCost, maxCost, fromYear, toYear);
    }

    public Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(
            Long productId,
            Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgUnitCostByAcquisition(
                productId, currencyId, fromYear, toYear);
    }

    public Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(
            Long productId,
            Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgTotalCostByAcquisition(
                productId, currencyId, fromYear, toYear);
    }

    public Flux<ProductionDTO> findProductionByCostAndYear(
            Long currencyId, double minCost, double maxCost,
            int fromYear, int toYear) {
        return movementPersistencePort.findProductionByCostAndYear(
                currencyId, minCost, maxCost, fromYear, toYear);
    }

    public Flux<AverageCostProductDTO> getAvgUnitProductionCost(
            Long productId, Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgUnitProductionCost(
                productId, currencyId, fromYear, toYear);
    }

    public Flux<AverageCostProductDTO> getAvgTotalProductionCost(
            Long productId, Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgTotalProductionCost(
                productId, currencyId, fromYear, toYear);
    }

    public Flux<SaleDTO> findSalesByValueAndYear(
            Long currencyId, double minValue, double maxValue, int fromYear, int toYear) {
        return movementPersistencePort.findSalesByValueAndYear(
                currencyId, minValue, maxValue, fromYear, toYear);
    }

    public Flux<AverageSellProductDTO> getAvgUnitSellValue(
            Long productId, Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgUnitSellValue(
                productId, currencyId, fromYear, toYear);
    }

    public Flux<AverageSellProductDTO> getAvgTotalSellValue(
            Long productId, Long currencyId, int fromYear, int toYear) {
        return movementPersistencePort.getAvgTotalSellValue(
                productId, currencyId, fromYear, toYear);
    }

    public Mono<MovementEntity> addAcquisitionEntryMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long supplierId, Long toLocationId,
            String transactionSubtype, double transactionValue, Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(toLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(transactionCurrencyId);
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
                productId, dateTime, reason, comment, quantity,
                supplierId, toLocationId, transactionSubtype, transactionValue,
                transactionCurrencyId));
    }

    public Mono<MovementEntity> addCustomerReturnEntryMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long toLocationId,
            String transactionSubtype, double transactionValue, Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(toLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(transactionCurrencyId);
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
                productId, dateTime, reason, comment, quantity,
                toLocationId, transactionSubtype, transactionValue,
                transactionCurrencyId));
    }

    public Mono<MovementEntity> addInventoryAdjustmentEntryMovement(
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity, Long toLocationId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(toLocationId);
        Mono<Void> checkDomainLogic = findProduct.flatMap(product -> {
            return findLocation.map(location -> {
                InventoryEntryMovement movement = new InventoryEntryMovement(
                        0, product, dateTime, EntryType.INVENTORY_ADJUSTMENT,
                        reason, quantity, location, new Cost(0, null, null));
                return Mono.just(movement.execute());
            });
        }).then();
        return checkDomainLogic.then(movementPersistencePort.addInventoryAdjustmentEntryMovement(
                productId, dateTime, reason, comment,
                quantity, toLocationId));
    }

    public Mono<MovementEntity> addProductionEntryMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long toLocationId,
            String transactionSubtype, double transactionValue,
            Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(toLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(transactionCurrencyId);
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
                productId, dateTime, reason, comment, quantity,
                toLocationId, transactionSubtype, transactionValue,
                transactionCurrencyId));
    }

    public Mono<MovementEntity> addSalesOutputMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long fromLocationId,
            String transactionSubtype, double transactionValue,
            Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(fromLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(transactionCurrencyId);
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
                productId, dateTime, reason, comment, quantity,
                fromLocationId, transactionSubtype, transactionValue,
                transactionCurrencyId));
    }

    public Mono<MovementEntity> addSupplierReturnOutputMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long supplierId,
            Long fromLocationId, String transactionSubtype,
            double transactionValue, Long transactionCurrencyId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(fromLocationId);
        Mono<String> getCurrencyName = currencyPersistencePort.getCurrencyNameById(transactionCurrencyId);
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
                productId, dateTime, reason, comment, quantity,
                supplierId, fromLocationId, transactionSubtype,
                transactionValue, transactionCurrencyId));
    }

    public Mono<MovementEntity> addInventoryAdjustmentOutputMovement(
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity,
            Long fromLocationId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(fromLocationId);
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
                productId, dateTime, reason, comment,
                quantity, fromLocationId));
    }

    public Mono<MovementEntity> addInternalConsumptionOutputMovement(
            Long productId, LocalDateTime dateTime,
            String reason, String comment, int quantity, Long fromLocationId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findLocation = locationPersistencePort.findLocationById(fromLocationId);
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
                productId, dateTime, reason, comment,
                quantity, fromLocationId));
    }

    public Mono<MovementEntity> addTransferMovement(
            Long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, Long fromLocationId, Long toLocationId) {
        Mono<InventoryProduct> findProduct = productPersistencePort.findProductById(productId);
        Mono<Location> findFromLocation = locationPersistencePort.findLocationById(fromLocationId);
        Mono<Location> findToLocation = locationPersistencePort.findLocationById(toLocationId);
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
        return checkDomainLogic.then(movementPersistencePort.addTransferMovement(
                productId, dateTime, reason, comment, quantity,
                fromLocationId, toLocationId));
    }

    public Mono<Boolean> cancelMovementById(Long movementId) {
        return movementPersistencePort.cancelMovementById(movementId);
    }

}
