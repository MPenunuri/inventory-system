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

import com.mapera.inventory_system.application.security.AuthenticationService;
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

    @Autowired
    private AuthenticationService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CurrencyEntity> registerCurrency(
            @Valid @RequestBody RegisterCurrencyRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return currencyApplicationService.registerCurrency(
                    userId, request.getName());
        });
    }

    @GetMapping
    public Flux<CurrencyEntity> getCurrencies() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return currencyApplicationService.getCurrencies(userId);
        });
    }

    @PatchMapping
    public Mono<CurrencyEntity> renameCurrency(
            @Valid @RequestBody PatchCurrencyRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return currencyApplicationService.renameCurrency(
                    userId, request.getId(), request.getName());
        });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCurrency(@PathVariable Long id) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return currencyApplicationService.deleteCurrency(userId, id);
        });
    }

}
