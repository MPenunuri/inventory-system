package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.currency_repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class SaveAndUpdateCurrency {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void test() {
        Samples samples = new Samples();
        UserEntity userEntity = samples.user();
        CurrencyEntity currency = new CurrencyEntity();
        currency.setName("USD");

        Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
            currency.setUser_id(u.getId());
        }).then();

        StepVerifier.create(savedUser).verifyComplete();

        Mono<Long> savedCurrencyId = currencyRepository.save(currency).map(c -> c.getId());
        Mono<CurrencyEntity> updatedCurrency = savedCurrencyId
                .flatMap(id -> currencyRepository.renameCurrency(currency.getUser_id(), id, "MXN"));

        StepVerifier.create(updatedCurrency).expectNextMatches(c -> c.getName().equals("MXN")).verifyComplete();
    }
}
