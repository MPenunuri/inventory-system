package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.currency.CurrencyDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyCrudRepository extends ReactiveCrudRepository<CurrencyEntity, Long> {

    @Query("SELECT COUNT(*) FROM currencies c WHERE c.user_id = :userId ")
    Mono<Integer> countByUserId(Long userId);

    @Query("DELETE FROM currencies c WHERE c.user_id = :userId ")
    Mono<Void> deleteByUserId(Long userId);

    @Query("SELECT * FROM currencies c WHERE c.user_id = :userId AND c.name = :name ")
    Mono<CurrencyEntity> getCurrencyByName(Long userId, String name);

    @Query("SELECT c.id AS currency_id, " +
            "c.name AS currency_name, " +
            "COUNT(DISTINCT p.id) AS products, " +
            "COUNT(DISTINCT m.id) AS movements " +
            "FROM currencies c " +
            "LEFT JOIN products p ON p.price_currency_id = c.id " +
            "LEFT JOIN movements m ON m.transaction_currency_id = c.id " +
            "WHERE c.user_id = :userId " +
            "GROUP BY c.id, c.name ")
    public Flux<CurrencyDTO> findAllUserCurrencies(Long userId);

}
