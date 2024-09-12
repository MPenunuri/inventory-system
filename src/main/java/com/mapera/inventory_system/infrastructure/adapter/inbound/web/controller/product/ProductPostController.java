package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.ProductApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.ProductPostRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products/post")
public class ProductPostController {

    @Autowired
    private ProductApplicationService productApplicationService;

    @PostMapping
    public Mono<ProductEntity> registerProduct(
            @RequestBody ProductPostRequest productRequest) {
        return productApplicationService.registerProduct(productRequest.getName());
    }

}
