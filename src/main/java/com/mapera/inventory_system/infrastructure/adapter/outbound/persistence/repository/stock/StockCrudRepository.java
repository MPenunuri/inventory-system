package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.StockEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StockCrudRepository extends ReactiveCrudRepository<StockEntity, Long> {

    @Query("SELECT COUNT(*) FROM stock_list sl WHERE sl.user_id = :userId ")
    Mono<Integer> countByUserId(Long userId);

    @Query("DELETE FROM stock_list sl WHERE sl.user_id = :userId ")
    Mono<Void> deleteByUserId(Long userId);

    @Query("SELECT * FROM stock_list sl WHERE sl.product_id = :productId AND sl.location_id = :locationId " +
            "AND sl.user_id = :userId ")
    Mono<StockEntity> findProductStockInLocation(Long productId, Long locationId, Long userId);

    @Query("SELECT * FROM stock_list sl WHERE sl.product_id = :productId " +
            "AND sl.user_id = :userId ")
    Flux<StockEntity> findProductStock(Long productId, Long userId);
}
