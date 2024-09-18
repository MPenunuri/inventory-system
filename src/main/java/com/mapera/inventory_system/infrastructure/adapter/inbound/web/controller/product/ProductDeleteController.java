package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.ProductApplicationService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/product")
public class ProductDeleteController {

    @Autowired
    private ProductApplicationService productApplicationService;

    @DeleteMapping("/{productId}")
    public Mono<Void> deleteProductById(@PathVariable Long productId) {
        return productApplicationService.deleteProductById(productId);
    }

}
