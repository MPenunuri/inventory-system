
package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mapera.inventory_system.application.port.outbound.CurrencyPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CurrencyRepositoryImpl implements CurrencyRepositoryCustom, CurrencyPersistencePort {

    @Autowired
    CurrencyCrudRepository currencyCrudRepository;

    @Override
    public Flux<CurrencyEntity> getCurrencies() {
        return currencyCrudRepository.findAll()
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Not currencies found")));
    }

    @Override
    public Mono<CurrencyEntity> registerCurrency(String name) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setName(name);
        return currencyCrudRepository.save(currencyEntity);
    }

    @Override
    public Mono<CurrencyEntity> renameCurrency(Long currencyId, String name) {
        return currencyCrudRepository.findById(currencyId).flatMap(c -> {
            c.setName(name);
            return currencyCrudRepository.save(c);
        }).switchIfEmpty(
                Mono.error(new RuntimeException("Currency not found")));
    }

    @Override
    public Mono<String> getCurrencyNameById(Long currencyId) {
        return currencyCrudRepository.findById(currencyId).map(c -> c.getName()).switchIfEmpty(
                Mono.error(new RuntimeException("Currency not found")));
    }

    @Override
    public Mono<Void> deleteCurrency(Long currencyId) {
        return currencyCrudRepository.deleteById(currencyId).onErrorMap(error -> {
            return new IllegalArgumentException(
                    "Failed to delete currency with ID: " + currencyId, error);
        });
    }

}
