package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AverageSellProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.OutputMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.SaleDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.SupplierReturnDTO;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/secure/movement")
@Validated
public class OutputGetController {

    @Autowired
    MovementApplicationService movementApplicationService;

    @GetMapping("/sale")
    public Flux<SaleDTO> getSales() {
        return movementApplicationService.getSales();
    }

    @GetMapping("/supplier-return")
    public Flux<SupplierReturnDTO> getSupplierReturns() {
        return movementApplicationService.getSupplierReturns();
    }

    @GetMapping("/output-adjusment")
    public Flux<OutputMovementDTO> getOutputInventoryAdjustments() {
        return movementApplicationService.getOutputInventoryAdjustments();
    }

    @GetMapping("/internal-consumption")
    public Flux<OutputMovementDTO> getInternalConsumptionMovements() {
        return movementApplicationService.getInternalConsumptionMovements();
    }

    @GetMapping("/sales/{currencyId}")
    public Flux<SaleDTO> findSalesByValueAndYear(
            @PathVariable Long currencyId,
            @RequestParam String sellType,
            @RequestParam double minValue,
            @RequestParam double maxValue,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.findSalesByValueAndYear(sellType,
                currencyId, minValue, maxValue, fromYear, toYear);
    }

    @GetMapping("/sales/avg-unit/{productId}")
    public Flux<AverageSellProductDTO> getAvgUnitSellValue(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.getAvgUnitSellValue(
                productId, currencyId, fromYear, toYear);
    }

    @GetMapping("/sales/avg-total/{productId}")
    public Flux<AverageSellProductDTO> getAvgTotalSellValue(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.getAvgTotalSellValue(
                productId, currencyId, fromYear, toYear);
    }
}
