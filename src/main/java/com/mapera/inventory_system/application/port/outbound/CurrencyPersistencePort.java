package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyPersistencePort {

    public Flux<CurrencyEntity> getCurrencies();

    public Mono<CurrencyEntity> registerCurrency(String name);

    public Mono<String> getCurrencyNameById(Long currencyId);

    public Mono<CurrencyEntity> renameCurrency(Long currencyId, String name);

    public Mono<Void> deleteCurrency(Long currencyId);
}
