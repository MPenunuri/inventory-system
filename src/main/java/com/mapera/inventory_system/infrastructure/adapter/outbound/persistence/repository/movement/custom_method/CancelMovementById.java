package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.custom_method;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepository;

import reactor.core.publisher.Mono;

public class CancelMovementById {
        public static Mono<Boolean> execute(
                        MovementCrudRepository movementCrudRepository,
                        StockRepository stockRepository,
                        long movementId) {
                return movementCrudRepository
                                .findById(movementId)
                                .flatMap(movement -> {
                                        String type = movement.getType();

                                        if (type.equals("Entry")) {
                                                return stockRepository.decreseStockByLocationAndProduct(
                                                                movement.getTo_location_id(),
                                                                movement.getProduct_id(),
                                                                movement.getQuantity()).then(
                                                                                movementCrudRepository.deleteById(
                                                                                                movementId))
                                                                .then(Mono.just(true));
                                        }

                                        if (type.equals("Output")) {
                                                return stockRepository.increseStockByLocationAndProduct(
                                                                movement.getFrom_location_id(),
                                                                movement.getProduct_id(),
                                                                movement.getQuantity()).then(
                                                                                movementCrudRepository.deleteById(
                                                                                                movementId))
                                                                .then(Mono.just(true));
                                        }

                                        if (type.equals("Transfer")) {
                                                return stockRepository.increseStockByLocationAndProduct(
                                                                movement.getFrom_location_id(),
                                                                movement.getProduct_id(),
                                                                movement.getQuantity())
                                                                .then(
                                                                                stockRepository.decreseStockByLocationAndProduct(
                                                                                                movement.getTo_location_id(),
                                                                                                movement.getProduct_id(),
                                                                                                movement.getQuantity()))
                                                                .then(
                                                                                movementCrudRepository.deleteById(
                                                                                                movementId))
                                                                .then(Mono.just(true));
                                        }

                                        return Mono.just(false);
                                })
                                .switchIfEmpty(Mono.defer(() -> Mono.just(false)));
        }
}