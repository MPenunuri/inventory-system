package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.service.ProductApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.product.ProductPostRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/product")
@Validated
public class ProductPostController {

    @Autowired
    private ProductApplicationService productApplicationService;

    @Autowired
    private AuthenticationService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductEntity> registerProduct(
            @Valid @RequestBody ProductPostRequest productRequest) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return productApplicationService
                    .registerProduct(userId, productRequest.getName());
        });

    }

}
