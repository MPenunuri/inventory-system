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
    public Mono<Boolean> addProductStockInLocation(Long locationId, Long product_id, int quantity) {
        // Check first if there is a current stock
        return stockCrudRepository.findProductStockInLocation(product_id, locationId)
                .flatMap(stock -> {
                    // Return Mono with false if stock already exists
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // If not, create stock a return Mono with true
                    StockEntity stockEntity = new StockEntity();
                    stockEntity.setLocation_id(locationId);
                    stockEntity.setProduct_id(product_id);
                    stockEntity.setQuantity(quantity);
                    return stockCrudRepository.save(stockEntity).then(Mono.just(true));
                }));
    }

    @Override
    public Mono<Boolean> addProductStockInLocation(Long locationId, Long product_id,
            int quantity, int maximum_storage) {
        // Check first if there is a current stock
        return stockCrudRepository.findProductStockInLocation(product_id, locationId)
                .flatMap(stock -> {
                    // Return Mono with false if stock already exists
                    return Mono.just(false);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // If not, create stock a return Mono with true
                    StockEntity stockEntity = new StockEntity();
                    stockEntity.setLocation_id(locationId);
                    stockEntity.setProduct_id(product_id);
                    stockEntity.setQuantity(quantity);
                    stockEntity.setMaximumStorage(maximum_storage);
                    return stockCrudRepository.save(stockEntity).then(Mono.just(true));
                }));
    }

    @Override
    public Mono<Boolean> removeProductStockInLocation(Long locationId, Long product_id) {
        // Check first if there is a current stock
        return stockCrudRepository.findProductStockInLocation(product_id, locationId)
                .flatMap(stock -> {
                    // Delete stock and return Mono with true
                    return stockCrudRepository.deleteById(stock.getId()).then(Mono.just(true));
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
    public Mono<StockEntity> increseStockByLocationAndProduct(Long locationId, Long product_id, int increse) {
        // Check if there is a current stock
        return stockCrudRepository.findProductStockInLocation(product_id, locationId)
                // If there is, increse stock quantity
                .flatMap(stock -> {
                    stock.setQuantity(stock.getQuantity() + increse);
                    return stockCrudRepository.save(stock);
                }).switchIfEmpty(Mono.defer(() -> {
                    // If not, create stock and set increase as quantity
                    StockEntity stockEntity = new StockEntity();
                    stockEntity.setLocation_id(locationId);
                    stockEntity.setProduct_id(product_id);
                    stockEntity.setQuantity(increse);
                    return stockCrudRepository.save(stockEntity);
                }));
    }

    @Override
    public Mono<StockEntity> decreseStockByLocationAndProduct(Long locationId, Long product_id, int decrese) {
        return stockCrudRepository.findProductStockInLocation(product_id, locationId)
                .flatMap(stock -> {
                    stock.setQuantity(stock.getQuantity() - decrese);
                    return stockCrudRepository.save(stock);
                });
    }

}
