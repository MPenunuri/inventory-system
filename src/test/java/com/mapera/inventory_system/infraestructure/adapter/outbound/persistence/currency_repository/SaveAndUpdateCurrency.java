package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.currency_repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class SaveAndUpdateCurrency {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void test() {
        CurrencyEntity currency = new CurrencyEntity();
        currency.setName("USD");

        Mono<Long> savedCurrencyId = currencyRepository.save(currency).map(c -> c.getId());
        Mono<CurrencyEntity> updatedCurrency = savedCurrencyId
                .flatMap(id -> currencyRepository.renameCurrency(id, "MXN"));

        StepVerifier.create(updatedCurrency).expectNextMatches(c -> c.getName().equals("MXN")).verifyComplete();
    }
}
