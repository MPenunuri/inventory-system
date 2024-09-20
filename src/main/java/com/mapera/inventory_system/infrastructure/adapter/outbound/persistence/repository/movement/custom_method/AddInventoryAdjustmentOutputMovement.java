package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method;

import java.time.LocalDateTime;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.MovementEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;

import reactor.core.publisher.Mono;

public class AddInventoryAdjustmentOutputMovement {
    public static Mono<MovementEntity> execute(Long userId,
            MovementCrudRepository movementRepository, StockRepository stockRepository,
            long productId, LocalDateTime dateTime, String reason, String comment,
            int quantity, long fromLocationId) {
        MovementEntity movement = new MovementEntity();
        movement.setUser_id(userId);
        movement.setProduct_id(productId);
        movement.setDate_time(dateTime);
        movement.setType("Output");
        movement.setSubtype("Inventory adjustment");
        movement.setReason(reason);
        movement.setComment(comment);
        movement.setQuantity(quantity);
        movement.setFrom_location_id(fromLocationId);
        return stockRepository.decreseStockByLocationAndProduct(userId,
                fromLocationId, productId, quantity)
                .then(movementRepository.save(movement));
    }
}
