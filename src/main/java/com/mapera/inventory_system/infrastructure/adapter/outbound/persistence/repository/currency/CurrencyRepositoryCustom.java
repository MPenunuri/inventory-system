
package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Mono;

public interface CurrencyRepositoryCustom {
    public Mono<CurrencyEntity> renameCurrency(Long userId, Long currencyId, String name);
}
