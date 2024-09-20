package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Flux;

public interface CurrencyCrudRepository extends ReactiveCrudRepository<CurrencyEntity, Long> {

    @Query("SELECT * FROM currencies c WHERE c.user_id = :userId ")
    public Flux<CurrencyEntity> findAllUserCurrencies(Long userId);

}
