package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mapera.inventory_system.application.security.AuthenticationService;
import com.mapera.inventory_system.application.service.LocationApplicationService;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.location.PatchLocationRequest;
import com.mapera.inventory_system.infrastructure.adapter.inbound.web.dto.location.RegisterLocationRequest;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.location.LocationDTO;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.LocationEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockRepositoryImpl;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure/location")
@Validated
public class LocationController {

    @Autowired
    LocationApplicationService locationApplicationService;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private StockRepositoryImpl stockRepositoryImpl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<LocationEntity> registerLocation(
            @Valid @RequestBody RegisterLocationRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            if (request.getAddress() == null) {
                return locationApplicationService.registerLocation(
                        userId, request.getName());
            }
            return locationApplicationService.registerLocation(
                    userId, request.getName(), request.getAddress());
        });
    }

    @GetMapping
    public Flux<LocationDTO> getLocations() {
        return authService.getUserIdFromToken().flatMapMany(userId -> {
            return locationApplicationService.getLocations(userId);
        });
    }

    @GetMapping("/{id}")
    public Mono<Location> findLocationById(@PathVariable Long id) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return locationApplicationService.findLocationById(userId, id);
        });
    }

    @PatchMapping("/name")
    public Mono<LocationEntity> updateLocationName(
            @Valid @RequestBody PatchLocationRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return locationApplicationService.updateLocationName(
                    userId, request.getId(), request.getName());
        });

    }

    @PatchMapping("/address")
    public Mono<LocationEntity> updateLocationAddress(
            @Valid @RequestBody PatchLocationRequest request) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return locationApplicationService.updateLocationAddress(
                    userId, request.getId(), request.getAddress());
        });
    }

    @PatchMapping("/maximum-stock/{locationId}/{productId}/{quantity}")
    public Mono<Boolean> setMaximumStorageInLocation(
            @PathVariable Long locationId, @PathVariable Long productId, @PathVariable int quantity) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return stockRepositoryImpl.setMaximumStorageInLocation(
                    userId, locationId, productId, quantity);
        });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteLocation(@PathVariable Long id) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return locationApplicationService.deleteLocationById(userId, id);
        });
    }

    @DeleteMapping("/stock")
    public Mono<Boolean> removeProductStockInLocation(@RequestParam Long locationId, @RequestParam Long productId) {
        return authService.getUserIdFromToken().flatMap(userId -> {
            return stockRepositoryImpl.removeProductStockInLocation(userId, locationId, productId);
        });
    }
}
