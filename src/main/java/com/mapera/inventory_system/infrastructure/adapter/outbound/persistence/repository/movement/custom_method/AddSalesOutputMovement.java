package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method;

import java.time.LocalDateTime;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;

import reactor.core.publisher.Mono;

public class AddSalesOutputMovement {
    public static Mono<MovementEntity> execute(
            MovementCrudRepository movementRepository, StockRepository stockRepository,
            long productId, LocalDateTime dateTime, String reason, String comment,
            int quantity, long fromLocationId, String transactionSubtype,
            double transactionValue, long transactionCurrencyId) {

        MovementEntity movement = new MovementEntity();
        movement.setProduct_id(productId);
        movement.setDate_time(dateTime);
        movement.setType("Output");
        movement.setSubtype("Sales");
        movement.setReason(reason);
        movement.setComment(comment);
        movement.setQuantity(quantity);
        movement.setFrom_location_id(fromLocationId);
        movement.setTransaction_type("Sell");
        movement.setTransaction_subtype(transactionSubtype);
        movement.setTransaction_value(transactionValue);
        movement.setTransaction_currency_id(transactionCurrencyId);
        return stockRepository.decreseStockByLocationAndProduct(
                fromLocationId, productId, quantity)
                .then(movementRepository.save(movement));
    }
}
