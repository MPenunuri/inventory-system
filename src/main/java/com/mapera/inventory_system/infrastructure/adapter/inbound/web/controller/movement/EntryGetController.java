
package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.movement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
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

    @Autowired
    private AuthenticationService authService;

    @GetMapping("/acquisition/{productId}")
    public Flux<AcquisitionDTO> getAcquisitions(@PathVariable Long productId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getAcquisitions(userId, productId);
        });
    }

    @GetMapping("/customer-return/{productId}")
    public Flux<CustomerReturnDTO> getCustomerReturns(@PathVariable Long productId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getCustomerReturns(userId, productId);
        });
    }

    @GetMapping("/entry-adjustment/{productId}")
    public Flux<EntryMovementDTO> getEntryInventoryAdjustments(@PathVariable Long productId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getEntryInventoryAdjustments(userId, productId);
        });
    }

    @GetMapping("/production/{productId}")
    public Flux<ProductionDTO> getProductions(@PathVariable Long productId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getProductions(userId, productId);
        });
    }

    @GetMapping("/acquisition/supplier/{supplierId}")
    public Flux<AcquisitionDTO> getAcquisitionsBySupplierId(@PathVariable Long supplierId) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getAcquisitionsBySupplierId(
                    userId, supplierId);
        });
    }

    @GetMapping("/acquisition/cost/{currencyId}")
    public Flux<AcquisitionDTO> findAcquisitionsByCostAndYear(
            @PathVariable Long currencyId,
            @RequestParam String costType,
            @RequestParam double minCost,
            @RequestParam double maxCost,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.findAcquisitionsByCostAndYear(
                    userId, costType, currencyId, minCost, maxCost, fromYear, toYear);
        });
    }

    @GetMapping("/acquisition/avg-unit/cost/{productId}")
    public Flux<AverageCostProductDTO> getAvgUnitCostByAcquisition(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getAvgUnitCostByAcquisition(
                    userId, productId, currencyId, fromYear, toYear);
        });
    }

    @GetMapping("/acquisition/avg-total/cost/{productId}")
    public Flux<AverageCostProductDTO> getAvgTotalCostByAcquisition(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getAvgTotalCostByAcquisition(
                    userId, productId, currencyId, fromYear, toYear);
        });
    }

    @GetMapping("/production/cost/{currencyId}")
    public Flux<ProductionDTO> findProductionByCostAndYear(
            @PathVariable Long currencyId,
            @RequestParam String costType,
            @RequestParam double minCost,
            @RequestParam double maxCost,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.findProductionByCostAndYear(
                    userId, costType, currencyId, minCost, maxCost, fromYear, toYear);
        });
    }

    @GetMapping("/production/avg-unit/cost/{productId}")
    public Flux<AverageCostProductDTO> getAvgUnitProductionCost(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getAvgUnitProductionCost(
                    userId, productId, currencyId, fromYear, toYear);
        });
    }

    @GetMapping("/production/avg-total/cost/{productId}")
    public Flux<AverageCostProductDTO> getAvgTotalProductionCost(
            @PathVariable Long productId,
            @RequestParam Long currencyId,
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return movementApplicationService.getAvgTotalProductionCost(
                    userId, productId, currencyId, fromYear, toYear);
        });
    }

}
