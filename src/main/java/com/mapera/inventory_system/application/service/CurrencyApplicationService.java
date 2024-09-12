package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.CurrencyPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrencyApplicationService {

    @Autowired
    private CurrencyPersistencePort currencyPersistencePort;

    public Flux<CurrencyEntity> getCurrencies(String name) {
        return currencyPersistencePort.getCurrencies(name);
    }

    public Mono<CurrencyEntity> registerCurrency(String name) {
        return currencyPersistencePort.registerCurrency(name);
    }

    public Mono<String> getCurrencyNameById(Long currencyId) {
        return currencyPersistencePort.getCurrencyNameById(currencyId);
    }

    public Mono<CurrencyEntity> renameCurrency(Long currencyId, String name) {
        return currencyPersistencePort.renameCurrency(currencyId, name);
    }

    public Mono<Void> deleteCurrency(Long currencyId) {
        return currencyPersistencePort.deleteCurrency(currencyId);
    }
}
