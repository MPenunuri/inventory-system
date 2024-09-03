package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.StockEntity;

import reactor.core.publisher.Mono;

public interface StockRepositoryCustom {
    public Mono<Boolean> addProductStockInLocation(Long locationId, Long product_id, int quantity);

    public Mono<Boolean> addProductStockInLocation(Long locationId, Long product_id, int quantity,
            int maximum_storage);

    public Mono<Boolean> removeProductStockInLocation(Long locationId, Long product_id);

    public Mono<StockEntity> increseQuantityInStock(Long stockId, int increse);

    public Mono<StockEntity> decreseQuantityInStock(Long stockId, int increse);
}
