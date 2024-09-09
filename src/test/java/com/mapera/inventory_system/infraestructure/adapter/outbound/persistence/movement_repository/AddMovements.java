package com.mapera.inventory_system.infraestructure.adapter.outbound.persistence.movement_repository;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.ProductEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AddMovements {
        MovementRepository movementRepository;
        Long productId;
        ProductEntity productEntity;
        Long supplierId;
        Long location1Id;
        Long location2Id;
        Long currencyId;
        AtomicReference<Long> transferId;

        public AddMovements(
                        MovementRepository movementRepository,
                        Long productId,
                        ProductEntity productEntity,
                        Long supplierId,
                        Long location1Id,
                        Long location2Id,
                        Long currencyId) {
                this.movementRepository = movementRepository;
                this.productId = productId;
                this.productEntity = productEntity;
                this.supplierId = supplierId;
                this.location1Id = location1Id;
                this.location2Id = location2Id;
                this.currencyId = currencyId;
                this.transferId = new AtomicReference<>();
        }

        public Flux<Void> setEntries() {
                Mono<Void> addAcquisition = movementRepository.addAcquisitionEntryMovement(
                                productId,
                                LocalDateTime.of(2022, 10, 11, 10, 30, 45),
                                "Regular acquisition",
                                "No comment", 20, supplierId,
                                location1Id, "Per unit",
                                .75, currencyId)
                                .then(movementRepository.addAcquisitionEntryMovement(
                                                productId,
                                                LocalDateTime.of(2023, 12, 25, 10, 30, 45),
                                                "Regular acquisition",
                                                "No comment", 20, supplierId,
                                                location1Id, "Per unit",
                                                .75, currencyId))
                                .then(movementRepository.addAcquisitionEntryMovement(
                                                productId,
                                                LocalDateTime.of(2024, 2, 14, 10, 30, 45),
                                                "Regular acquisition",
                                                "No comment", 20, supplierId,
                                                location1Id, "Per unit",
                                                1, currencyId))
                                .then(movementRepository.addAcquisitionEntryMovement(
                                                productId,
                                                LocalDateTime.of(2025, 5, 2, 10, 30, 45),
                                                "Regular acquisition",
                                                "No comment", 20, supplierId,
                                                location1Id, "Per unit",
                                                .75, currencyId))
                                .then();

                Mono<Void> addCustomerReturn = movementRepository.addCustomerReturnEntryMovement(
                                productId,
                                LocalDateTime.of(2024, 2, 14, 10, 30, 45),
                                "Defective product",
                                "The product was damaged", 1, location1Id, "Per unit",
                                1.00, currencyId).then();

                Mono<Void> addEntryAdjusment = movementRepository.addInventoryAdjustmentEntryMovement(
                                productId,
                                LocalDateTime.of(2024, 2, 14, 10, 30, 45),
                                "No registered product units",
                                "No comment", 20, location1Id).then();

                Mono<Void> addProductionEntry = movementRepository.addProductionEntryMovement(
                                productId,
                                LocalDateTime.of(2022, 10, 11, 10, 30, 45),
                                "Company production",
                                "Experimental own production", 20, location1Id, "Per unit",
                                .25, currencyId)
                                .then(movementRepository.addProductionEntryMovement(
                                                productId,
                                                LocalDateTime.of(2023, 12, 25, 10, 30, 45),
                                                "Company production",
                                                "Experimental own production", 20, location1Id, "Per unit",
                                                .5, currencyId))
                                .then(movementRepository.addProductionEntryMovement(
                                                productId,
                                                LocalDateTime.of(2024, 2, 14, 10, 30, 45),
                                                "Company production",
                                                "Experimental own production", 20, location1Id, "Per unit",
                                                .75, currencyId))
                                .then(movementRepository.addProductionEntryMovement(
                                                productId,
                                                LocalDateTime.of(2025, 5, 2, 10, 30, 45),
                                                "Company production",
                                                "Experimental own production", 20, location1Id, "Per unit",
                                                .5, currencyId))
                                .then();

                return Flux.concat(
                                addAcquisition,
                                addCustomerReturn,
                                addEntryAdjusment,
                                addProductionEntry);

        }

        public Flux<Void> setOutputs() {

                Mono<Void> addSale = movementRepository.addSalesOutputMovement(
                                productId,
                                LocalDateTime.of(2022, 1, 27, 10, 30, 45),
                                "Regular sell", "No comment",
                                20, location1Id, "Retail", productEntity.getRetail_price() - .25,
                                productEntity.getPrice_currency_id())
                                .then(movementRepository.addSalesOutputMovement(
                                                productId,
                                                LocalDateTime.of(2023, 5, 2, 10, 30, 45),
                                                "Regular sell", "No comment",
                                                20, location1Id, "Retail", productEntity.getRetail_price(),
                                                productEntity.getPrice_currency_id()))
                                .then(movementRepository.addSalesOutputMovement(
                                                productId,
                                                LocalDateTime.of(2024, 11, 13, 10, 30, 45),
                                                "Regular sell", "No comment",
                                                20, location1Id, "Retail", productEntity.getRetail_price() + .25,
                                                productEntity.getPrice_currency_id()))
                                .then(movementRepository.addSalesOutputMovement(
                                                productId,
                                                LocalDateTime.of(2025, 7, 22, 10, 30, 45),
                                                "Regular sell", "No comment",
                                                20, location1Id, "Retail", productEntity.getRetail_price(),
                                                productEntity.getPrice_currency_id()))
                                .then();

                Mono<Void> addSupplierReturn = movementRepository.addSupplierReturnOutputMovement(
                                productId, LocalDateTime.now(), "Defective product", "The product was damaged",
                                20, supplierId, location1Id, "Per unit",
                                .75, currencyId).then();

                Mono<Void> addOutputAdjustment = movementRepository.addInventoryAdjustmentOutputMovement(
                                productId, LocalDateTime.now(), "Damaged product",
                                "Product was damaged in warehouse",
                                1, location1Id).then();

                Mono<Void> addInternalConsumption = movementRepository.addInternalConsumptionOutputMovement(
                                productId, LocalDateTime.now(), "Boss party", "No comments",
                                10, location1Id).then();

                return Flux.concat(
                                addSale,
                                addSupplierReturn,
                                addOutputAdjustment,
                                addInternalConsumption);

        }

        public Flux<Void> setTransfers() {

                Mono<Void> addTransfer = movementRepository.addTransferMovement(
                                productId, LocalDateTime.now(), "Manager indication", "No comment",
                                10, location1Id, location2Id).map(t -> {
                                        transferId.set(t.getId());
                                        return t;
                                }).then();

                return Flux.concat(
                                addTransfer);

        }

        public Flux<Void> setCancelations() {

                Mono<Void> cancelTransfer = movementRepository.cancelMovementById(transferId.get()).then();
                return Flux.concat(
                                cancelTransfer);

        }

}
