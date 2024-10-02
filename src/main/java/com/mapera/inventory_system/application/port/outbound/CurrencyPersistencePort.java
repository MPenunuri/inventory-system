package com.mapera.inventory_system.application.port.outbound;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.currency.CurrencyDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyPersistencePort {

    public Flux<CurrencyDTO> getCurrencies(Long userId);

    public Mono<CurrencyEntity> registerCurrency(Long userId, String name);

    public Mono<String> getCurrencyNameById(Long userId, Long currencyId);

    public Mono<CurrencyEntity> renameCurrency(Long userId, Long currencyId, String name);

    public Mono<Void> deleteCurrency(Long userId, Long currencyId);
}
