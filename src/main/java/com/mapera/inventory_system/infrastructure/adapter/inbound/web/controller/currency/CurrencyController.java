package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.CurrencyApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.currency.PatchCurrencyRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.currency.RegisterCurrencyRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.CurrencyEntity;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/currency")
@Validated
public class CurrencyController {

    @Autowired
    CurrencyApplicationService currencyApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CurrencyEntity> registerCurrency(
            @Valid @RequestBody RegisterCurrencyRequest request) {
        return currencyApplicationService.registerCurrency(request.getName());
    }

    @GetMapping
    public Flux<CurrencyEntity> getCurrencies() {
        return currencyApplicationService.getCurrencies();
    }

    @PatchMapping
    public Mono<CurrencyEntity> renameCurrency(
            @Valid @RequestBody PatchCurrencyRequest request) {
        return currencyApplicationService.renameCurrency(
                request.getId(), request.getName());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCurrency(@PathVariable Long id) {
        return currencyApplicationService.deleteCurrency(id);
    }

}
