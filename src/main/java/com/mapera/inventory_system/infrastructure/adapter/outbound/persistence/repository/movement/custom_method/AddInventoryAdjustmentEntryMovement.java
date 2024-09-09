package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method;

import java.time.LocalDateTime;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;

import reactor.core.publisher.Mono;

public class AddInventoryAdjustmentEntryMovement {
    public static Mono<MovementEntity> execute(
            MovementCrudRepository movementRepository, StockRepository stockRepository, long productId,
            LocalDateTime dateTime, String reason,
            String comment, int quantity, long toLocationId) {

        MovementEntity movement = new MovementEntity();
        movement.setProduct_id(productId);
        movement.setDate_time(dateTime);
        movement.setType("Entry");
        movement.setSubtype("Inventory adjustment");
        movement.setReason(reason);
        movement.setComment(comment);
        movement.setQuantity(quantity);
        movement.setTo_location_id(toLocationId);
        return stockRepository.increseStockByLocationAndProduct(
                toLocationId, productId, quantity)
                .then(movementRepository.save(movement));
    }
}
