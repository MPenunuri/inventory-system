package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.StockEntity;

import reactor.core.publisher.Mono;

@Repository
public class StockRepositoryImpl implements StockRepositoryCustom {

    @Autowired
    StockCrudRepository stockCrudRepository;

    @Override
    public Mono<Boolean> addProductStockInLocation(Long userId,
            long locationId, long productId, int quantity,
            Integer maximumStorage) {
        return stockCrudRepository.findProductStockInLocation(productId, locationId)
                .flatMap(stock -> {
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    StockEntity stockEntity = new StockEntity();
                    stockEntity.setUser_id(userId);
                    stockEntity.setLocation_id(locationId);
                    stockEntity.setProduct_id(productId);
                    stockEntity.setQuantity(quantity);
                    if (maximumStorage != null) {
                        stockEntity.setMaximumStorage(maximumStorage);
                    }
                    return stockCrudRepository.save(stockEntity).then(Mono.just(true));
                }));

    }

    @Override
    public Mono<Boolean> removeProductStockInLocation(Long userId,
            Long locationId, Long product_id) {
        // Check first if there is a current stock
        return stockCrudRepository.findProductStockInLocation(product_id, locationId)
                .flatMap(stock -> {
                    if (!stock.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    // Delete stock and return Mono with true
                    return stockCrudRepository.deleteById(
                            stock.getId()).then(Mono.just(true));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Return Mono with false if stock does not exists
                    return Mono.just(false);
                }));
    }

    @Override
    public Mono<StockEntity> increseQuantityInStock(Long stockId, int increse) {
        return stockCrudRepository.findById(stockId)
                .flatMap(stock -> {
                    stock.setQuantity(stock.getQuantity() + increse);
                    return stockCrudRepository.save(stock);
                });
    }

    @Override
    public Mono<StockEntity> decreseQuantityInStock(Long stockId, int decrese) {
        return stockCrudRepository.findById(stockId)
                .flatMap(stock -> {
                    stock.setQuantity(stock.getQuantity() - decrese);
                    return stockCrudRepository.save(stock);
                });
    }

    @Override
    public Mono<StockEntity> increseStockByLocationAndProduct(
            Long userId, Long locationId, Long product_id, int increse) {
        // Check if there is a current stock
        return stockCrudRepository.findProductStockInLocation(product_id, locationId)
                // If there is, increse stock quantity
                .flatMap(stock -> {
                    if (!stock.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    stock.setQuantity(stock.getQuantity() + increse);
                    return stockCrudRepository.save(stock);
                }).switchIfEmpty(Mono.defer(() -> {
                    // If not, create stock and set increase as quantity
                    StockEntity stockEntity = new StockEntity();
                    stockEntity.setUser_id(userId);
                    stockEntity.setLocation_id(locationId);
                    stockEntity.setProduct_id(product_id);
                    stockEntity.setQuantity(increse);
                    return stockCrudRepository.save(stockEntity);
                }));
    }

    @Override
    public Mono<StockEntity> decreseStockByLocationAndProduct(
            Long userId, Long locationId, Long product_id, int decrese) {
        return stockCrudRepository.findProductStockInLocation(product_id, locationId)
                .flatMap(stock -> {
                    if (!stock.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    stock.setQuantity(stock.getQuantity() - decrese);
                    return stockCrudRepository.save(stock);
                });
    }

}
