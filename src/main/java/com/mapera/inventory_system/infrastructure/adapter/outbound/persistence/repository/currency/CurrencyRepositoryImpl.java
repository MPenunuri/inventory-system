
package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public Flux<CurrencyEntity> getCurrencies(Long userId) {
        return currencyCrudRepository.findAllUserCurrencies(userId)
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Not currencies found")));
    }

    @Override
    public Mono<CurrencyEntity> registerCurrency(Long userId, String name) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setUser_id(userId);
        currencyEntity.setName(name);
        return currencyCrudRepository.save(currencyEntity);
    }

    @Override
    public Mono<CurrencyEntity> renameCurrency(Long userId, Long currencyId, String name) {
        return currencyCrudRepository.findById(currencyId).flatMap(c -> {
            if (!c.getUser_id().equals(userId)) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            c.setName(name);
            return currencyCrudRepository.save(c);
        }).switchIfEmpty(
                Mono.error(new RuntimeException("Currency not found")));
    }

    @Override
    public Mono<String> getCurrencyNameById(Long userId, Long currencyId) {
        return currencyCrudRepository.findById(currencyId).flatMap(c -> {
            if (!c.getUser_id().equals(userId)) {
                throw new IllegalArgumentException("Invalid credentials");
            }
            return Mono.just(c.getName());
        }).switchIfEmpty(
                Mono.error(new RuntimeException("Currency not found")));
    }

    @Override
    public Mono<Void> deleteCurrency(Long userId, Long currencyId) {
        return currencyCrudRepository.findById(currencyId)
                .switchIfEmpty(
                        Mono.error(new RuntimeException("Currency not found")))
                .flatMap(c -> {
                    if (!c.getUser_id().equals(userId)) {
                        throw new IllegalArgumentException("Invalid credentials");
                    }
                    return currencyCrudRepository.delete(c);
                }).onErrorMap(error -> {
                    if (error instanceof DataIntegrityViolationException) {
                        return new IllegalStateException(
                                "Failed to delete currency with ID: " + currencyId + ". " +
                                        "The currency is associated with other records and cannot be deleted. "
                                        +
                                        "Please remove any related registry before attempting to delete this currency.");
                    }
                    return new IllegalArgumentException(
                            "Failed to delete currency with ID: " + currencyId + ". Unexpected error occurred.");
                });
    }

}
