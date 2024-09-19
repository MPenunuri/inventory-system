
package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.service.MovementApplicationService;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AcquisitionDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.AverageCostProductDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.CustomerReturnDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.EntryMovementDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.movement.ProductionDTO;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/secure/movement")
@Validated
public class EntryGetController {

    @Autowired
    MovementApplicationService movementApplicationService;

    @GetMapping("/acquisition")
    public Flux<AcquisitionDTO> getAcquisitions() {
        return movementApplicationService.getAcquisitions();
    }

    @GetMapping("/customer-return")
    public Flux<CustomerReturnDTO> getCustomerReturns() {
        return movementApplicationService.getCustomerReturns();
    }

    @GetMapping("/entry-adjustment")
    public Flux<EntryMovementDTO> getEntryInventoryAdjustments() {
        return movementApplicationService.getEntryInventoryAdjustments();
    }

    @GetMapping("/production")
    public Flux<ProductionDTO> getProductions() {
        return movementApplicationService.getProductions();
    }

    @GetMapping("/acquisition/supplier/{supplierId}")
    public Flux<AcquisitionDTO> getAcquisitionsBySupplierId(@PathVariable Long supplierId) {
        return movementApplicationService.getAcquisitionsBySupplierId(supplierId);
    }

    @GetMapping("/acquisition/cost/{currencyId}")
    public Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(
            @PathVariable Long currencyId,
            @RequestParam String costType,
            @RequestParam double minCost,
            @RequestParam double maxCost,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.findAcquisitionsByCostAndYear(
                costType, currencyId, minCost, maxCost, fromYear, toYear);
    }

    @GetMapping("/acquisition/avg-unit/cost/{productId}")
    public Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.getAvgUnitCostByAcquisition(
                productId, currencyId, fromYear, toYear);
    }

    @GetMapping("/acquisition/avg-total/cost/{productId}")
    public Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.getAvgTotalCostByAcquisition(
                productId, currencyId, fromYear, toYear);
    }

    @GetMapping("/production/cost/{currencyId}")
    public Flux<ProductionDTO> findProductionByCostAndYear(
            @PathVariable Long currencyId,
            @RequestParam String costType,
            @RequestParam double minCost,
            @RequestParam double maxCost,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.findProductionByCostAndYear(
                costType, currencyId, minCost, maxCost, fromYear, toYear);
    }

    @GetMapping("/production/avg-unit/cost/{productId}")
    public Flux<AverageCostProductDTO> getAvgUnitProductionCost(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.getAvgUnitProductionCost(
                productId, currencyId, fromYear, toYear);
    }

    @GetMapping("/production/avg-total/cost/{productId}")
    public Flux<AverageCostProductDTO> getAvgTotalProductionCost(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return movementApplicationService.getAvgTotalProductionCost(
                productId, currencyId, fromYear, toYear);
    }

}
