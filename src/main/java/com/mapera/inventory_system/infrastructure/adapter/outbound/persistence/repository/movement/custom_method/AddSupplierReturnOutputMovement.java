package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method;

import java.time.LocalDateTime;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;

import reactor.core.publisher.Mono;

public class AddSupplierReturnOutputMovement {
    public static Mono<MovementEntity> execute(
            MovementCrudRepository movementRepository, StockRepository stockRepository,
            long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, long supplierId,
            long fromLocationId, String transactionSubtype,
            double transactionValue, long transactionCurrencyId) {

        MovementEntity movement = new MovementEntity();
        movement.setProduct_id(productId);
        movement.setDate_time(dateTime);
        movement.setType("Output");
        movement.setSubtype("Supplier Return");
        movement.setReason(reason);
        movement.setComment(comment);
        movement.setSupplier_id(supplierId);
        movement.setQuantity(quantity);
        movement.setFrom_location_id(fromLocationId);
        movement.setTransaction_type("Return");
        movement.setTransaction_subtype(transactionSubtype);
        movement.setTransaction_value(transactionValue);
        movement.setTransaction_currency_id(transactionCurrencyId);
        return stockRepository.decreseStockByLocationAndProduct(
                fromLocationId, productId, quantity)
                .then(movementRepository.save(movement));
    }
}
