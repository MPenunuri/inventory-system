package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.SupplierApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierPatchRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.supplier.SupplierRegisterRequest;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SupplierEntity> registerSupplier(
            @Valid @RequestBody SupplierRegisterRequest request) {
        return supplierApplicationService.registerSupplier(request.getName());
    }

    @GetMapping
    public Flux<SupplierEntity> getSuppliers() {
        return supplierApplicationService.getSuppliers();
    }

    @PatchMapping
    public Mono<SupplierEntity> renameSupplier(
            @Valid @RequestBody SupplierPatchRequest request) {
        return supplierApplicationService.renameSupplier(request.getId(), request.getName());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteSupplier(@PathVariable Long id) {
        return supplierApplicationService.deleteSupplier(id);
    }

}
