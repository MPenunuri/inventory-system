package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method;

import java.time.LocalDateTime;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;

import reactor.core.publisher.Mono;

public class AddCustomerReturnEntryMovement {
    public static Mono<MovementEntity> execute(Long userId,
            MovementCrudRepository movementRepository, StockRepository stockRepository,
            long productId, LocalDateTime dateTime, String reason,
            String comment, int quantity, long toLocationId,
            String transactionSubtype, double transactionValue, long transactionCurrencyId) {

        MovementEntity movement = new MovementEntity();
        movement.setUser_id(userId);
        movement.setProduct_id(productId);
        movement.setDate_time(dateTime);
        movement.setType("Entry");
        movement.setSubtype("Customer return");
        movement.setReason(reason);
        movement.setComment(comment);
        movement.setQuantity(quantity);
        movement.setTo_location_id(toLocationId);
        movement.setTransaction_type("Return");
        movement.setTransaction_subtype(transactionSubtype);
        movement.setTransaction_value(transactionValue);
        movement.setTransaction_currency_id(transactionCurrencyId);
        return stockRepository.increseStockByLocationAndProduct(userId,
                toLocationId, productId, quantity)
                .then(movementRepository.save(movement));
    }
}
