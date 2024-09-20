package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.supplier_repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;

import com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.product_repository.Samples;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.SupplierEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataR2dbcTest
public class UpdateSupplier {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    public void test() {
        Samples samples = new Samples();
        UserEntity userEntity = samples.user();
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setName("International company");

        Mono<Void> savedUser = userRepository.save(userEntity).doOnNext(u -> {
            supplierEntity.setUser_id(u.getId());
        }).then();

        StepVerifier.create(savedUser).verifyComplete();

        Mono<Long> savedSupplierId = supplierRepository.save(supplierEntity).map(s -> s.getId());

        Mono<SupplierEntity> updatedSupplier = savedSupplierId
                .flatMap(id -> supplierRepository.renameSupplier(supplierEntity.getUser_id(), id, "Local company"));

        StepVerifier.create(updatedSupplier).expectNextMatches(s -> s.getName().equals("Local company"))
                .verifyComplete();
    }
}
