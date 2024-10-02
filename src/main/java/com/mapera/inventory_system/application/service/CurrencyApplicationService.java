package com.mapera.inventory_system.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.application.port.outbound.CurrencyPersistencePort;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.currency.CurrencyDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CurrencyApplicationService {

    @Autowired
    private CurrencyPersistencePort currencyPersistencePort;

    public Flux<CurrencyDTO> getCurrencies(Long userId) {
        return currencyPersistencePort.getCurrencies(userId);
    }

    public Mono<CurrencyEntity> registerCurrency(Long userId, String name) {
        return currencyPersistencePort.registerCurrency(userId, name);
    }

    public Mono<String> getCurrencyNameById(Long userId, Long currencyId) {
        return currencyPersistencePort.getCurrencyNameById(userId, currencyId);
    }

    public Mono<CurrencyEntity> renameCurrency(Long userId, Long currencyId, String name) {
        return currencyPersistencePort.renameCurrency(userId, currencyId, name);
    }

    public Mono<Void> deleteCurrency(Long userId, Long currencyId) {
        return currencyPersistencePort.deleteCurrency(userId, currencyId);
    }
}
