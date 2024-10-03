package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.service.ProductSupplierApplicationService;
import com.mapera.inventory_system.application.service.SupplierApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.ProductSupplierRelationRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierPatchRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierRegisterRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.supplier.SupplierDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/secure/supplier")
@Validated
public class SupplierController {

    @Autowired
    SupplierApplicationService supplierApplicationService;

    @Autowired
    ProductSupplierApplicationService productSupplierApplicationService;

    @Autowired
    private AuthenticationService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SupplierEntity> registerSupplier(
            @Valid @RequestBody SupplierRegisterRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return supplierApplicationService.registerSupplier(
                    userId, request.getName());
        });
    }

    @GetMapping
    public Flux<SupplierDTO> getSuppliers() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return supplierApplicationService.getSuppliers(userId);
        });
    }

    @PatchMapping
    public Mono<SupplierEntity> renameSupplier(
            @Valid @RequestBody SupplierPatchRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return supplierApplicationService.renameSupplier(
                    userId, request.getId(), request.getName());
        });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSupplier(@PathVariable Long id) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return supplierApplicationService.deleteSupplier(userId, id);
        });
    }

    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Boolean> addProductSupplierRelation(
            @Valid @RequestBody ProductSupplierRelationRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return productSupplierApplicationService.addProductSupplierRelation(
                    userId,
                    request.getProductId(), request.getSupplierId());
        });

    }

    @DeleteMapping("/product")
    public Mono<Boolean> deleteProductSupplierRelation(
            @RequestParam Long productId, @RequestParam Long supplierId) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return productSupplierApplicationService.deleteProductSupplierRelation(
                    userId,
                    productId, supplierId);
        });

    }

}
