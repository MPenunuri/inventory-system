
package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Mono;

@Repository
public class CurrencyRepositoryImpl implements CurrencyRepositoryCustom {

    @Autowired
    CurrencyCrudRepository currencyCrudRepository;

    @Override
    public Mono<CurrencyEntity> renameCurrency(Long currencyId, String name) {
        return currencyCrudRepository.findById(currencyId).flatMap(c -> {
            c.setName(name);
            return currencyCrudRepository.save(c);
        });
    }

}
